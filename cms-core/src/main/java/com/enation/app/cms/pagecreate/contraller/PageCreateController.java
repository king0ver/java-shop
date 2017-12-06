package com.enation.app.cms.pagecreate.contraller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.cms.pagecreate.service.IPageCreateManager;
import com.enation.eop.SystemSetting;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.JsonResultUtil;

/**
 * 
 * 静态也生成控制器
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年9月4日 上午11:37:16
 */
@RestController
@RequestMapping("/shop/admin/page-create")
public class PageCreateController {

	@Autowired
	private IPageCreateManager pageCreateManager;
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ISettingService settingService;


	/**
	 * 转向生成页面
	 */
	@RequestMapping(value = "/input")
	public ModelAndView input() {
		ModelAndView view = new ModelAndView();
		view.addObject("address", SystemSetting.getStatic_page_address());
		view.setViewName("/shop/admin/pagecreator/create");
		return view;
	}


	/**
	 * 生成		
	 * @param choose_pages	要生成的页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/create")
	public JsonResult create(String[] choose_pages){
		try {
			boolean result = pageCreateManager.startCreate( choose_pages);
			if(result){
				return JsonResultUtil.getSuccessJson("生成成功");
			}else{
				return JsonResultUtil.getErrorJson("有静态页生成任务正在进行中，需等待本次任务完成后才能再次生成。");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("生成失败"+e.getMessage());
		}

	}
	@ResponseBody
	@RequestMapping(value="/save")
	public JsonResult save(String address){
		Map<String, String> settings = settingService.getSetting("system");
		settings.put("static_page_address", address);
		settingService.save("system", settings);
		SystemSetting.load();
		return JsonResultUtil.getSuccessJson("修改成功");
	}

}
