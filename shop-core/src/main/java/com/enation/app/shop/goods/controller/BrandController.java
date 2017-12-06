package com.enation.app.shop.goods.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.po.Brand;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

/**
 * 品牌action 负责品牌的添加和修改
 * 
 * @author apexking
 * @author LiFenLong 2014-4-1;4.0版本改造
 * @author kingapex 2016.2.15;6.1版本改造
 */
@Controller
@RequestMapping("/shop/admin/brand")
public class BrandController extends GridController {
	@Autowired
	private IBrandManager brandManager;

	/**
	 * 查看品牌名称是否重复
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkname")
	public JsonResult checkname(Brand brand) {
		if (this.brandManager.checkname(brand.getName(), brand.getBrand_id())) {
			return JsonResultUtil.getSuccessJson("已存在");
		} else {
			return JsonResultUtil.getSuccessJson("不存在");
		}
	}

	/**
	 * 跳转至品牌添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add() {
		return "/shop/admin/brand/brand_add";
	}

	/**
	 * 跳转至品牌列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView mv = this.getGridModelAndView();
		mv.setViewName("/shop/admin/brand/brand_list");

		return mv;
	}

	/**
	 * 获取品牌JSON列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/list-json")
	public GridJsonResult listJson(String keyword) {
		Map brandMap = new HashMap();
		brandMap.put("keyword", keyword);
		Page webpage = brandManager.searchBrand(brandMap, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(webpage);
	}

	/**
	 * 保存品牌添加
	 * 
	 * @param brand
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save")
	public void save(Brand brand) {
		brand.setDisabled(0);
		this.brandManager.add(brand);
	}

	/**
	 * 跳转至品牌修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit(Integer brandId) {
		ModelAndView view = new ModelAndView();
		Brand brand = brandManager.get(brandId);
		view.addObject("brand", brand);
		view.setViewName("/shop/admin/brand/brand_edit");
		return view;
	}

	/**
	 * 根据品牌ID 查品牌
	 * 
	 * @param brandId
	 * @return
	 */
	@GetMapping(value = "/get/{brandId}")
	@ResponseBody
	public Brand get(@PathVariable Integer brandId) {
		Brand brand = brandManager.get(brandId);
		return brand;
	}

	/**
	 * 保存修改品牌
	 * 
	 * @author xulipeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save-edit")
	public void saveEdit(Brand brand) {
		brandManager.update(brand);
	}

	/**
	 * 将品牌删除
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonResult delete(Integer[] brand_id) {
		try {
			this.brandManager.delete(brand_id);
			return JsonResultUtil.getSuccessJson("删除成功");
		} catch (RuntimeException e) {
			this.logger.error("删除失败", e);
			return JsonResultUtil.getErrorJson("删除失败:" + e.getMessage());
		}

	}

}
