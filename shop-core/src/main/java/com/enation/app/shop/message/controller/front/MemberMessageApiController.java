package com.enation.app.shop.message.controller.front;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.message.service.IMemberMessageManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.TestUtil;

/**
 * 
 * 会员站内消息api
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月5日 下午1:05:20
 * 
 * @author Kanon
 * @version v1.1
 * @since v6.4.0
 * 2017-9-8
 */
@Controller
@RequestMapping("/api/shop/member-message")
@Scope("prototype")
public class MemberMessageApiController {

	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IMemberMessageManager memberMessageManager;
	/**
	 * 获取站内消息未读数量
	 * @return
	 */
	@ResponseBody  
	@RequestMapping(value="/get-message-data", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult getMessageData(){
		
		try{
			int count = memberMessageManager.getMessageNoReadCount();
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("count", count);
			
			return JsonResultUtil.getObjectJson(data);
		}catch(RuntimeException e ){
			TestUtil.print(e);
			this.logger.error("获取未读站内消息出错",e);
			return JsonResultUtil.getErrorJson("获取未读站内消息出错");
		}
	}
	
	/**
	 * 消息已读
	 * @return
	 */
	@ResponseBody  
	@RequestMapping(value="/have-read", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult haveRead(){
		try{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			//增加权限验证
			Member member = UserConext.getCurrentMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("登陆超时");
			}
			
			String messageIds = request.getParameter("messageids");
			if(messageIds == null){
				return JsonResultUtil.getErrorJson("消息已读失败");
			}
			String[] messageIdArray = messageIds.split(",");
			int length = messageIdArray.length;
			Integer count = memberMessageManager.getCountByIds(messageIds);
			
			if(!count.equals(length)){
				return JsonResultUtil.getErrorJson("没有操作权限");
			}
			memberMessageManager.read(messageIds);
			return JsonResultUtil.getSuccessJson("消息已读");
		}catch(RuntimeException e){
			TestUtil.print(e);
			this.logger.error("消息已读失败",e);
			return JsonResultUtil.getErrorJson("消息已读失败");
		}
	}
	
	/**
	 * 消息删除
	 * @return
	 */
	@ResponseBody  
	@RequestMapping(value="/msg-delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult deleteMsg(){
		try{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			//增加权限验证
			Member member = UserConext.getCurrentMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("登陆超时");
			}
			
			String messageIds = request.getParameter("messageids");
			if(messageIds == null){
				return JsonResultUtil.getErrorJson("消息删除失败");
			}
			String[] messageIdArray = messageIds.split(",");
			int length = messageIdArray.length;
			Integer count = memberMessageManager.getCountByIds(messageIds);
			
			if(!count.equals(length)){
				return JsonResultUtil.getErrorJson("没有操作权限");
			}
			memberMessageManager.delete(messageIds);
			return JsonResultUtil.getSuccessJson("消息已删除");
		}catch(RuntimeException e){
			TestUtil.print(e);
			this.logger.error("消息删除失败",e);
			return JsonResultUtil.getErrorJson("消息删除失败");
		}
	}
}
