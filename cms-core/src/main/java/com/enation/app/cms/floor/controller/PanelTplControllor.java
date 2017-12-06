package com.enation.app.cms.floor.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.cms.floor.model.po.PanelTpl;
import com.enation.app.cms.floor.service.IPanelTplManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 后台楼层管理接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * 2017年8月12日 下午1:06:24
 */
@Validated
@RestController
@RequestMapping("/cms")
@Api(description = "后台楼层管理接口")
public class PanelTplControllor extends GridController {
	@Autowired
	private IPanelTplManager panelTplManager;

	/**
	 * 进入pc端楼层list页
	 * 
	 * @return page
	 */
	@RequestMapping(value = "/admin/panel-tpl/pc/list")
	public ModelAndView list_json() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/panel/pc/list");
		return view;
	}

	/**
	 * 进入pc端添加楼层模版页面
	 * 
	 * @return page
	 */
	@RequestMapping(value = "/admin/panel/pc/add-page")
	public ModelAndView add_page() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/panel/pc/add");
		return view;
	}

	/**
	 * 进入pc端修改楼层模版页面
	 * 
	 * @param tpl_id
	 * @return page
	 */
	@RequestMapping(value = "/admin/panel/pc/edit_page")
	public ModelAndView edit_page(Integer tpl_id) {
		ModelAndView view = new ModelAndView();
		view.addObject("panel_id", tpl_id);
		view.setViewName("/cms/admin/panel/pc/edit");
		return view;
	}

	/**
	 * 手机端装修list页
	 * 
	 * @return page
	 */
	@RequestMapping(value = "/admin/panel-tpl/mobile/list")
	public ModelAndView list_mobile() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/panel/mobile/list");
		return view;
	}

	/**
	 * 进入手机端添加页
	 * 
	 * @return page
	 */
	@RequestMapping(value = "/admin/panel/mobile/add-page")
	public ModelAndView add_page_mobile() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/panel/mobile/add");
		return view;
	}

	/**
	 * 进入手机端修改页
	 * 
	 * @param tpl_id 模版id
	 * @return page
	 */
	@RequestMapping(value = "/admin/panel/mobile/edit_page")
	public ModelAndView edit_page_mobile(Integer tpl_id) {
		ModelAndView view = new ModelAndView();
		view.addObject("panel_id", tpl_id);
		view.setViewName("/cms/admin/panel/mobile/edit");
		return view;
	}

	/**
	 * 后台楼层面板列表
	 * 
	 * @param client_type 客户端类型
	 * @return 集合
	 */
	@ApiOperation("后台楼层面板列表")
	@GetMapping(value = "/admin/panel-tpl")
	public GridJsonResult list(String client_type) {
		Page panelPage = panelTplManager.list(this.getPage(), this.getPageSize(), client_type);
		return JsonResultUtil.getGridJson(panelPage);

	}

	/**
	 * 后台增加楼层面板
	 * 
	 * @param panelTpl对象
	 * @return  panelTpl对象
	 */
	@ApiOperation("后台增加楼层面板")
	@PostMapping(value = "/admin/panel-tpl")
	public PanelTpl saveAdd(@Valid PanelTpl panelTpl) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		panelTplManager.add(panelTpl);
		return panelTpl;
	}

	/**
	 * 后台删除楼层面板
	 * 
	 * @param tpl_id 面板id
	 * @return 楼层面板删除成功
	 */
	@ApiOperation("后台删除楼层面板")
	@ApiImplicitParam(name = "tpl_id", value = "面板id", required = true, dataType = "int", paramType = "path")
	@DeleteMapping(value = "/admin/panel-tpl/{tpl_id}")
	public String delete(@PathVariable Integer tpl_id){
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.panelTplManager.delete(tpl_id);
		return "楼层面板删除成功";
	}

	/**
	 * 后台修改楼层面板
	 * 
	 * @param panelTpl 对象
	 * @return PanelTpl 对象
	 */
	@ApiOperation("后台修改楼层面板")
	@ApiImplicitParam(name = "tpl_id", value = "面板id", required = true, dataType = "int", paramType = "path")
	@PostMapping(value = "/admin/panel-tpl/{tpl_id}")
	public PanelTpl saveEdit(PanelTpl panelTpl){
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.panelTplManager.edit(panelTpl);
		panelTpl = this.panelTplManager.get(panelTpl.getTpl_id());
		return panelTpl;
	}

	/**
	 * 获取面板模板内容
	 * 
	 * @param tpl_id 模板id
	 * @return  面板模板内容
	 */
	@ApiOperation(value = "获取面板模板内容", notes = "根据模板id获取面板模板内容")
	@ApiImplicitParam(name = "id", value = "模板id", required = true, paramType = "path", dataType = "Integer")
	@GetMapping("/admin/panel-tpl/content/{tpl_id}")
	public String getPanelTpl(@NotNull(message = "必须输入模板id") @PathVariable Integer tpl_id) {

		String content = this.panelTplManager.getPanelTplContent(tpl_id);

		return content;
	}

	/**
	 * 根据id获取楼层
	 * 
	 * @param tpl_id  楼层id
	 * @return PanelTpl 对象
	 */
	@ApiOperation("获取楼层")
	@ApiImplicitParam(name = "tpl_id", value = "楼层id", required = true, dataType = "int", paramType = "path")
	@GetMapping(value = "/admin/panel-tpl/{tpl_id}")
	public PanelTpl get(@PathVariable int tpl_id) {
		return panelTplManager.get(tpl_id);
	}

	/**
	 * 获取所有主楼层面板
	 * 
	 * @param client_type 客户端类型
	 * @return PanelTpl 集合
	 */
	@ApiOperation("获取所有主楼层面板")
	@GetMapping(value = "/admin/panel-tpl/main")
	public List<PanelTpl> getAllMainPanel(String client_type) {
		return panelTplManager.getMain(client_type);
	}
	
	
}
