package com.enation.app.shop.promotion.groupbuy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.enation.framework.action.GridController;
/**
 * 多商户团购管理类
 * @author fenlongli
 * @date 2015-7-13 下午2:17:31
 */
@Controller
@RequestMapping("/b2b2c/admin/groupBuy")
public class B2b2cGroupBuyController extends GridController{

	@RequestMapping(value="/list")
	public ModelAndView list(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/groupbuy/act_list");
		return view;
	}
}
