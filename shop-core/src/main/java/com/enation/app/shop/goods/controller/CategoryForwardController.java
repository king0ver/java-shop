package com.enation.app.shop.goods.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 后台分类类表的跳转页面
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月19日 上午9:24:58
 */
@RestController
@RequestMapping("/shop/admin")
public class CategoryForwardController {
	@Autowired
	private ICategoryManager categoryManager;

	// 进入分类列表页
	@RequestMapping(value = "/catrgory/list")
	public ModelAndView list() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/cat/cat_list");
		return view;
	}

	// 进入参数页面
	@RequestMapping(value = "/cat/params")
	public ModelAndView params(Integer id) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.setViewName("/shop/admin/cat/params");
		return view;
	}

	/**
	 * 跳转至分类添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add")
	public ModelAndView add() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/cat/cat_add");
		return view;
	}

	/**
	 * 跳转至分类修改页面 并获取数据返回
	 * 
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit(Integer cat_id) {
		ModelAndView view = new ModelAndView();
		Category cat = categoryManager.get(cat_id);
		if (cat.getImage() != null && !StringUtil.isEmpty(cat.getImage())) {
			view.addObject("imgPath", cat.getImage());
		}
		view.addObject("catList", categoryManager.listAllChildren(0));
		view.addObject("cat", cat);
		view.setViewName("/shop/admin/cat/cat_edit");
		return view;
	}

}
