package com.enation.app.shop.message.controller.front;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.shop.message.service.IStoreMessageManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;

/**
 * 店铺消息API
 * @author kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-16
 */
@Controller
@RequestMapping("/api/b2b2c/store-message")
public class StoreMessageApiController {
	
	@Autowired
	private IStoreMessageManager storeMessageManager;

	private final Logger logger = Logger.getLogger(getClass());
	/**
	 * 删除店铺消息
	 * @return 删除状态
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public JsonResult delete(){
		try {
			
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			
			String messageIds = request.getParameter("messageids");
			
			this.storeMessageManager.delete(messageIds);
			
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (Exception e) {
			this.logger.error("删除店铺消息出错", e);
			return JsonResultUtil.getErrorJson("删除失败");
		}
	}
	
	/**
	 * 店铺消息标记已读
	 * @param id 消息ID
	 * @return 标记状态
	 */
	@ResponseBody
	@RequestMapping("/read")
	public JsonResult read(){
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			
			String messageIds = request.getParameter("messageids");
			
			this.storeMessageManager.read(messageIds);
			
			return JsonResultUtil.getSuccessJson("标记成功");
		} catch (Exception e) {
			this.logger.error("标记店铺消息出错", e);
			return JsonResultUtil.getErrorJson("标记失败");
		}
	}
}
