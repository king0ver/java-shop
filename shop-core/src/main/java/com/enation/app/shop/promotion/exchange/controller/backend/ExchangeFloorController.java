package com.enation.app.shop.promotion.exchange.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.enation.framework.database.IDaoSupport;
/**
 * 
 * 跳转到积分商城 主页管理
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月23日 下午2:30:16
 */
@Controller
@RequestMapping("/shop/admin/exchange-floor")
public class ExchangeFloorController {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	
	/**
	 * 跳转主页管理
	 * @return url
	 */
	@RequestMapping(value="list")
	public ModelAndView list(){
		ModelAndView view=new ModelAndView();
		Integer pageid=this.daoSupport.queryForInt("select id from es_page where name=?", "积分商城首页");
		view.addObject("pageid", pageid);
		view.setViewName("/floor/admin/indexpage/list");
		return view;
	}
}
