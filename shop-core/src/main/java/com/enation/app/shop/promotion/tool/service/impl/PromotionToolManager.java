package com.enation.app.shop.promotion.tool.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.member.model.po.Address;
import com.enation.app.shop.member.service.IMemberAddressManager;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.plugin.IPromotionEvent;
import com.enation.app.shop.promotion.tool.service.IPromotionToolManager;
import com.enation.app.shop.shop.setting.model.po.ShipTemplate;
import com.enation.app.shop.shop.setting.service.IShipTemplateManager;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Coupon;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.service.ITradePriceManager;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.util.CurrencyUtil;

/**
 * 促销工具接口实现类
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * @version v1.0 2017年08月18日18:09:38
 */
@Service
public class PromotionToolManager implements IPromotionToolManager {

	@Autowired
	private ITradePriceManager tradePriceManager;
	
	@Autowired
	private IShipTemplateManager shipTemplateManager;
	
	@Autowired
	private ICheckoutParamManager checkoutParamManager ;
	
	@Autowired
	private IMemberAddressManager memberAddressManager;
	/*
	 * (non-Javadoc)
	 * @see
	 * com.enation.app.promotion.service.IPromotionToolManager#checkOverlap(java
	 * .lang.Integer[])
	 */
	@Override
	public Integer[] checkOverlap(Integer[] goods_id) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.promotion.tool.service.IPromotionToolManager#
	 * countPrice(java.util.List)
	 */
	@Override
	public void countPrice(List<CartVo> carts) {

		// 循环carts
		for (Cart cart : carts) {
			
			//每次进入活动处理插件之前，先把店铺的价格从新new对象
			//防止在购物车修改数量的时候，进入价格计算插件中，拿到的是上一次修改过的值。
			cart.setPrice(new PriceDetail());
			//清空已使用的优惠券信息
			cart.setCouponList(new ArrayList<Coupon>());
			//初始化赠送优惠券
			cart.setGiftCouponList(new ArrayList<Coupon>());
			//初始化赠送赠品
			cart.setGiftList(new ArrayList<FullDiscountGift>());
			//初始化赠送积分
			cart.setGiftPoint(0);
			
			// 循环购物车单品活动价格计算
			List<String> pluginIdList = PromotionTypeEnum.getSingle();
			for (String pluginId : pluginIdList) {
				IPromotionEvent promotion = SpringContextHolder.getBean(pluginId);
				promotion.onPriceProcess(cart);
			}
		}
		
		for (Cart cart : carts) {
			// 循环购物车组合活动价格计算
			List<String> pluginIdList = PromotionTypeEnum.getGroup();
			for (String pluginId : pluginIdList) {
				IPromotionEvent promotion = SpringContextHolder.getBean(pluginId);
				promotion.onPriceProcess(cart);
			}
		}
		
		//统计价格
		countTotalPrice(carts);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.tool.service.IPromotionToolManager#countCouponPrice(com.enation.app.shop.trade.model.vo.Coupon, java.lang.Integer)
	 */
	@Override
	public void countCouponPrice(CartVo cart,Coupon coupon,Integer is_use) {
		
		//所有店铺的总交易金额
		PriceDetail price = this.tradePriceManager.getTradePrice();
//		//当前店铺的交易金额
		PriceDetail curCartPrice = cart.getPrice();
		
		List<Coupon> couponList = cart.getCouponList();
		
		//切换优惠券之前 已使用的优惠券的金额
		double before_coupon_price = 0.0;
		
		//将要使用的优惠券金额
		double coupon_price = 0.0;
		
		//如果是使用优惠券
		if(is_use==1){
			
			//如果是空则为使用优惠券
			if(couponList.isEmpty()){
				couponList.add(coupon);
				
			}else{	//切换优惠券
				before_coupon_price = couponList.get(0).getAmount();
				couponList.clear();
				couponList.add(coupon);
			}
			coupon_price = coupon.getAmount();
			
		}else{
			
			//如果是空则为使用优惠券
			if(couponList.isEmpty()){
				before_coupon_price = 0;
			}else{
				before_coupon_price = couponList.get(0).getAmount();
				//取消使用，清空优惠券
				couponList.clear();
			}
			
		}
		
		//总优惠金额
		double discount_price =  price.getDiscount_price();
		
		//总优惠金额 = 总优惠金额 - 之前使用的优惠券金额 + 将要使用的优惠券金额
		discount_price = CurrencyUtil.add(CurrencyUtil.sub(discount_price, before_coupon_price), coupon_price);
		
		price.setDiscount_price(discount_price);

		price.countPrice();
		
		curCartPrice.setDiscount_price(CurrencyUtil.add(CurrencyUtil.sub(curCartPrice.getDiscount_price(), before_coupon_price), coupon_price));
		curCartPrice.countPrice();
		// 写入redis 购物车价格 包含
		this.tradePriceManager.pushPrice(price);
	}
	

	/**
	 * 计算价格,将优惠等等信息汇总，存入redis
	 * @param carts
	 */
	private void countTotalPrice(List<CartVo> carts) {

		PriceDetail price = new PriceDetail();

		//遍历所有我的购物车中的所有商家
		for (Cart cart : carts) {
			
			//店铺在活动插件处理后的价格
			PriceDetail cart_price = cart.getPrice();
			
			//店铺总商品重量
			Double cartWeight = 0.0;
			
			//遍历所有商家包含的商品
			for (Product product : cart.getProductList()) {
				
				//如果商品为选中，则直接跳过不进行计算
				if(product.getIs_check()==0){
					continue;
				}
				
				//单个商品原价小计
				Double original_total_price = CurrencyUtil.mul(product.getNum(), product.getOriginal_price());
				
				//店铺商品总价累计
				cart_price.setGoods_price(CurrencyUtil.add(cart_price.getGoods_price(), original_total_price));
				
				Double weight = CurrencyUtil.mul(product.getGoods_weight(), product.getNum());
				
				cartWeight = CurrencyUtil.add(cartWeight, weight);
			}
			
			//设置店铺总商品重量
			cart.setWeight(cartWeight);
			
			//调用价格计算
			cart_price.countPrice();
			
			cart.setPrice(cart_price);
			
			// 累计每个购物车的 优惠金额、商品金额、积分等信息
			price = price.plus(cart_price);
			
		}
		
		// 写入redis 购物车价格 包含
		this.tradePriceManager.pushPrice(price);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.tool.service.IPromotionToolManager#countShipingPrice(java.util.List)
	 */
	@Override
	public void countShipingPrice(List<CartVo> cartList) {
		//所有店铺的总交易金额
		PriceDetail price = this.tradePriceManager.getTradePrice();
		
		double total_freight_price = 0;
		for (CartVo cartVo : cartList) {
			if(cartVo.getPrice().getIs_free_freight()!=1){
				total_freight_price = CurrencyUtil.add(cartVo.getPrice().getFreight_price(),total_freight_price);
				cartVo.getPrice().countPrice();
			}
		}
		
		price.setFreight_price(total_freight_price);
		price.countPrice();
		
		// 写入redis 购物车价格 包含
		this.tradePriceManager.pushPrice(price);
	} 
	

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.tool.service.IPromotionToolManager#countShippingPrice(java.util.List)
	 */
	@Override
	public void setShippingPrice(List<CartVo> cartList) {
		
		for (CartVo cartVo : cartList) {
			
			Integer seller_id = cartVo.getSeller_id();
			Integer weight = cartVo.getWeight().intValue();
			
			//总件数
			Integer piece = 0;
			
			int is_free_freight = 0;
			
			//遍历此店铺所有的商品，如果有一个商品免运费，则整个店铺免运费
			for (Product product : cartVo.getProductList()) {
				if(product.getIs_free_freight()==1){
					is_free_freight=1;
					break;
				}
				piece=piece+product.getNum();
			}
			
			//设置免运费
			if(is_free_freight==1){
				cartVo.setShipping_type_id(0);
				cartVo.setShipping_type_name("免运费"); 
				cartVo.getPrice().setFreight_price(0.00);
				break;
			}
			
			Integer area_id = null;
			Address address = memberAddressManager.get(checkoutParamManager.getParam().getAddressId());
			if(address.getTown_id()==null) {
				area_id=address.getRegion_id();
			}
			
			List<ShipTemplate> templates = shipTemplateManager.getStoreTemplate(seller_id, area_id);
			// 如果没有运费模版，则免运费
			if (templates.size() == 0) {
				cartVo.setShipping_type_id(0);
				cartVo.setShipping_type_name("免运费"); 
				cartVo.getPrice().setFreight_price(0.00);
				break;
			}
			double[] ship_prices = new double[templates.size()];
			String[] ship_name = new String[templates.size()];
			int[] ship_id = new int[templates.size()];

			int index = 0;
			for (ShipTemplate template : templates) {
				double shipprice = template.getFirst_price();
				// 计算模式 1 重量算运费 2 计件算运费
				if (template.getType() == 1) {
					
					//商品总重量是否大于首重
					if (weight <= template.getFirst_company()) {
						ship_prices[index] = shipprice;
					} else {
						//(商品总重量 - 首重) / 续重 = 续重的倍数
						int count = (weight - template.getFirst_company()) / template.getContinued_company();
						
						//取余如果不等于0 则续重倍数+1
						if (weight % template.getContinued_company() != 0) {
							count += 1;
						}
						//运费 = 首重价格+续重倍数*续重费用
						shipprice += count * template.getContinued_price();
						ship_prices[index] = shipprice;
					}
				} else {
					if (piece <= template.getFirst_company()) {
						ship_prices[index] = shipprice;
					} else {
						int count = (piece - template.getFirst_company()) / template.getContinued_company();
						if (weight % template.getFirst_company() != 0) {
							count += 1;
						}
						shipprice += count * template.getContinued_price();
						ship_prices[index] = shipprice;
					}
				}
				ship_name[index] = template.getName();
				ship_id[index] = template.getTemplate_id();
				index++;
			}

			double final_ship = 9999d;	//默认一个运费金额，下面的循环中计算出金额最少的运费方式
			String ship_type_name = "默认运费";
			int ship_type_id = 0;
			
			for (int i = 0; i < ship_prices.length; i++) {
				if (ship_prices[i] < final_ship) {
					final_ship = ship_prices[i];
					ship_type_name = ship_name[i];
					ship_type_id = ship_id[i];
				}
			}
			//如果商家没有配置配送方式则将运费设置为0
			if(ship_prices.length==0){
				final_ship=0;
			}
			// 根据当前店铺设置配送方式
			cartVo.setShipping_type_id(ship_type_id);
			cartVo.setShipping_type_name(ship_type_name);

			//设置配送金额
			cartVo.getPrice().setFreight_price(final_ship);
		} 
	}

	
}
