package com.enation.app.base.core.action.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.util.JsonResultUtil;

@Controller
@Scope("prototype")
@RequestMapping("/core/admin/data")
public class DataApiController  {
	
	public Object export(String tb){
		
		try {
			String[] tables={tb};
			return DBSolutionFactory.dbExport(tables, false, "");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("导出失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/export-xml")
	public Object exportXml(String tb){
		
		try {
			String[] b2b2c_tables=new String[]{"es_order","es_trade","es_order_items"};
			return DBSolutionFactory.dbExportXml(b2b2c_tables, "shop1.xml");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("导出失败");
		}
	}
	
}
