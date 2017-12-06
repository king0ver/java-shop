package com.enation.app.shop.shop.rdesign.service;

import java.util.List;

import com.enation.app.shop.shop.rdesign.model.StoreSilde;



/**
 * 订单幻灯片管理类
 * @author LiFenLong
 *
 */
public interface IStoreSildeManager {
	/**
	 * 获取店铺幻灯片列表
	 * @param store_id
	 * @return
	 */
	public List<StoreSilde> list(Integer store_id);
	/**
	 * 修改店铺幻灯片
	 * @param fsImg
	 * @param silde_id
	 * @param silde_url
	 */
	public void edit(Integer[] silde_id,String[] fsImg,String[] silde_url);
	/**
	 * 根据幻灯片id实现删除功能
	 * @param silde_id	幻灯片id
	 */
	public void delete(Integer silde_id);
}
