package com.enation.app.shop.waybill.service;

import java.util.List;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.waybill.model.WayBill;
import com.enation.app.shop.waybill.plugin.WayBillEvent;
import com.enation.framework.database.Page;

/**
 * 
 * 后台电子面单管理接口 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月9日 下午7:18:34
 */
public interface IWayBillManager {

	
	/**
	 * 获取电子面单分页列表
	 * @param page_no
	 * @param page_size
	 * @return
	 */
	public Page List(Integer page_no, Integer page_size);
	
	/**
	 * 设置默认启用的电子面单
	 * @param id 电子面单id
	 */
	public void open(Integer id);
	
	/**
	 * 获取开启的电子面单
	 * @return
	 */
	public WayBill getOpen();
	
	/**
	 * 根据ID获取电子面单
	 * @param id 电子面单id
	 * @return
	 */
	public WayBill getWayBill(Integer id);
	
	/**
	 * 根据插件beanid获取电子面单插件
	 * @param id 电子面单id
	 * @return
	 */
	public WayBillEvent findPlugin(String bill_bean);
	
	
	/**
	 * 根据插件id获取电子面单配置项
	 * @param bill_bean 插件id
	 * @return 返回此插件的配置项list
	 */
	public List<ConfigItem> getPlugin(String bill_bean);
	
	/**
	 * 保存修改电子面单参数
	 * @param param 要修改的电子面单各项参数
	 */
	public void edit(WayBill wayBill);
	
	/**
	 * 根据插件id获取电子面单配置项
	 * @param bill_bean 插件beanid
	 * @return 返回电子面单配置项的string字符串
	 */
	public String getConfig(String bill_bean);
	
	/**
	 * 获取生成电子面单所返回的json
	 * @param orderid 订单id
	 * @return 返回的json结果
	 */
	public String createPrintData(String ordersn,Integer logi_id) throws Exception;
	
}
