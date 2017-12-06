package com.enation.app.shop.waybill.service.impl;


import java.util.ArrayList;
import java.util.Map;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.waybill.model.WayBill;
import com.enation.app.shop.waybill.service.IWayBillManager;
import com.enation.app.shop.waybill.plugin.WayBillEvent;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;

import net.sf.json.JSONObject;


/**
 * 后台电子面单管理实现类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月14日 上午10:37:51
 */
@Service("wayBillManager")
public class WayBillManager implements IWayBillManager {

	
	
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private List<WayBillEvent> wayBillEventList;
	@Autowired
	private WayBillManager wayBillManager;
	
	
	
	
	@Override
	public Page List(Integer page_no, Integer page_size) {
		String sql = "select * from es_waybill";
		return daoSupport.queryForPage(sql, page_no, page_size);
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type=LogType.SETTING,detail="ID为${id}电子面单设置为默认启用的电子面单")
	public void open(Integer id) {
		this.daoSupport.execute("update es_waybill set is_open=0");
		this.daoSupport.execute("update es_waybill set is_open=1 where id=?", id);
	}
	
	
	@Override
	public WayBill getWayBill(Integer id) {
		String sql ="select * from es_waybill where id=?";
		WayBill wayBill = (WayBill) this.daoSupport.queryForObject(sql, WayBill.class, id);
		return wayBill;
	}


	@Override
	public List<ConfigItem> getPlugin(String bill_bean) {
		List<ConfigItem> plugins = new ArrayList<>();
		// 调起插件
		WayBillEvent plugin = this.findPlugin(bill_bean);
		plugins.addAll(plugin.definitionConfigItem());
		return plugins;
	}

	


	@Override
	public WayBillEvent findPlugin(String bill_bean) {
		for (WayBillEvent plugin : wayBillEventList) {
			if (plugin.getPluginId().equals(bill_bean)) {
				return plugin;
			}
		}
		throw new RuntimeException("没有找到相应的插件[" + bill_bean + "]");
	}


	@Override
	public void edit(WayBill wayBill) {
		this.daoSupport.update("es_waybill", wayBill, "id=" + wayBill.getId());
	}


	@Override
	public String getConfig(String bill_bean) {
		String sql = "select r.bill_config from es_waybill r where r.bill_bean =?";
		return this.daoSupport.queryForString(sql, bill_bean);
	}


	@Override
	public String createPrintData(String ordersn,Integer logi_id) throws Exception {
		WayBill wayBill = this.wayBillManager.getOpen();
		WayBillEvent plugin = this.findPlugin(wayBill.getBill_bean());
		String result = plugin.createPrintData(ordersn,logi_id);
		return result;
	}


	@Override
	public WayBill getOpen() {
		String sql = "select * from es_waybill where is_open=?";
		WayBill wayBill = (WayBill) this.daoSupport.queryForObject(sql, WayBill.class, 1);
		return wayBill;
	}


	
}


	
	

