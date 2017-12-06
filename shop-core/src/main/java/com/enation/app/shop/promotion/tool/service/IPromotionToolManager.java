package com.enation.app.shop.promotion.tool.service;

import java.util.List;

import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Coupon;

/**
 * 促销工具接口
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月18日18:06:40
 */
public interface IPromotionToolManager {

	/**
	 * 检测商品之前参与的活动和将要参加的活动是否可以重叠使用，和权重是否有冲突，如果有问题则不能添加商品到活动中。
	 * @param goods_id
	 * @return 不能添加的商品id集合
	 */
	public Integer[] checkOverlap(Integer[] goods_id);
	
	/**
	 * <h3>计算价格</h3>
	 * 使用场景:<br>
	 * 1、此接口的调用为商品加入购物车时，调用此接口。
	 * <br>计算过程：<br>
	 * 1、根据商品参与的促销活动，并计算价格。
	 * 2、根据以上1、计算的价格，计算此购物车的价格。
	 * <br>注意事项:<br>
	 * 1、此接口的价格计算不包含优惠券，配送费用。
	 * @param cart
	 */
	public void countPrice(List<CartVo> cartList);
	
	/**
	 * <h3>计算优惠券价格</h3>
	 * 使用场景：
	 * 1、订单结算页，用户点击使用优惠券时调用
	 * <br>计算过程：<br>
	 * 1、计算当前店铺使用优惠券后的价格。
	 * 2、计算总订单交易金额
	 * <br>注意事项:<br>
	 * 1、此接口只计算当前商家的，使用的优惠券的价格。
	 * @param cartList	购物车列表
	 * @param coupon	将要使用的优惠券
	 * @param is_use  1为：要使用，0为：取消使用
	 */
	public void countCouponPrice(CartVo cart,Coupon coupon,Integer is_use);
	
	
	/**
	 * <h3>计算配送费用</h3>
	 * 1、读取每个店铺的配送费用
	 * 2、累加配送费用
	 * @param cartList
	 */
	public void countShipingPrice(List<CartVo> cartList);
	
	
	/**
	 * 设置配送方式价格
	 * 设置每个cartvo的默认配送方式，及配送费用
	 * @param cartList
	 */
	public void setShippingPrice(List<CartVo> cartList);

}
