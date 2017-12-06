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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.cms.floor.model.floor.FloorDesign;
import com.enation.app.cms.floor.model.floor.FloorPo;
import com.enation.app.cms.floor.model.floor.PanelPo;
import com.enation.app.cms.floor.model.vo.Floor;
import com.enation.app.cms.floor.model.vo.Panel;
import com.enation.app.cms.floor.service.IFloorContentManager;
import com.enation.app.cms.floor.service.IFloorManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * cms系统楼层管理维护接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * 2017年8月13日 下午4:06:54
 */
@RestController
@RequestMapping("/cms/admin/floor")
@Api(description = "后台楼层维护接口")
@Validated
public class FloorBackController extends GridController {

	@Autowired
	private IFloorManager floorManager;
	@Autowired
	private IFloorContentManager floorContentManager;

	/**
	 * 进入pc端首页楼层页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pc/list")
	public ModelAndView list_json() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/floor/pc/list");
		return view;
	}

	/**
	 * 进入pc端楼层设计添加弹出层
	 * 
	 * @return
	 */

	@RequestMapping(value = "/pc/add-page")
	public ModelAndView addPage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/floor/pc/add");
		return view;
	}

	/**
	 * 进入pc端楼层设计修改弹出层
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pc/edit-page")
	public ModelAndView editPage(Integer id) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.setViewName("/cms/admin/floor/pc/edit");
		return view;
	}

	/**
	 * 进入pc端楼层设计页
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping(value = "/pc/design-page")
	public ModelAndView designPage(Integer id) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.setViewName("/cms/admin/floor/pc/design");
		return view;
	}

	/**
	 * pc端楼层设计里点击遮罩端修改标题的弹出层
	 * 
	 * @return
	 */
	@RequestMapping(value = "/title-selector-page")
	public ModelAndView titleSelectorPage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/selector/title_selector");
		return view;
	}
	
	/**
	 * pc端楼层设计里点击设置弹出修改板块名称
	 * 
	 * @return
	 */
	@RequestMapping(value = "/panel-name-page")
	public ModelAndView panelNamePage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/selector/panel_edit_selector");
		return view;
	}

	/**
	 * 手机端楼层设计
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mobile/design-page")
	public ModelAndView mobileDesignPage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/floor/mobile/design");
		return view;
	}

	/**
	 * 添加楼层接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value = "pc/mobile添加楼层接口", notes = "添加楼层接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "tpl_id", value = "模版主键", required = false, paramType = "query", dataType = "int"),
			@ApiImplicitParam(name = "name", value = "楼层名称", required = true, paramType = "query", dataType = "String", example = "服装鞋帽"),
			@ApiImplicitParam(name = "sort", value = "楼层排序", required = true, paramType = "query", dataType = "int", example = "1"),
			@ApiImplicitParam(name = "is_display", value = "楼层是否显示", required = true, paramType = "query", dataType = "int", example = "1"),
			@ApiImplicitParam(name = "anchor_words", value = "楼层锚点文字 只有当楼层类型 type=1时才有", required = true, paramType = "query", dataType = "String", example = "一楼服饰"),
			@ApiImplicitParam(name = "anchor_url", value = "楼层锚点图片 只有当楼层类型 type=1时才有", required = true, paramType = "query", dataType = "String", example = "fs://adfaf"),
			@ApiImplicitParam(name = "floor_color", value = "楼层颜色风格  只有当楼层类型 type=1时才有", required = true, paramType = "query", dataType = "String", example = "#000000") })
	public FloorPo addFloor(@Valid @ApiIgnore FloorPo floorPo) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.floorManager.addFloor(floorPo);
		return floorPo;
	}

	/**
	 * 删除楼层接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除楼层接口", notes = "删除楼层接口")
	@ApiImplicitParam(name = "id", value = "楼层id", required = true, paramType = "path", dataType = "Integer")
	public String deleteFloor(@PathVariable Integer id) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.floorManager.delete(id);
		this.floorContentManager.deleteByFloorId(id);
		return null;
	}

	/**
	 * 修改楼层接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	@ApiOperation(value = "修改楼层接口", notes = "修改楼层接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "楼层id", required = true, paramType = "path", dataType = "int"),
			@ApiImplicitParam(name = "type", value = "楼层类型 1为普通类型 2为品牌  3为广告条", required = true, paramType = "query", dataType = "Integer", example = "1"),
			@ApiImplicitParam(name = "name", value = "楼层名称", required = true, paramType = "query", dataType = "String", example = "服装鞋帽"),
			@ApiImplicitParam(name = "sort", value = "楼层排序", required = true, paramType = "query", dataType = "int", example = "1"),
			@ApiImplicitParam(name = "is_display", value = "楼层是否显示", required = true, paramType = "query", dataType = "int", example = "1"),
			@ApiImplicitParam(name = "anchor_words", value = "楼层锚点文字 只有当楼层类型 type=1时才有", required = true, paramType = "query", dataType = "String", example = "一楼服饰"),
			@ApiImplicitParam(name = "anchor_url", value = "楼层锚点图片 只有当楼层类型 type=1时才有", required = true, paramType = "query", dataType = "String", example = "fs://adfaf"),
			@ApiImplicitParam(name = "floor_color", value = "楼层颜色风格  只有当楼层类型 type=1时才有", required = true, paramType = "query", dataType = "String", example = "#000000") })
	public FloorPo updateFloor(@PathVariable Integer id, @Valid @ApiIgnore FloorPo floorPo) {
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		floorPo.setId(id);
		if (floorPo.getIs_display() == null)
			floorPo.setIs_display(0);
		this.floorManager.updateFloor(floorPo);
		return floorPo;
	}

	/**
	 * 查询楼层接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "查询楼层接口", notes = "查询楼层接口")
	@ApiImplicitParam(name = "id", value = "楼层id", required = true, paramType = "path", dataType = "Integer")
	public FloorPo getFloor(@PathVariable Integer id) {
		return this.floorManager.getCmsFloor(id);
	}

	/**
	 * 分页查询楼层接口
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "分页查询楼层接口", notes = "分页查询楼层接口")
	public GridJsonResult getFloorList(String order_by, String order, String client_type) {
		Page floorPage = floorManager.getCmsFloorList(this.getPage(), this.getPageSize(), order_by, order, client_type);
		return JsonResultUtil.getGridJson(floorPage);
	}
	/**
	 * 查询楼层接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ApiOperation(value = "查询楼层接口", notes = "查询楼层接口")
	public List<FloorPo> getFloorAll() {
		return this.floorManager.getCmsFloorAll();
	}
}
