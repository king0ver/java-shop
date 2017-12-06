package com.enation.app.shop.shop.rdesign.service;

import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.rdesign.model.WapShopThemes;
import com.enation.framework.database.Page;

/**
 * 
 * (wap店铺模版管理) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月22日 下午11:01:40
 */
public interface IShopWapThemesManager {

	/**
	 * 店铺模板列表
	 * @return
	 */
	public Page list(Integer pageNo,Integer pageSize);
	
	/**
	 * 店铺模板添加
	 * @param wapStoreThemes wap店铺模板
	 * @return
	 */
	public void add(WapShopThemes wapStoreThemes);
	
	/**
	 * 店铺模板修改
	 * @param wapStoreThemes 店铺模板
	 * @return
	 */
	public void edit(WapShopThemes wapStoreThemes);
	
	/**
	 * 删除店铺模板
	 * @param id 店铺模板Id
	 */
	public void delete(Integer id);
	
	/**
	 * 获取店铺模板信息
	 * @param id 店铺模板Id
	 * @return
	 */
	public WapShopThemes getStorethThemes(Integer id);
	
	/**
	 * 根据店铺Id获取模板文件夹
	 * @param storeId 店铺Id
	 * @return 模板文件夹
	 */
	public String getStrorePath(Integer storeId);
	
	/**
	 * 切换当前分销商店铺模板
	 * @param themesId 模板Id
	 */
	public void changeStoreThemes(Integer themes_id);
	
	/**
	 * 获取当前默认模板
	 * @return
	 */
	public WapShopThemes getDefaultStoreThemes();
	
	/**
	 * 通过链接获取店铺
	 * @param url 链接 
	 * @return
	 */
	public Shop getStoreByUrl(String url);
	/**
	 * 通过链接获取店铺Id
	 * @param url 链接 
	 * @return
	 */
	public Integer getStoreIdByUrl(String url);
	
}
