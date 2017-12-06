package com.enation.app.shop.goods.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.po.Parameters;
import com.enation.app.shop.goods.model.po.ParametersGroup;
import com.enation.app.shop.goods.service.IParametersGroupManager;
import com.enation.app.shop.goods.service.IParametersManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 参数相关controller
 * 
 * @author fk
 * @version v1.0
 * 2017年4月10日 下午4:25:07
 */
@Api(description = "商品参数相关api")
@RestController
@RequestMapping("/goods/admin/params")
public class ParametersBackController {

	@Autowired
	private IParametersGroupManager parametersGroupManager;
	@Autowired
	private IParametersManager parametersManager;
	
	
	@RequestMapping(value = "/add-page")
	public ModelAndView addPage(Integer id) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.setViewName("/shop/admin/params/add");
		return view;
	}
	@RequestMapping(value = "/edit-page")
	public ModelAndView editPage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/params/edit");
		return view;
	}
	
	
	@ApiOperation(value="增加参数组", notes="分类绑定参数时，增加参数组时使用，单次增加单次保存")
	@RequestMapping(value = "/group",method = RequestMethod.POST)
	public ParametersGroup saveGroup(ParametersGroup categoryParamsGroup) throws Exception{
		
		ParametersGroup group = this.parametersGroupManager.add(categoryParamsGroup);
		
		return group;
	}
	
	@ApiOperation(value="修改参数组", notes="分类绑定参数时，修改参数组时使用，单次修改单次保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name="group_id",value="参数组id",required=true,paramType="path",dataType="int",example="1")
	})
	@RequestMapping(value = "/group/{group_id}",method = RequestMethod.POST)
	public ParametersGroup updateGroup(@PathVariable Integer group_id,ParametersGroup categoryParamsGroup) throws Exception{
		
		categoryParamsGroup.setGroup_id(group_id);
		ParametersGroup group = this.parametersGroupManager.update(categoryParamsGroup);
		
		return group;
	}
	
	@ApiOperation(value="删除参数组", notes="分类绑定参数时，删除参数组时使用")
	@ApiImplicitParam(name="group_id",value="参数组id",required=true,paramType="path",dataType="int",example="1")
	@RequestMapping(value = "/group/{group_id}",method = RequestMethod.DELETE)
	public String deleteGroup(@PathVariable Integer group_id){
		
		this.parametersGroupManager.deleteGroup(group_id);
		
		return null;
	}
	
	@ApiOperation(value="参数组上移或者下移", notes="分类绑定参数时，上移下移参数组时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="group_id",value="参数组id",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="sort_type",value="排序类型，上移 up，下移down",required=true,paramType="query",dataType="String"),
	})
	@RequestMapping(value = "/group/{group_id}/sort",method = RequestMethod.POST)
	public String groupSort(@PathVariable Integer group_id,String sort_type){
		
		this.parametersGroupManager.groupSort(group_id,sort_type);
		
		return null;
	}
	
	@ApiOperation(value="增加参数", notes="分类绑定参数时，增加参数组下的参数时使用，单次增加单次保存")
	@RequestMapping(method = RequestMethod.POST)
	public Parameters saveParam(Parameters param){
		
		Parameters categoryParams = this.parametersManager.add(param);
		
		return categoryParams;
	}
	
	@ApiOperation(value="修改参数", notes="分类绑定参数时，修改参数组下的参数时使用，单次修改单次保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name="param_id",value="参数id",required=true,paramType="path",dataType="int",example="1")
	})
	@RequestMapping(value = "/{param_id}",method = RequestMethod.POST)
	public Parameters updateParam(Parameters param,@PathVariable Integer param_id){
		
		param.setParam_id(param_id);
		Parameters categoryParams = this.parametersManager.update(param);
		
		return categoryParams;
	}
	
	@ApiOperation(value="删除参数", notes="分类绑定参数时，删除参数组下的参数时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="param_id",value="参数id",required=true,paramType="path",dataType="int",example="1")
	})
	@RequestMapping(value = "/{param_id}",method = RequestMethod.DELETE)
	public String deleteParam(@PathVariable Integer param_id){
		
		this.parametersManager.delete(param_id);
		
		return null;
	}
	
	@ApiOperation(value="参数上移或者下移", notes="分类绑定参数时，上移下移参数组时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="sort_type",value="排序类型，上移 up，下移down",required=true,paramType="query",dataType="String"),
		@ApiImplicitParam(name="param_id",value="排序类型，上移 up，下移down",required=true,paramType="query",dataType="String"),
	})
	@RequestMapping(value = "/{param_id}/sort",method = RequestMethod.POST)
	public String paramSort(@PathVariable Integer param_id,String sort_type){
		
		this.parametersManager.paramSort(param_id,sort_type);
		
		return null;
	}
}
