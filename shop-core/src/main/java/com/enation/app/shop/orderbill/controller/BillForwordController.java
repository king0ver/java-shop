package com.enation.app.shop.orderbill.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;

@Api(description = "后台结算单跳转Api")
@RestController
@RequestMapping("/shop/admin/order-bill")
public class BillForwordController {

	
	/**
	 * admin进入结算列表，所有商家某个周期的账单
	 * @return page
	 */
	@RequestMapping(value = "/bill/list-page")
	public ModelAndView listPage() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/shop/admin/orderbill/bill-list");
		return view;
	}
	
	/**
	 * 某个商家的账单详细列表
	 * @return
	 */
	@RequestMapping(value = "/bill/detail-list")
	public ModelAndView billDetailList(String sn) {
		ModelAndView view = new ModelAndView();
		view.addObject("sn", sn);
		view.setViewName("/shop/admin/orderbill/bill-detail-list");
		return view;
	}
	
	
	/**
	 * 某商家某周期的账单详细
	 * @return
	 */
	@RequestMapping(value = "/bill/order-detail")
	public ModelAndView billDetail(Integer id) {
		ModelAndView view = new ModelAndView();
		view.addObject("id", id);
		view.setViewName("/shop/admin/orderbill/bill-order-detail");
		return view;
	}
}
