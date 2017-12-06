package com.enation.app.shop.waybill.plugin;

import java.util.List;

import com.enation.app.base.core.model.ConfigItem;
/**
 * 电子面单参数借口
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月10日 下午2:29:05
 */
public interface WayBillEvent {

	
	/**
	 * 配置各个电子面单的参数
	 * @return 在页面加载的电子面单参数
	 */
	public List<ConfigItem> definitionConfigItem();
	
	/**
	 * 获取插件ID
	 * @return 
	 */
	public String getPluginId();
	
	/**
	 * 获取电子面单json
	 * @param orderid 订单id
	 * @return
	 */
	public String createPrintData(String ordersn,Integer logi_id) throws Exception;
	
}
