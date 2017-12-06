package com.enation.app.shop.shop.setting.service;

import java.util.List;


import com.enation.app.shop.shop.setting.model.po.StoreLevel;



/**
 * 店铺等级管理类
 * @author LiFenLong
 *
 */
public interface IStoreLevelManager {

	/**
	 * 获取店铺等级列表
	 * @return List
	 */
	public List storeLevelList();
	/**
	 * 添加店铺等级
	 * @param levelName
	 */
	public void addStoreLevel(String levelName);
	/**
	 * 修改店铺等级
	 */
	public void editStoreLevel(Integer levelId,Integer space_capacity);
	/**
	 * 删除店铺等级
	 * @param levelId
	 */
	public void delStoreLevel(Integer levelId);
	/**
	 * 获取一个店铺等级对象
	 * @param levelId
	 */
	public StoreLevel getStoreLevel(Integer levelId);
}
