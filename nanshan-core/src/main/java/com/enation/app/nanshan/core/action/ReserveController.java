package com.enation.app.nanshan.core.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.model.ReserveQueryParam;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

/**  
*
* @Description: 预约管理
* @author luyanfen
* @date 2017年12月27日 下午7:35:34
*  
*/ 
@Controller 
@Scope("prototype")
@RequestMapping("/admin/reserve")
public class ReserveController  extends GridController {
	@Autowired
	IArticleManager  articleManager;
	/**
	 * 跳转到列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(){
		String ctx=ThreadContextHolder.getHttpRequest().getContextPath();
		ModelAndView view=new ModelAndView();
		view.setViewName("/nanshan/admin/reserve/list");
		view.addObject("ctx",ctx);
		return view;
	}
	
	/**
	 * 获取列表JSON
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(ReserveQueryParam param){
		Page page = this.articleManager.queryReserveList(param, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}

}
