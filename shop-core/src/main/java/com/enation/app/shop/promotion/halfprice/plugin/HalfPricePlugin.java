package com.enation.app.shop.promotion.halfprice.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.halfprice.service.impl.HalfPriceManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.plugin.IPromotionEvent;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.util.CurrencyUtil;

/**
 * 第二件半价促销活动价格计算插件
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月22日 下午2:27:39
 */
@SuppressWarnings("unchecked")
@Component
public class HalfPricePlugin implements IPromotionEvent {

	@Autowired
	private HalfPriceManager halfPriceManager;
	/**
	 * 第二件半价促销活动的计算接口
	 * 
	 * @param cart 传入购物车参加本活动的商品信息
	 * 1.循环购物车中的商品
	 * 2.循环判断购物车中的商品是否参加第二件半价活动，判断是否参加
	 * 3.然后判断商品是否超过2件，如果超过或等于就计算出要优惠的半价金额，追加到优惠金额中
	 * 4.然后将小计金额减去参加这次活动所要减去的金额
	 */
	@Override
	public void onPriceProcess(Cart cart) {
		List<Product> productList = cart.getProductList();
		for(Product product : productList) {
			
			//如果商品没有选中，则不进行计算
			if(product.getIs_check()==0){
				continue;
			}
			
			/** 单个商品成交价 */
			Double purchase_price = product.getOriginal_price();
			/** 商品数量 */
			Integer num = product.getNum();
			/** 商品小计 */
			Double subTotal = CurrencyUtil.mul(purchase_price, num);
			/** 参加活动要优惠的价格 */
			Double preferentialPrice = CurrencyUtil.div(purchase_price, 2);
			
			/** 判断该购物车商品是否是俩件 */
			if(num.intValue() > 1) {
				/** 获取该商品参加的单品活动列表 */
				List<CartPromotionGoodsVo> activity_list = product.getSingle_list();
				/** 判断有没有活动 */
				if(activity_list == null) {
					return;
				}
				/** 循环判断有没有第二件半价活动 */
				for(CartPromotionGoodsVo cartPromotionGoodsVo:activity_list) {
					if(cartPromotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.HALFPRICE.getType()) && cartPromotionGoodsVo.getIs_check()==1 ) {
						/** 将优惠价格追加到原优惠价格当中 */
						cart.getPrice().setDiscount_price(CurrencyUtil.add(cart.getPrice().getDiscount_price(), preferentialPrice));
						/** 将小计金额减去参加这次活动所要优惠的金额 */
						product.setSubtotal(CurrencyUtil.sub(subTotal, preferentialPrice));
					}
				}
			}
		}	
	}

}

