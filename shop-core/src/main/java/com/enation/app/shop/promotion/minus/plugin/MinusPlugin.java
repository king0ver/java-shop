package com.enation.app.shop.promotion.minus.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.minus.model.vo.MinusVo;
import com.enation.app.shop.promotion.minus.service.IMinusManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.plugin.IPromotionEvent;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.util.CurrencyUtil;

/**
 * 单品立减插件
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月18日下午9:15:00
 *
 */
@Component
public class MinusPlugin implements IPromotionEvent {

	@Autowired
	private IMinusManager minusManager;

	/**
	 * 单品立减活动，计算插件
	 */
	@Override
	public void onPriceProcess(Cart cart) {

		PromotionServiceConstant constant = new PromotionServiceConstant();

		List<Product> products = cart.getProductList();
		// 循环购物车商品
		for (Product product : products) {
			
			//如果商品没有选中，则不进行计算
			if(product.getIs_check()==0){
				continue;
			}
			
			List<CartPromotionGoodsVo> cartPromotionGoodsVoList = product.getSingle_list();
			if (cartPromotionGoodsVoList == null) {
				continue;
			}
			// 循环商品参与的活动
			for (CartPromotionGoodsVo goodsVo : cartPromotionGoodsVoList) {
				
				// 如果有商品参与单品立减，则减去活动金额
				if (goodsVo.getPromotion_type().equals(PromotionTypeEnum.MINUS.getType()) && goodsVo.getIs_check()==1) {
					
					// 获取活动实例
					MinusVo minusVo = this.minusManager.get(goodsVo.getActivity_id());
					
					//商品原价
					Double original_price = product.getOriginal_price();
					
					//商品优惠后的单价
					Double purchase_price = CurrencyUtil.sub(product.getOriginal_price(), minusVo.getMinus_price());
					if(purchase_price.intValue()<0){
						purchase_price = 0d;
					}
					
					//商品数量
					int num = product.getNum();
					
					//设置商品的优惠后单价
					product.setPurchase_price(purchase_price);
					
					//设置商品的小计(商品优惠后的单价*数量)
					product.setSubtotal(CurrencyUtil.mul(purchase_price, num));
					
					//此商品的总优惠金额 (商品原价*数量 - 商品优惠后价格*数量)
					Double goodsDiscountPrice = CurrencyUtil.sub(CurrencyUtil.mul(original_price, num), CurrencyUtil.mul(purchase_price, num));

					//累加店铺优惠金额
					cart.getPrice().setDiscount_price(CurrencyUtil.add(cart.getPrice().getDiscount_price(), goodsDiscountPrice));

				}
			}
		}
	}

}
