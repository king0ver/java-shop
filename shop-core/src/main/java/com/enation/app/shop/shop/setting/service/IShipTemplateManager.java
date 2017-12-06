package com.enation.app.shop.shop.setting.service;

import java.util.List;

import com.enation.app.shop.shop.setting.model.po.ShipTemplate; 

/**
 * 运费模版管理类 
 * @author Chopper
 * @version v1.0
 * @since v6.2
 * 2017年4月11日 下午5:35:18 
 *
 */
public interface IShipTemplateManager {
	
	/**
	 * 新增
	 * @param tamplate
	 * @return
	 */
	public ShipTemplate save(ShipTemplate tamplate);
	
	/**
	 * 修改
	 * @param template
	 * @return
	 */
	public ShipTemplate edit(ShipTemplate template);

	/**
	 * 获取商家运送方式
	 * @param seller_id
	 * @return
	 */
	public List<ShipTemplate> getStoreTemplate(Integer seller_id);
	
	/**
	 * 获取地区匹配的运费模版
	 * @param seller_id
	 * @param area_id
	 * @return
	 */
	public List<ShipTemplate> getStoreTemplate(Integer seller_id,Integer area_id);
	

	/**
	 * 获取商家运送方式
	 * @param tamplate_id
	 * @return
	 */
	public ShipTemplate getOne(Integer tamplate_id);
	
	/**
	 * 删除
	 * @param template_id
	 */
	public void delete(Integer template_id);
	
}
