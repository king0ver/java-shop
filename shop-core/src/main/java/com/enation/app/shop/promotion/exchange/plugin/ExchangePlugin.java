package com.enation.app.shop.promotion.exchange.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.exchange.model.po.Exchange;
import com.enation.app.shop.promotion.exchange.service.IExchangeManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.plugin.IPromotionEvent;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.util.CurrencyUtil;

/**
 * 
 * 计算购物车积分换购商品的价格
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月23日 上午10:48:49
 */
@Component
public class ExchangePlugin implements IPromotionEvent{
	
	@Autowired
	private IExchangeManager exchangeManager;

	/**
	 * 设计思路：从购物车的商品列表中循环商品， 如果有这个商品说明此商品参加了积分换购， 拿到这个商品的换购信息去计算价格积分等信息，
	 * 计算价格时: 
	 * 1、Cart.productList.Product.point 需要修改。<br>
	 * 2、Cart.price.exchange_point 需要累加。<br>
	 * 3、Cart.price.discount_price 需要累加。<br>
	 * 4、Cart.productList.Product.subtotal 需要计算<br>
	 * 
	 */
	@Override
	public void onPriceProcess(Cart cart) {
		// 获取购物车商品列表
		List<Product> productList = cart.getProductList();
		for (Product product : productList) {
			
			//如果商品没有选中，则不进行计算
			if(product.getIs_check()==0){
				continue;
			}
			
			// 如果当前购物车有这个
			// 得到Product的 已参的单品活动工具列表
			List<CartPromotionGoodsVo> pgvs = product.getSingle_list();
			if (pgvs==null && pgvs.isEmpty()) {
				continue;
			}
			// 循环商品参与的活动
			for (CartPromotionGoodsVo pgv : pgvs) {
				// 赋予活动过价格 ，如果当前商品参与的是积分换购活动
				if (pgv.getPromotion_type().equals(PromotionTypeEnum.EXCHANGE.getType()) && pgv.getIs_check()==1) {
					Exchange exchange = exchangeManager.get(pgv.getActivity_id(), product.getSeller_id());
					// 获取当前这个商品购买的数量
					int num = product.getNum();
					if(exchange!=null) {
						//商品所需兑换的金额(单个商品优惠后价格)
						Double exchange_money = exchange.getExchange_money();
						//商品所需兑换的积分
						int exchange_point = exchange.getExchange_point();
						//商品原价
						Double original_price = product.getOriginal_price();
						//此商品的总优惠后需要的积分(商品所需兑换积分*数量)
						int goodsdiscounPoint= CurrencyUtil.mul(exchange_point, num).intValue();
						//修改商品小计金额
						product.setSubtotal(CurrencyUtil.mul(exchange_money, num));
						//修改购买此商品所消耗的积分
						product.setPoint(Integer.valueOf(CurrencyUtil.mul(exchange_point, num).intValue()));
						//此商品的总优惠金额(商品原价*数量-商品优惠后价格*数量)
						Double goodsdiscounPrice = CurrencyUtil.sub(CurrencyUtil.mul(original_price, num), CurrencyUtil.mul(exchange_money, num));
						//累加店铺优惠金额
						cart.getPrice().setDiscount_price(CurrencyUtil.add(cart.getPrice().getDiscount_price(), goodsdiscounPrice));
						//累加店铺优惠后的所需要的积分
						cart.getPrice().setExchange_point((CurrencyUtil.add(cart.getPrice().getExchange_point(), goodsdiscounPoint)).intValue());
					}
				}
			}

		}

	}

}
