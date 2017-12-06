package com.enation.app.shop.order.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.AmqpExchange;
import com.enation.app.shop.member.service.IMemberPointManger;
import com.enation.app.shop.order.manager.IProductIntoDBManager;
import com.enation.app.shop.order.manager.ITradeIntoDBManager;
import com.enation.app.shop.order.support.TradeServiceConstant;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.trade.model.enums.OrderPermission;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.po.OrderLog;
import com.enation.app.shop.trade.model.po.OrderMeta;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.po.TradePo;
import com.enation.app.shop.trade.model.vo.Coupon;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.OrderLine;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.model.vo.Trade;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonUtil;
import com.google.gson.Gson;

/**
 * 交易入库业务类
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月23日下午5:21:12
 */
@Service
public class TradeIntoDBManager implements ITradeIntoDBManager {

	@Autowired
	private ICache cache;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IProductIntoDBManager productIntoDBManager;
	
	@Autowired
	private ICheckoutParamManager checkoutParamManager;
	
	@Autowired
	private IMemberPointManger memberPointManger;
	
	@Autowired
	private IOrderOperateManager orderOperateManager;
	

	/*
	 * (non-Javadoc)
	 * @see com.enation.javashop.consumer.shop.order.manager.ITradeIntoDBManager#intoDB(java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void intoDB(String cacheKey) {
		
		try {
			//入库操作
			this.innerIntoDB(cacheKey);
		} catch (Exception e) {
			e.printStackTrace();
			//任何情况出现的失败，将此key的数据转移到另一个key下面
			Trade trade = (Trade) cache.get(cacheKey);
			List<Trade> list = (List<Trade>) cache.get(TradeServiceConstant.TRADE_FAIL_CACHE_KEY);
			if(list==null){
				list = new ArrayList<>();
			}
			list.add(trade);
			cache.put(TradeServiceConstant.TRADE_FAIL_CACHE_KEY, list);
			//清除交易缓存数据
			this.cache.remove(cacheKey);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void innerIntoDB(String cacheKey) {

		Trade trade = (Trade) cache.get(cacheKey);

		if (trade == null) {
			throw new RuntimeException("交易无法入库，原因：trade为空");
		}

		// 交易入库
		TradePo tradePo = new TradePo(trade);
		long create_time = DateUtil.getDateline();
		tradePo.setCreate_time(create_time);
		List<OrderLine> orderLineList = new ArrayList<OrderLine>();

		// 订单入库
		List<Order> orderList = trade.getOrderList();
		for (Order order : orderList) {
			OrderPo orderPo = new OrderPo(order);
			orderPo.setTrade_sn(trade.getTrade_sn());
			orderPo.setOrder_status(OrderStatus.CONFIRM.value());

			// 为order vo 赋默认值，这些值会在orderline中使用
			order.setOrder_status(orderPo.getOrder_status());
			order.setPay_status(orderPo.getPay_status());
			order.setShip_status(orderPo.getShip_status());
			order.setComment_status(orderPo.getComment_status());
			order.setService_status(orderPo.getService_status());
			OrderLine orderLine = new OrderLine(order);
			orderLineList.add(orderLine);
			this.daoSupport.insert("es_order", orderPo);
			int order_id = this.daoSupport.getLastId("es_order");
			//记录下单日志
			this.log(orderPo.getSn(), "创建订单", "会员："+orderPo.getMember_name());
			orderPo.setOrder_id(order_id);
			
			//使用的积分
			int Consume_point = order.getPrice().getExchange_point();
			
			//扣除购买商品需要的积分
			if(Consume_point>0) {
				this.memberPointManger.useMarketPoint(orderPo.getMember_id(), Consume_point, "购买商品，扣除消费积分", null);
			}
			//使用积分的记录
			OrderMeta pointMeta = new OrderMeta();
			pointMeta.setMeta_key("point");
			pointMeta.setMeta_value(Consume_point+"");
			pointMeta.setOrder_sn(orderPo.getSn());
			pointMeta.setStatus(ServiceStatus.NOT_APPLY.name());
			this.daoSupport.insert("es_order_meta", pointMeta);
			
			//赠送积分的记录
			OrderMeta giftPointMeta = new OrderMeta();
			giftPointMeta.setMeta_key("giftPoint");
			giftPointMeta.setMeta_value(order.getGiftPoint()+"");
			giftPointMeta.setOrder_sn(orderPo.getSn());
			giftPointMeta.setStatus(ServiceStatus.NOT_APPLY.name());
			this.daoSupport.insert("es_order_meta", giftPointMeta);
			
			//赠优惠券入库
			List<Coupon> couponList = order.getGiftCouponList();
			OrderMeta couponMeta = new OrderMeta();
			couponMeta.setMeta_key("coupon");
			couponMeta.setMeta_value(JsonUtil.ListToJson(couponList));
			couponMeta.setOrder_sn(orderPo.getSn());
			couponMeta.setStatus(ServiceStatus.NOT_APPLY.name());
			this.daoSupport.insert("es_order_meta", couponMeta);
			
			//赠品入库
			List<FullDiscountGift> giftList = order.getGiftList();
			OrderMeta giftMeta = new OrderMeta();
			giftMeta.setMeta_key("gift");
			giftMeta.setMeta_value(JsonUtil.ListToJson(giftList));
			giftMeta.setOrder_sn(orderPo.getSn());
			giftMeta.setStatus(ServiceStatus.NOT_APPLY.name());
			this.daoSupport.insert("es_order_meta", giftMeta);
			
			//将使用过的优惠券变为已使用
			List<Coupon> useCoupons = order.getCouponList();
			if(useCoupons!=null){
				for(Coupon c:useCoupons){
					this.daoSupport.execute("update es_member_bonus set used = 1 where bonus_id = ?", c.getCoupon_id());
				}
			}
			
			OrderStatusChangeMessage message = new OrderStatusChangeMessage();
			message.setOrder(orderPo);
			message.setOldStatus(OrderStatus.valueOf(order.getOrder_status()));
			
			// 产品入库
			List<Product> productList = order.getProductList();
			this.productIntoDBManager.productIntoDB(order, productList);
			
			//如果订单入库失败，则发送订单出库失败消息
			if(order.getOrder_status().equals(OrderStatus.INTODB_ERROR.value())){
				message.setNewStatus(OrderStatus.INTODB_ERROR);
				amqpTemplate.convertAndSend(AmqpExchange.ORDER_INTODB_ERROR.name(),"order-IntoDB-error-routingkey", message);
				//修改订单的状态
				this.daoSupport.execute("update es_order set order_status=? where sn=?", OrderStatus.INTODB_ERROR.value(),orderPo.getSn());
				
			}else{
				message.setNewStatus(OrderStatus.CONFIRM);
				amqpTemplate.convertAndSend(AmqpExchange.ORDER_STATUS_CHANGE.name(),"order-create-success-routingkey", message);
				
				//如果订单的金额为0元，则系统自动为此订单付款
				if(order.getPrice().getTotal_price()==0 && order.getPayment_type().equals(PaymentType.online.name()) ){
					this.orderOperateManager.payOrder(order.getSn(), 0d, OrderPermission.client);
				}
				
			}
		}
		
		// 将OrderLine放到 order_json字段中
		Gson gson = new Gson();
		tradePo.setOrder_json(gson.toJson(orderLineList));
		this.daoSupport.insert("es_trade", tradePo);
		// 入库成功，清除已购买的商品购物车缓存数据
		this.cache.remove(cacheKey);

	}

	/**
	 * 记录订单日志
	 * 
	 * @param ordersn
	 *            订单编号
	 * @param message
	 *            操作信息
	 * @param op_name
	 *            操作者
	 */
	private void log(String ordersn, String message, String op_name) {
		OrderLog orderLog = new OrderLog();
		orderLog.setMessage(message);
		orderLog.setOp_name(op_name);
		orderLog.setOrder_sn(ordersn);
		orderLog.setOp_time(DateUtil.getDateline());
		this.daoSupport.insert("es_order_log", orderLog);
	}
}
