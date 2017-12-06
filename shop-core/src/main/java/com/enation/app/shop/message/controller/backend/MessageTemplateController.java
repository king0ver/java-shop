package com.enation.app.shop.message.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 消息模板Controller
 * @author kanon
 * @since v6.4.0
 * @version v1.0 
 * 2017-8-16
 */
@Controller
@RequestMapping("/b2b2c/admin/message-template")
public class MessageTemplateController extends GridController{
	
	@Autowired
	private IMessageManager messageManager;
	
	/** 
	 * 跳转至模板列表页面
	 * @param type 类型
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView list(Integer type){
		
		ModelAndView view=this.getGridModelAndView();
		view.addObject("type", type);
		view.setViewName("/b2b2c/admin/message/list");
		return view;
	}

	
	/**
	 * 获取模板列表JSON数据
	 * @param type 类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list-json")
	public GridJsonResult listJson(Integer type){
		
		this.webpage=messageManager.listPage(this.getPage(), this.getPageSize(), type);
		
		return JsonResultUtil.getGridJson(this.webpage);
	}
	
	/**
	 * 获取消息模板
	 * @param id ID编号
	 * @return
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(Integer id,Integer type){
		ModelAndView view=this.getGridModelAndView();
		view.addObject("messageTemplate",messageManager.getMessageTemplate(id));
		if(type==1){
			view.setViewName("/b2b2c/admin/message/message");
		}else if(type==2){
			view.setViewName("/b2b2c/admin/message/sms");
		}else{
			view.setViewName("/b2b2c/admin/message/email");
		}
		return view;
	}
	
	/**
	 * 修改消息模板
	 * @param messageTemplate 消息模板
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save-edit")
	public JsonResult saveEdit(MessageTemplate messageTemplate){
		try {
			messageManager.editMessageTemplate(messageTemplate);
			return JsonResultUtil.getSuccessJson("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("修改失败");
		}
	}
	
	/**
	 * 跳转至添加消息模板页面
	 * @param type 类型
	 * @return
	 */
	@RequestMapping("/add")
	public ModelAndView add(Integer type){
		ModelAndView view=new ModelAndView();
		view.addObject("type",type);
		view.setViewName("/b2b2c/admin/message/add_message");
		return view;
	}
	
	/**
	 * 添加消息模板
	 * @param messageTemplate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save-add")
	public JsonResult saveAdd(MessageTemplate messageTemplate){
		try {
			messageManager.addMessageTemplate(messageTemplate);
			return JsonResultUtil.getSuccessJson("添加成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("添加失败");
		}
	}
}
