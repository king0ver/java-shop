package com.enation.app.shop.trade.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.service.IGoodsSkuManager;
import com.enation.app.shop.member.model.po.Address;
import com.enation.app.shop.member.service.IMemberAddressManager;
import com.enation.app.shop.promotion.exchange.service.IExchangeManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Consignee;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.model.vo.Trade;
import com.enation.app.shop.trade.service.ICartReadManager;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.service.ITradeCreator;
import com.enation.app.shop.trade.service.ITradePriceManager;
import com.enation.app.shop.trade.service.ITradeSnCreator;
import com.enation.app.shop.trade.support.CheckoutParam;
import com.enation.app.shop.trade.support.OrderServiceConstant;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.cache.ICache;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 订单创建的redis实现
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月18日上午10:49:11
 */
@Service
public class RedisTradeCreator implements ITradeCreator {


	@Autowired
	private ICache cache;
	
	@Autowired
    private AmqpTemplate amqpTemplate;
	
	@Autowired
	private ICheckoutParamManager checkoutParamManager;
	
	@Autowired
	private ICartReadManager cartReadManager;
	
	@Autowired
	private ICartWriteManager cartWriteManager;
	
	@Autowired
	private ITradePriceManager tradePriceManager;
	
	@Autowired
	private IMemberAddressManager memberAddressManager;
	
	@Autowired
	private IGoodsSkuManager goodsSkuManager;

	@Autowired
	private ITradeSnCreator tradeSnCreator;
	
	@Autowired
	private IExchangeManager exchangeManager;


	public RedisTradeCreator() {

	}

	public void finalize() throws Throwable {

	}
	@Override
	public Trade createTrade( ){
		try{
			//检测
			this.checkTrade();
			return this.innerCreateTrade();
		}catch (Exception e){
			throw new UnProccessableServiceException(e.getMessage());
			
		}finally {
			
		}
	}

	/**
	 * 创建当前的交易
	 */
	private Trade innerCreateTrade( ) {

		CheckoutParam param  = checkoutParamManager.getParam();
		
		Assert.notNull(param.getAddressId(), "必须选择收货地址");
		Assert.notNull(param.getPaymentType(), "必须选择支付方式");
		
		List<CartVo> cartList  = this.cartReadManager.getCheckedItems();
		
		Address address =null;
			
		address = this.memberAddressManager.get(param.getAddressId());
		if(address==null){
			throw new RuntimeException("找不到指定的收货地址");
		}
		
		//收货人
		Consignee consignee = new  Consignee();
		consignee.setConsignee_id(address.getAddr_id());
		consignee.setAddress(address.getAddr());
		
		consignee.setCounty("中国");
		consignee.setProvince(address.getProvince());
		consignee.setCity(address.getCity());
		consignee.setDistrict(address.getRegion());
		consignee.setTown(address.getTown());
		
		consignee.setProvince_id(address.getProvince_id());
		consignee.setCity_id(address.getCity_id());
		if(address.getTown_id()!=null){
			consignee.setTown_id( address.getTown_id());
		}
		consignee.setDistrict_id(address.getRegion_id());
		
		consignee.setMobile(address.getMobile());
		consignee.setTelephone(address.getTel());
		consignee.setName(address.getName());
		
		
		PriceDetail paymentDetail =tradePriceManager.getTradePrice();
		
		String tradeNo  = tradeSnCreator.generateTradeSn();
		Member member  = UserConext.getCurrentMember();
		Trade trade = new  Trade();
		
		//收货人
		trade.setConsignee(consignee);
		
		//价格
		trade.setPrice_detail(paymentDetail);
		
		//效果编号
		trade.setTrade_sn(tradeNo);
		
		//支付类型
		trade.setPayment_type(param.getPaymentType().value());
		
		//会员信息
		trade.setMember_id(member.getMember_id());
		trade.setMember_name(member.getName());
		
		List<Order> orderList  = new ArrayList<Order>();
		
		
		//订单创建时间
		long create_time = DateUtil.getDateline();
		
		//交易总价格
		double trade_total_price=0d;
		double goods_total_price=0d;
		double freight_total_price=0d;
		double discount_total_price=0d;
		
		//生成订单
		for (Cart cart : cartList) {
			
			//生成订单编号
			String order_sn  = tradeSnCreator.generateOrderSn();
			
			//购物信息
			Order order  = new Order(cart);
			
			//创建时间
			order.setCreate_time(create_time);
			
			//购买的会员信息
			order.setMember_id(member.getMember_id());
			order.setMember_name(member.getName());
			
			order.setTrade_sn(tradeNo);
			order.setSn(order_sn);
			
			order.setConsignee(consignee);
			
			//配送方式
			order.setShippingid(cart.getShipping_type_id());
			
			//支付类型 
			order.setPayment_type(param.getPaymentType().value());
			
			//收货时间
			order.setReceive_time(param.getReceive_time());
			
			//发票
			order.setReceipt(param.getReceipt());
			
			//订单备注
			order.setRemark(param.getRemark());
			
			//订单来源
			order.setClient_type(param.getClient_type());
			
			orderList.add(order);
			
			//计算总价，以后应该转移到价格服务
			double total_price  = order.getPrice().getTotal_price();
			double goods_price=order.getPrice().getGoods_price();
			double freight_price=order.getPrice().getFreight_price();
			double discount_price=order.getPrice().getDiscount_price();
			
			trade_total_price= CurrencyUtil.add(total_price, trade_total_price);
			goods_total_price= CurrencyUtil.add(goods_price, goods_total_price);
			freight_total_price= CurrencyUtil.add(freight_price, freight_total_price);
			discount_price= CurrencyUtil.add(discount_price, freight_total_price);
			
		}
		
		PriceDetail tradePrice  = new  PriceDetail();
		tradePrice.setDiscount_price(discount_total_price);
		tradePrice.setFreight_price(freight_total_price);
		tradePrice.setGoods_price(goods_total_price);
		tradePrice.setTotal_price(trade_total_price);
		
		trade.setPrice_detail(tradePrice);
		trade.setOrderList(orderList);
		

		//压入缓存
		String cacheKey = this.getCacheKey(tradeNo);
		this.cache.put(cacheKey, trade);
		
		//将cacheKey发送mq
		amqpTemplate.convertAndSend(AmqpExchange.ORDER_CREATE.name(),"order-create-routingkey", cacheKey);
		
		//清除已购买的商品购物车数据
		this.cartWriteManager.cleanChecked();
		
		//清除价格
		tradePriceManager.cleanPrice();
		
		//清空备注信息
		this.checkoutParamManager.setRemark("");
		
		return trade;
	}

	/**
	 * 根据会话id获取缓存的key<br>
	 * 如果会员没登陆使用会话id做为key<br>
	 * 如果会员登录了，用会员id做为key<br>
	 * @param tradeSn
	 * 		  会话id
	 * @return 缓存key
	 */
	private String getCacheKey( String tradeSn ){
		
		String cacheKey = OrderServiceConstant.TRADE_SESSION_ID_PREFIX+ tradeSn;
		
		return cacheKey;
	}





	
	/**
	 * 下单前的检测<br>
	 * 检测的内容：（以下有一条出现问题，则不予许下单）<br>
	 * 1、检测商品的库存是否超卖
	 * 2、检测商品参与的活动是否已经超期
	 * 3、检测价格：重新计算一遍价格是否和redis中存储的价格相同。
	 * @return 返回true则为允许下单，false则为检测有问题。
	 */
	private boolean checkTrade(){
		
		Member member = UserConext.getCurrentMember();
		if(member == null){
			throw new RuntimeException("您的会话已经失效，请重新登陆再创建订单");
		}
		
		List<CartVo> cartList  = this.cartReadManager.getCheckedItems();
		
		boolean flag = true;
		
		for (CartVo cartVo : cartList) {
			
		 	List<Product> productList =	cartVo.getProductList();
		 	
		 	for (Product product : productList) {
		 		
		 		Integer product_id = product.getProduct_id();
		 		
		 		GoodsSkuVo sku  = this.goodsSkuManager.getSkuFromCache(product_id);
		 		
		 		if(sku == null){
		 			throw new UnProccessableServiceException("商品【"+product.getName()+"】发生变动，请移除后重新加入购物车");
		 		}
		 		
		 		//判断商品的上下架状态
		 		if(sku.getMarket_enable()!=null && sku.getMarket_enable().intValue()!=1){
		 			throw new RuntimeException("商品["+product.getName()+"]已下架。");
		 		}
		 		
		 		//判断商品的上下架状态
		 		if(sku.getDisabled()!=null && sku.getDisabled().intValue()!=0){
		 			throw new RuntimeException("商品["+product.getName()+"]已被删除。");
		 		}
		 		
		 		//读取此产品的可用库存数量
		 		int enableQuantity = sku.getEnable_quantity();
		 		//此产品将要购买的数量
		 		int num = product.getNum();
		 		
		 		//如果将要购买的产品数量大于redis中的数量，则此产品不能下单
		 		if(num>enableQuantity){
		 			flag = false;
		 			throw new RuntimeException("商品["+product.getName()+"]库存不足");
		 		}
		 		
		 		//此商品参与的单品活动
		 		List<CartPromotionGoodsVo> singlePromotionList = product.getSingle_list();
		 		if(!singlePromotionList.isEmpty()){
		 			for (CartPromotionGoodsVo cartPromotionGoodsVo : singlePromotionList) {
		 				
		 				// 默认参与的活动 && 非不参与活动的状态
		 				if(cartPromotionGoodsVo.getIs_check().intValue()==1 && !cartPromotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.NO.getType())){
		 					//当前活动的失效时间
							long entTime = cartPromotionGoodsVo.getEnd_time();
							
							//当前时间
							long currTime = DateUtil.getDateline();
							
							//如果当前时间大于失效时间，则此活动已经失效了，不能下单
							if(currTime>entTime){
								flag = false;
								throw new RuntimeException(product.getName()+"参与的["+cartPromotionGoodsVo.getTitle()+"]活动已失效了。请返回购物车修改");
							}
		 				}
		 				
					}
		 		}
		 		
		 		//此商品参与的组合活动
		 		List<CartPromotionGoodsVo> groupPromotionList = product.getGroup_list();
		 		if(!groupPromotionList.isEmpty()){
		 			for (CartPromotionGoodsVo cartPromotionGoodsVo : groupPromotionList) {
		 				//当前活动的失效时间
						long entTime = cartPromotionGoodsVo.getEnd_time();
						
						//当前时间
						long currTime = DateUtil.getDateline();
						
						//如果当前时间大于失效时间，则此活动已经失效了，不能下单
						if(currTime>entTime){
							flag = false;
							throw new RuntimeException("商品【"+product.getName()+"】参与的【"+cartPromotionGoodsVo.getTitle()+"】活动已失效了。请返回购物车修改");
						}
					}
		 		}
			}
		 	
		}
		
		//读取订单的总交易价格信息
		PriceDetail detail = this.tradePriceManager.getTradePrice();
		
		//此交易需要扣除用户的积分
		int point = detail.getExchange_point();
		
		//读取用户可使用的消费积分
		int mp = member.getMp();
		
		//如果用户可使用的消费积分小于 交易需要扣除的积分时，则不能下单
		if(mp<point){
			throw new UnProccessableServiceException("您可使用的消费积分不足,请返回购物车修改商品");
		}
		
		//购物车中的商品价格要和商品的价格联动起来
		
		return flag;
	};
	
}