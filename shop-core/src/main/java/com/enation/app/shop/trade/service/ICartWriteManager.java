package com.enation.app.shop.trade.service;

import java.util.List;

import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Product;

/**
 * 购物车写入操作业务类<br>
 * 包含对购物车添加，修改，删除操作
 * @author xulipeng
 * @version 1.0
 * @since pangu1.0
 * 2017年09月13日17:43:10
 */
public interface ICartWriteManager {

	/**
	 * 将一个产品加入购物车
	 * @param goodsid 	商品id
	 * @param productid    货品id
	 * @param num		购物数量
	 * @param activity_id  默认参与的活动id
	 */
	public Product add(Integer productid,Integer num,Integer activity_id);
	
	
	/**
	 * 更新数量
	 * @param goodsid    商品id
	 * @param productid    货品id
	 * @param num    要更新的数量
	 */
	public Product updateNum(Integer productid, Integer num);
	
	
	/**
	 * 更新一个货品的选中状态	 *
	 * @param sessionid    会话id
	 * @param productid    货品id
	 * @param checked    选中状态1为选中,0为未选中
	 */
	public Product checked(Integer[] productid, int checked);
	
	
	/**
	 * 更新一个店铺的选中状态
	 * @param sellerid
	 * @param checked
	 * @return
	 */
	public Cart checkedSellerAll( Integer sellerid ,int checked);
	
	/**
	 * 更新所有的货品的选中状态	 *
	 * 
	 * @param sessionid    会话id
	 * @param checked    选中状态1为选中,0为未选中
	 */
	public void checkedAll(int checked);
	
	
	/**
	 * 批量删除购物车中的商品
	 * @param sessionid 会话id 
	 * @param productids 要删除的产品id数组
	 */
	public void delete(Integer[] productids);
	
	
	/**
	 * 清空我的购物车
	 */
	public void clean();
	
	/**
	 * 清空已创建订单的购物车商品数据<br>
	 * 当用户下单时只勾选了某几个商品去购买，应清楚购物车中这几个已购买的商品
	 */
	public void cleanChecked();

	
	/**
	 * 设置优惠券的时候分为三种情况：前2种情况couponid 不为0,不为空。第3种情况couponid为0<br>
	 * 1、使用优惠券:在刚进入订单结算页，为使用任何优惠券之前。<br>
	 * 2、切换优惠券:在1、情况之后，当用切换优惠券的时候。<br>
	 * 3、取消已使用的优惠券:用户不想使用优惠券的时候。<br>
	 * @param couponid
	 * @param sellerid
	 */
	public Cart userCoupon(int couponid,int sellerid);
	
	
	/**
	 * 压入redis
	 * @param itemList
	 */
	public void putCache(String cacheKey,List<CartVo> itemList);
	
	
	/**
	 * 设置促销活动
	 * @param sellerid
	 * @param skuid
	 * @param activity_id
	 * @param promotion_type
	 */
	public void setPromotion(Integer sellerid,Integer skuid,Integer activity_id,String promotion_type);
	
	/**
	 * 设置默认配送方式
	 */
	public void setShipping();
	
	/**
	 * 刷新购物车，从新计算价格
	 */
	public void refreshCart();
	
	/**
	 * 合并购物车<br>
	 * 用户未登录的时候，将商品加入购物车，当用户登录之后应把未登录时的购物车中的商品合并登录之后的购物车中。
	 */
	public void mergeCart();
	
	
}
