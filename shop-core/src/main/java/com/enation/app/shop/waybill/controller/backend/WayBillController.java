package com.enation.app.shop.waybill.controller.backend;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.waybill.model.WayBill;
import com.enation.app.shop.waybill.service.IWayBillManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.JsonUtil;

import net.sf.json.JSONObject;


/**
 * 
 * 电子面单控制器
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月9日 下午6:36:00
 */
@Validated
@RestController
@RequestMapping("/shop/admin/waybill")
public class WayBillController extends GridController{
   
	
	@Autowired
	private IWayBillManager wayBillManager;
	
	/**
	 * 跳转至电子面单列表
	 * @return  电子面单
	 */
	@RequestMapping(value = "/waybill-list")
	public ModelAndView list() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/waybill/waybill_list");
		return view;
	}
	
	/**
	 * 获取电子面单列表json
	 * @return 电子面单列表json
	 */
	@RequestMapping(value = "waybill-list-json")
	public GridJsonResult list_json() {
		Page waybillPage =wayBillManager.List(this.getPage(), this.getPageSize());
		return	JsonResultUtil.getGridJson(waybillPage);

	}
	
	
	
	/**
	 * 设置启用的电子面单
	 * @param id 电子面单id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/set-open")
	public JsonResult setOpen(Integer id){
		try {
			this.wayBillManager.open(id);
			return JsonResultUtil.getSuccessJson("设置成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("出现错误，请稍后重试");
		}
	}
	
	
	
	/**
	 * 修改电子面单参数数据
	 * @param id	 电子面单id
	 * @return
	 */
	@RequestMapping(value="/waybill-edit")
	public ModelAndView edit(Integer id){
		ModelAndView view = new ModelAndView();
		try {
			WayBill wayBill=this.wayBillManager.getWayBill(id); 
			List<ConfigItem> wayBillHtml = this.wayBillManager.getPlugin(wayBill.getBill_bean());
			String config = wayBill.getBill_config();
			Map configs=new HashMap();
			if(!config.equals("")) {
				configs=JsonUtil.toMap(config);
			}
			view.addObject("configs",configs);	
		    view.addObject("wayBill",wayBill);
			view.addObject("wayBillHtml", wayBillHtml);
			view.setViewName("/shop/admin/waybill/waybill_edit");
		} catch (Exception e) {
			Logger logger = Logger.getLogger(getClass());
			logger.error("修改参数出现错误",e);
		}
		return view;
	}
	
	
	/**
	 * 修改保存参数
	 * @param id 电子面单id
	 * @return 保存返回json
	 */
	@ResponseBody  
	@RequestMapping(value="/save-edit")
	public JsonResult saveEdit(@RequestBody WayBill wayBill){
		try{
			this.wayBillManager.edit(wayBill);
			return JsonResultUtil.getSuccessJson("设置成功");
		}catch(Exception e){
			this.logger.error("设置短信网关参数出错", e);
			return JsonResultUtil.getErrorJson("设置失败");
		}
	}
	
	
}
