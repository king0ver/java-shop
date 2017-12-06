package com.enation.app.shop.promotion.fulldiscount.plugin;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.bonus.model.StoreBonusType;
import com.enation.app.shop.promotion.bonus.service.IB2b2cBonusManager;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.plugin.IPromotionEvent;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.Coupon;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.cache.ICache;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
/**
 * 满优惠价格计算插件
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 下午3:14:36
 */
@Component
public class FullDiscountPlugin implements IPromotionEvent{

	@Autowired
	private ICache cache;
	
	@Autowired
	private IFullDiscountGiftManager fullDiscountGiftManager; 
	
	@Autowired 
	private IB2b2cBonusManager bonusManager;
	
	@Autowired
	private IFullDiscountManager fullDiscountManager;
	/**
	 * 满优惠促销活动的计算接口
	 * @param cart 传入购物车参加本活动的商品信息
	 */
	@Override
	public void onPriceProcess(Cart cart) {
		List<Product> productList = cart.getProductList();
		/**声明map用于存储促销活动对应参与此活动的商品的总价*/
		Map<Integer,Double> map = new HashMap<>();
		
		/**遍历购物车商品*/
		for(Product product : productList) {
			//如果商品没有选中，则不进行计算
			if(product.getIs_check()==0){
				continue;
			}
			/**取出商品参加的组合活动列表*/
			List<CartPromotionGoodsVo> group_list = product.getGroup_list();
			if(group_list.size()>0) {
				for (CartPromotionGoodsVo cartPromotionGoodsVo : group_list) {
					if(cartPromotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.FULLDISCOUNT.getType())) {
						Integer activity_id = cartPromotionGoodsVo.getActivity_id();
						Double price =  map.get(activity_id)==null? 0 : (Double)map.get(activity_id);
						map.put(activity_id, CurrencyUtil.add(product.getSubtotal(),price));
					}
				}
			}
		}
		
		if(map.size()>0) {
			
			for (Map.Entry<Integer,Double> entry : map.entrySet()) {
				/**声明变量用于计算优惠价格*/
				Double discount_price = 0.00;
				Integer activity_id = entry.getKey();
				Double price = entry.getValue();
				/**从redis获取活动信息*/
				String fullDiscountKey = PromotionServiceConstant.getFullDiscountKey(activity_id);
				FullDiscountVo fullDiscountVo = (FullDiscountVo) cache.get(fullDiscountKey);
				if(fullDiscountVo==null) {
					 fullDiscountVo = fullDiscountManager.getFromDB(activity_id);
				}
				/**判断是否达到优惠条件*/
				if(fullDiscountVo.getFull_money() != null && fullDiscountVo.getFull_money()<=price) {
					/**满减优惠计算*/
					if(fullDiscountVo.getIs_full_minus()!=null && fullDiscountVo.getIs_full_minus()==1 && fullDiscountVo.getMinus_value() != null) {
						discount_price += fullDiscountVo.getMinus_value();
					}
					/**满折优惠计算*/
					if(fullDiscountVo.getIs_discount()!=null && fullDiscountVo.getIs_discount()==1 && fullDiscountVo.getDiscount_value() != null) {
						discount_price += price - CurrencyUtil.mul(price, CurrencyUtil.mul(fullDiscountVo.getDiscount_value(), 0.1));
					}
					/**免运费优惠计算*/
					if(fullDiscountVo.getIs_free_ship()!=null && fullDiscountVo.getIs_free_ship() ==1 ) {
						cart.getPrice().setIs_free_freight(1);
						cart.getPrice().setFreight_price(0);
					}
					/**判断是否赠送赠品*/
					if(fullDiscountVo.getIs_send_gift()!=null && fullDiscountVo.getIs_send_gift()==1 && fullDiscountVo.getGift_id() !=null) {
						FullDiscountGift gift = fullDiscountGiftManager.get(fullDiscountVo.getGift_id());
						cart.getGiftList().add(gift);
					}
					/**判断是否赠品优惠券*/
					if(fullDiscountVo.getIs_send_bonus()!=null && fullDiscountVo.getIs_send_bonus() ==1 && fullDiscountVo.getBonus_id() != null) {
						StoreBonusType storeBonusType = bonusManager.get(fullDiscountVo.getBonus_id());
						Coupon coupon= new Coupon();
						coupon.setAmount(storeBonusType.getMin_goods_amount());
						coupon.setCoupon_id(storeBonusType.getType_id());
						coupon.setSeller_id(storeBonusType.getStore_id());
						coupon.setEnd_time(DateUtil.toString(storeBonusType.getUse_end_date(), "yyyy-MM-dd"));
						cart.getGiftCouponList().add(coupon);
					}
					/**判断是否赠送积分*/
					if(fullDiscountVo.getIs_send_point()!=null&&fullDiscountVo.getIs_send_point()==1 && fullDiscountVo.getPoint_value()!=null) {
						cart.setGiftPoint(cart.getGiftPoint()+fullDiscountVo.getPoint_value());
					}
					cart.getPrice().setDiscount_price(CurrencyUtil.add(cart.getPrice().getDiscount_price(), discount_price));
				}
			}
		}
	}
}
