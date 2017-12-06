package com.enation.app.shop.shop.seller;

import java.util.List;

import com.enation.app.base.core.model.Seller;


/**
 * 商家管理
 * @author Chopper
 * @version v1.0
 * @since v6.4
 * 2017年8月16日 下午2:35:28 
 *
 */
public interface ISellerManager {
	public static final String CURRENT_STORE_MEMBER_KEY="current_seller";
	 
	/**
	 * 获取商家
	 * @param member_id 会员id
	 * @return 商家
	 */
	public Seller getSeller(Integer member_id);
	/**
	 * 获取店铺会员
	 * @param member_name 会员名称
	 * @return 商家
	 */
	public Seller getSeller(String member_name);
	
	/**
	 * 获取当前登录的商家 
	 * @return 商家
	 */
	public Seller getSeller();

	/**
	 * 获取当前店铺的人员
	 * @param 店铺id
	 * @return 店铺管理人员
	 */
	public List getMyStoreMembers(Integer store_id);
	/**
	 * 更新商家
	 * @param seller 商家信息
	 * @return 商家
	 */
	public void updateSeller(Seller seller);
	
	
	/**
	 * 校验是否可以操作当前模版
	 * @param seller_id
	 * @return
	 */
	public boolean verification(Integer seller_id);
	
}
