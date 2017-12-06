package com.enation.app.shop.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.base.core.service.impl.RegionsManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;
/**
 * 
 * 初始化数据，所有需要初始化的数据都可以放入
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年10月4日 上午7:09:37
 */
@RestController
@RequestMapping("/shop/admin/init")
public class InitController {

	@Autowired
	private IRegionsManager regionsManager;

	/**
	 * 跳转到初始化界面
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/init")
	public ModelAndView init() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/init/init");
		return view;
	}



	@ResponseBody
	@RequestMapping("/reg-init")
	public JsonResult regionsInit() {
		this.regionsManager.reset();
		return JsonResultUtil.getSuccessJson("初始化地区成功");
	}
}
