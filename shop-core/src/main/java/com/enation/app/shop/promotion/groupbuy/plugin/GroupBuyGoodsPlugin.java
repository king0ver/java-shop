package com.enation.app.shop.promotion.groupbuy.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.groupbuy.model.vo.GroupBuyVo;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.plugin.IPromotionEvent;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.cache.ICache;
import com.enation.framework.util.CurrencyUtil;

/**
 * 
 * @ClassName: GroupBuyGoodsPlugin
 * @Description: 团购商品插件
 * @author TALON
 * @date 2015-7-31 上午10:44:09
 *
 */
@Component
public class GroupBuyGoodsPlugin implements IPromotionEvent {

	@Autowired
	private ICache cache;
	
	@Autowired
	private IGroupBuyManager groupBuyManager;

	PromotionServiceConstant constant = new PromotionServiceConstant();

	@Override
	public void onPriceProcess(Cart cart) {
		List<Product> productList = cart.getProductList();
		// 循环购物车商品
		for (Product product : productList) {
			//如果商品没有选中，则不进行计算
			if(product.getIs_check()==0){
				continue;
			}
			List<CartPromotionGoodsVo> pgvs = product.getSingle_list();
			if (pgvs != null && pgvs.size() == 0) {
				continue;
			}
			// 循环商品参与的活动
			for (CartPromotionGoodsVo pgv : pgvs) {
				// 赋予活动过价格
				if (pgv.getPromotion_type().equals(PromotionTypeEnum.GROUPBUY.getType()) && pgv.getIs_check()==1) {
					GroupBuyVo gbv = (GroupBuyVo) cache
							.get(constant.getGroupbuyKey(cart.getSeller_id(), pgv.getActivity_id()));
					if(gbv == null) {
						 gbv = groupBuyManager.getVo(pgv.getActivity_id());
					}
					//写入成交价格
					product.setPurchase_price(gbv.getPrice());
					
					//商品原价
					Double original_price = product.getOriginal_price();
					
					//商品优惠后的单价
					Double purchase_price = gbv.getPrice();
					
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


					// 小记 数量*商品成交价格
					product.setSubtotal(CurrencyUtil.mul(product.getNum(), product.getPurchase_price()));
					
				}
				
			} 

		}
	}

}
