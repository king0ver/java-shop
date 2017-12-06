package com.enation.app.shop.member.controller.backend;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.member.model.MessageBg;
import com.enation.app.shop.member.service.IMessageBgManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 后台站内消息
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月5日 下午1:34:22
 */
@Controller
@Scope("prototype")
@RequestMapping("/shop/admin/message")
@SuppressWarnings({ "rawtypes", "unchecked" })
@Validated
public class MessageBgController  extends GridController {

	@Autowired
	private IMessageBgManager messageBgManager;
	
	/**
	 * 跳转历史消息列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView list() {
		ModelAndView view =this.getGridModelAndView();
		view.setViewName("/shop/admin/messagebg/messagebg_list");
		return view;
	}
	
	/**
	 * 历史消息json
	 * @param pageNo 分页页数,Integer
	 * @param pageSize  每页分页的数量,Integer
	 * @return  历史消息json
	 */
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson() {
		webpage = messageBgManager.getAllMessage(getPage(), getPageSize());
		return JsonResultUtil.getGridJson(webpage);

	}
	
	
	/**
	 * 跳转到添加消息
	 * @return
	 */
	@RequestMapping(value="/add")
	public ModelAndView add() {
		ModelAndView view =this.getGridModelAndView();
		view.setViewName("/shop/admin/messagebg/messagebg_add");
		return view;
	}
	
	/**
	 * 消息发送
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save-message")
	public Object saveMessage(@Valid MessageBg message) {
		Map map = new HashMap();
		map.put("result", 1);
		map.put("message", "消息发送成功");
		if(message != null){
			try {
				messageBgManager.add(message);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("result", 0);
				map.put("message", "消息发送失败");
			}
		}else{
			map.put("result", 0);
			map.put("message", "消息发送失败");
		}
		return map;
	}
	
	
}
