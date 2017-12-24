package com.enation.app.shop.shop.rdesign.service;

import java.util.List;

import com.enation.app.shop.shop.rdesign.model.StorePhotos;


/**
 * 店铺相册管理类
 * @author LiFenLong
 *
 */
public interface IStorePhotosManager {
	/**
	 * 获取店铺相册列表
	 * @param store_id
	 * @return
	 */
	public List<StorePhotos> list(Integer store_id);
	
	/**
	 * 添加图片
	 * @param store_id 店铺id
	 * @param img 图片地址
	 */
	public void add(Integer store_id,String img);
	/**
	 * 删除图片
	 * @param store_id 店铺id
	 * @param photo_id 图片id
	 */
	public void delete(Integer store_id,Integer photo_id);
	/**
	 * 获取店铺相册图片
	 * @param store_id 店铺id
	 * @param photo_id 图片id
	 * @return
	 */
	public StorePhotos getStorePhotos(Integer store_id,Integer photo_id);
}
