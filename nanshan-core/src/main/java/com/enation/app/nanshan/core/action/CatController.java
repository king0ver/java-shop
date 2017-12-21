package com.enation.app.nanshan.core.action;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.service.ICatManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

@Controller
@Scope("prototype")
@RequestMapping("/admin/cat")
public class CatController {
	
	@Autowired
	ICatManager  catManager;
	
	
	@ResponseBody
	@RequestMapping("/catreset")
	public JsonResult reset(){
		try {
			this.catManager.reset();			
			return JsonResultUtil.getSuccessJson("初始化分类成功");
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("初始化分类失败");
		}
	}
	/** 
	* @Description:查询分类 
	* @author luyanfen  
	* @date 2017年12月18日 下午8:03:54
	*  
	*/ 
	@ResponseBody
	@RequestMapping("/getcat")
	public JsonResult getcCat(){
		try {
			return JsonResultUtil.getObjectJson(this.catManager.getCatTree());
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("初始化分类失败");
		}
	}
	
	/** 
	* @Description:活动预约 
	* @author luyanfen  
	* @date 2017年12月18日 下午8:04:04
	*  
	*/ 
	@ResponseBody
	@RequestMapping("/reserve")
	public JsonResult reserve(@RequestBody NanShanActReserve anShanActReserve){
		try {
			this.catManager.reserve(anShanActReserve);
			return JsonResultUtil.getSuccessJson("预约成功");
		} catch (RuntimeException e) {
			e.printStackTrace();			
			return JsonResultUtil.getErrorJson("预约失败，请联系客服处理");
		}
	}

}
