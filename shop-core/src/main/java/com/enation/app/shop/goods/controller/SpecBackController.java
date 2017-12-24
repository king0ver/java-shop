package com.enation.app.shop.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.po.Specification;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.goods.service.ISpecManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.annotation.PageNo;
import com.enation.framework.annotation.PageSize;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 规格controller 关于规格的读和写都在此
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月29日 下午3:12:56
 */
@Api(description = "规格api")
@RestController
public class SpecBackController extends GridController {

	@Autowired
	private ISpecManager specManager;

	/**
	 * 跳转至添加规格页面
	 * 
	 * @return 添加规格页面
	 */
	@RequestMapping(value = "/shop/admin/spec/add-page")
	public ModelAndView addPage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/spec/spec_add_edit");
		return view;
	}
	@RequestMapping(value = "/shop/admin/spec/list")
	public ModelAndView ListPage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("shop/admin/spec/spec_list");
		return view;
	}

	/**
	 * 跳转至修改规格页面
	 * 
	 * @return 修改规格页面
	 */
	@RequestMapping(value = "/shop/admin/spec/edit-page")
	public ModelAndView editPage(Integer spec_id, Integer spec_type) {
		ModelAndView view = new ModelAndView();
		view.addObject("spec_id", spec_id);
		view.addObject("spec_type", spec_type);
		view.setViewName("/shop/admin/spec/spec_add_edit");
		return view;
	}
	
	/**
	 * 添加规格
	 * 
	 * @param spec
	 * @return
	 */
	@ApiOperation(value = "添加规格", notes = "添加规格时使用")
	@RequestMapping(value = "/goods/admin/spec", method = RequestMethod.POST)
	public Specification saveAdd(Specification spec) {
		this.specManager.add(spec);
		return spec;
	}

	/**
	 * 修改规格
	 * 
	 * @param id
	 *            规格ID
	 * @param spec
	 *            接收到的对象
	 * @return
	 */
	@ApiOperation(value = "修改规格")
	@ApiImplicitParam(name = "id", value = "规格id", required = true, paramType = "path", dataType = "int")
	@RequestMapping(value = "/goods/admin/spec/{id}", method = RequestMethod.POST)
	public Specification saveEdit(@PathVariable Integer id, Specification spec) {
		spec.setSpec_id(id);
		this.specManager.edit(spec);
		return spec;
	}

	/**
	 * 删除规格
	 * 
	 * @param spec_id
	 *            规格ID
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "删除规格", notes = "删除规格时使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spec_id", value = "规格id数组", required = true, paramType = "query", dataType = "int", allowMultiple = true), })
	@RequestMapping(value = "/goods/admin/spec/{spec_id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("spec_id") Integer[] spec_id) throws Exception {

		this.specManager.delete(spec_id);
	}

	/**
	 * 规格列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value = "查询规格列表", notes = "管理员查询查询规格列表使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "/goods-info/admin/spec", method = RequestMethod.GET)
	public GridJsonResult getSpecList(@PageNo Integer pageNo, @PageSize Integer pageSize) {
		Page page = this.specManager.getSpecList(this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(page);
	}

	/**
	 * 根据规格id 查看规格
	 * 
	 * @param spec_id
	 *            规格ID
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "查看某规格")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spec_id", value = "规格id", required = true, dataType = "int", paramType = "path"), })
	@RequestMapping(value = "/goods-info/admin/spec/{spec_id}", method = RequestMethod.GET)
	public Specification getSpec(@PathVariable Integer spec_id) throws Exception {
		Specification vo = this.specManager.get(spec_id);
		return vo;
	}

	/**
	 * 查询某规格的规格值
	 * 
	 * @param spec_id
	 *            规格id
	 * @return
	 */
	@RequestMapping(value = "/goods-info/admin/get/spec-value/{spec_id}", method = RequestMethod.GET)
	public List<SpecValueVo> getSpecValueList(@PathVariable Integer spec_id) {
		List<SpecValueVo> list = this.specManager.getSpecValueList(spec_id);
		return list;
	}

	/**
	 * 保存某规格的规格值
	 * 
	 * @param spec_id
	 *            规格id
	 * @return
	 */
	@PostMapping(value = "/goods-info/admin/save/spec-value/{spec_id}")
	public void saveSpecValue(@PathVariable Integer spec_id, @RequestBody List<SpecValue> valueList) {
		this.specManager.saveSpecValue(spec_id, valueList);
	}

	/**
	 * 检测规格是否被使用
	 * 
	 * @param spec_id
	 *            规格Id数组,Integer[]
	 * @return true：使用，false：未使用
	 */
	@ApiOperation(value = "检测规格是否被使用", notes = "true：使用，false：未使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spec_ids", value = "规格id数组", required = true, dataType = "int", paramType = "query", allowMultiple = true), })
	@RequestMapping(value = "/goods-info/admin/spec/check-used", method = RequestMethod.GET)
	public boolean checkUsed(Integer[] spec_ids) {

		boolean res = this.specManager.checkUsed(spec_ids);

		return res;
	}

	@ApiOperation(value = "检测规格值是否被使用", notes = "检测规格值是否被使用，使用不可删除，返回true，使用")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spec_value_id", value = "规格值id", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "/goods-info/admin/spec/check-value-used", method = RequestMethod.GET)
	public boolean checkSpecValueUsed(Integer spec_value_id) {

		boolean isused = this.specManager.checkUsed(spec_value_id);

		return isused;
	}

}
