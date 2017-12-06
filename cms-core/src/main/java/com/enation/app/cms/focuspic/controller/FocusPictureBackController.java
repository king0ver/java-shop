package com.enation.app.cms.focuspic.controller;

import java.util.List;

import javax.validation.Valid;

import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.cms.focuspic.model.po.CmsFocusPicture;
import com.enation.app.cms.focuspic.service.IFocusPictureManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * cms系统焦点图管理模块 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日 上午11:06:47
 */
@RestController
@RequestMapping("/cms/admin/focus")
@Api(description="后台焦点图片维护接口")
public class FocusPictureBackController {
	
	@Autowired
	private IFocusPictureManager focusPictureManager;
	
	/**
	 * 进入pc端焦点图页
	 * 
	 * @return page
	 */
	@RequestMapping(value = "/pc/list")
	public ModelAndView list_pc() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/focus/pc/design");
		return view;
	}
	/**
	 * 进入移动端焦点图页
	 * 
	 * @return page
	 */
	@RequestMapping(value = "/mobile/list")
	public ModelAndView list_mobile() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/cms/admin/focus/mobile/design");
		return view;
	}
	
	
	/**
	 * 增加焦点图接口
	 * @return CmsFocusPicture 模型
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ApiOperation(value="增加焦点图接口",notes="增加焦点接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="pic_url",value="图片url",required=true,paramType="query",dataType="String",example="fs://asdfasdf"),
		@ApiImplicitParam(name="operation_type",value="操作类型  1:链接地址 2.关键字搜索 3.专题编号 4.商品编号 5.店铺编号 6.商品分类搜索",required=false,paramType="query",dataType="int",example="1"),
		@ApiImplicitParam(name="operation_param",value="操作传入参数",required=false,paramType="query",dataType="String",example="1"),
	})
	public CmsFocusPicture addFocus(@ApiIgnore @Valid CmsFocusPicture cmsFocusPicture){
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		return this.focusPictureManager.addFocus(cmsFocusPicture);
	}
	
	/**
	 * 删除焦点图接口
	 * @return null
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	@ApiOperation(value="删除焦点图接口",notes="删除焦点图接口")
	@ApiImplicitParam(name="id",value="焦点图id",required=true,paramType="path",dataType="int",example="1")
	public String deleteFocus(@PathVariable Integer id){
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		this.focusPictureManager.deleteFocus(id);
		return null;
	}
	
	/**
	 * 修改焦点图接口
	 * @return CmsFocusPicture 模型
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	@ApiOperation(value="修改焦点图接口",notes="修改焦点图接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id",value="焦点图id",required=true,paramType="path",dataType="int",example="1"),
		@ApiImplicitParam(name="pic_url",value="图片url",required=true,paramType="query",dataType="String",example="fs://asdfasdf"),
		@ApiImplicitParam(name="operation_type",value="操作类型  1:链接地址 2.关键字搜索 3.专题编号 4.商品编号 5.店铺编号 6.商品分类搜索",required=false,paramType="query",dataType="int",example="1"),
		@ApiImplicitParam(name="operation_param",value="操作传入参数",required=false,paramType="query",dataType="String",example="1"),
	})
	public CmsFocusPicture updateFocus(@PathVariable Integer id,@ApiIgnore @Valid CmsFocusPicture cmsFocusPicture){
		//判断是否为演示站点
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		cmsFocusPicture.setId(id);
		return 	this.focusPictureManager.updateFocus(cmsFocusPicture);
	}
	
	/**
	 * 查询焦点图接口
	 * @return  CmsFocusPicture 模型
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ApiOperation(value="查看焦点图接口",notes="查看焦点图接口")
	@ApiImplicitParam(name="id",value="焦点图id",required=true,paramType="path",dataType="int",example="1")
	public CmsFocusPicture getFocus(@PathVariable Integer id){
		return this.focusPictureManager.getFocus(id);
	}
	

	/**
	 * 查询焦点图列表接口
	 * @param client_type 客户端类型
	 * @return CmsFocusPicture集合
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ApiOperation(value="查询焦点图列表接口",notes="查询焦点图列表接口")
	public List<CmsFocusPicture> getAllFocusList(String client_type){
		return this.focusPictureManager.getAllFocusList(client_type);
	}
	
}
