package com.enation.app.shop.waybill.controller.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.waybill.model.WayBill;
import com.enation.app.shop.waybill.service.impl.WayBillManager;

import net.sf.json.JSONObject;


/**
 * 商家中心电子面单生成控制器
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月10日 下午4:28:05
 */
@Validated
@RestController
@RequestMapping("/shop/waybill/waybill-api")
public class WayBillApiController {
	

	@Autowired
	private WayBillManager wayBillManager;


	/**
	 * 获取电子面单json
	 * @param orderid 订单id
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/waybill-json")
	public Map createPrintData(String ordersn,Integer logi_id) throws Exception {
		
		Map map = new HashMap<>();
		WayBill wayBill = this.wayBillManager.getOpen();
		List<ConfigItem> wayBillPlugins = this.wayBillManager.getPlugin(wayBill.getBill_bean());
		String result = this.wayBillManager.createPrintData(ordersn,logi_id);
		//获取电子面单的快递单号
		JSONObject resultjson = JSONObject.fromObject(result);
		Object Order  = resultjson.get("Order");
		JSONObject Orders = JSONObject.fromObject(Order);
		String LogisticCode =  (String) Orders.get("LogisticCode");
		Object template =  resultjson.get("PrintTemplate");
		map.put("html", template);
		map.put("code", LogisticCode);
		return map;
		
	}

	
}
