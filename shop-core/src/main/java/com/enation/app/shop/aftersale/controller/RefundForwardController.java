package com.enation.app.shop.aftersale.controller;

import com.enation.framework.context.webcontext.ThreadContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.framework.action.GridController;

/**
 * 后台退款单跳转controller
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月31日 下午5:47:34
 */
@RestController
@RequestMapping("/shop/admin")
public class RefundForwardController  extends GridController {

	/**
	 * 跳转订单列表页
	 * @return
	 */
	@RequestMapping(value="/refund-list")
	public ModelAndView list(){

		
		ModelAndView view = getGridModelAndView();
		Object ident = ThreadContextHolder.getHttpRequest().getParameter("ident");
		view.addObject("ident",ident);
		view.setViewName("/b2b2c/admin/aftersale/refund_list");
		return view;
	}
	
	/**
	 * 跳转至订单详情页
	 * @return
	 */
	@RequestMapping(value="/refund-detail",method=RequestMethod.GET)
	public ModelAndView orderDetail(String sn){
		ModelAndView view = new ModelAndView();
		view.addObject("sn",sn);
		view.setViewName("/b2b2c/admin/aftersale/returned-detail"); 
		return view;
	}
}
