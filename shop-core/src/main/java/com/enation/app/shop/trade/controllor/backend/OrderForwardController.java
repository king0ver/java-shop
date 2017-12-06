package com.enation.app.shop.trade.controllor.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.core.DomainUtil;
import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.trade.model.po.OrderLog;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderLogManager;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 后台订单跳转controller
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月31日 下午5:47:34
 */
@RestController
@RequestMapping("/shop/admin")
public class OrderForwardController  extends GridController {
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IOrderQueryManager orderQueryManager;
	
	@Autowired
	private IOrderLogManager orderLogManager;
	
	/**
	 * 跳转订单列表页
	 * @return
	 */
	@RequestMapping(value="/order-list")
	public ModelAndView list(){

		
		ModelAndView view = getGridModelAndView();
		
		view.setViewName("/b2b2c/admin/order/store_order_list");
		return view;
	}
	
	/**
	 * 跳转至订单详情页
	 * @return
	 */
	@RequestMapping(value="/order-detail",method=RequestMethod.GET)
	public ModelAndView orderDetail(String sn){
		ModelAndView view = new ModelAndView();
		AdminUser adminuser  = UserConext.getCurrentAdminUser();
		
		if(adminuser == null){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		OrderDetail po  = this.orderQueryManager.getOneBySn(sn);
		List<OrderLog> logs = orderLogManager.getOrderLogs(sn);
		view.addObject("orderDetail",po);
		view.addObject("orderLogs",logs);
		//循环赠品
		Gson gson = new Gson();
		String sql ="select meta_value from es_order_meta where order_sn=? and meta_key='gift'";
		String fullDiscountGift= daoSupport.queryForString(sql,sn);
		List<FullDiscountGift> giftlist = gson.fromJson(fullDiscountGift, new TypeToken<List<FullDiscountGift>>() {}.getType());
		
		view.addObject("giftlist",giftlist);
		
		String domain = DomainUtil.getGoodsDomain(ClientType.PC);
		view.addObject("domain",domain);
		view.setViewName("/b2b2c/admin/order/order_detail"); 
		return view;
	}
}
