package com.enation.app.shop.trade.service;

import java.util.List;
import com.enation.app.shop.trade.model.vo.CartVo;

/**
 * 购物车查询操作业务类<br>
 * 包含对购物车查询操作
 * @author xulipeng
 * @version 1.0
 * @since pangu1.0
 * 2017年09月13日17:43:10
 */
public interface ICartReadManager {

	
	/**
	 * 由缓存中取出购物列表<br>
	 * 如果已经登录，会以会员id为key
	 * @return 购物车列表
	 * 
	 * @param sessionid    会话id
	 */
	public List<CartVo> getCartlist();
	
	
	/**
	 * 读取购物车商品数量
	 * @return
	 */
	public Integer count();
	
	/**
	 * 获取某属主的购物车
	 * @param ownerid
	 * @return
	 */
	public CartVo getCart(Integer ownerid);
	
	
	/**
	 * 获取已经选中要结算的购物列表<br> 
	 * 如果已经登录，会以会员id为key
	 * @return
	 */
	public List<CartVo> getCheckedItems();
	
	
	/**
	 * 读取未登录的用户购物车集合<br>
	 * @return
	 */
	public List<CartVo> getCartlistByNoLogin();
	
}
