package com.enation.app.shop.connect.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.shop.connect.model.ConnectSetting;
import com.enation.app.shop.connect.service.ConnectLogin;
import com.enation.framework.action.GridController;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;

/**
 * Author: Dawei 
 * Datetime: 2016-03-14 18:22
 */

@Controller
@RequestMapping("/shop/admin/connect-setting")
public class ConnectSettingAction extends GridController {

	@Autowired
	private ISettingService settingService;

	@RequestMapping
	public ModelAndView execute() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/shop/admin/connect/setting");

		Map<String, Map<String, String>> allSetting = settingService
				.getSetting();
		if (allSetting.containsKey(ConnectSetting.SETTING_KEY)) {
			Map<String, String> setting = allSetting
					.get(ConnectSetting.SETTING_KEY);
			modelAndView.addObject("setting", setting);
		}
		return modelAndView;
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public JsonResult save() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Enumeration<String> names = request.getParameterNames();
		Map<String, Map<String, String>> settings = new HashMap<String, Map<String, String>>();

		while (names.hasMoreElements()) {

			String name = names.nextElement();
			String paramValue = request.getParameter(name);

			Map<String, String> params = settings
					.get(ConnectSetting.SETTING_KEY);
			if (params == null) {
				params = new HashMap<String, String>();
				settings.put(ConnectSetting.SETTING_KEY, params);
			}
			params.put(name, paramValue);
		}

		settingService.save(settings);
		ConnectLogin.resetConnectSetting();
		return JsonResultUtil.getSuccessJson("信任登录配置修改成功");
	}
}
