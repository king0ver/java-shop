package com.enation.app.shop.trade.controllor.front;


import com.enation.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.vo.OrderLine;
import com.enation.app.shop.trade.model.vo.OrderLineSeller;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 订单查询API
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年6月2日上午10:54:58
 */
@Api(description="订单查询API")
@RestController
@RequestMapping("/order-query")
public class OrderQueryController extends GridController {

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private ISellerManager sellerManager;
	
	@ApiOperation(value="查询卖家订单", notes="查询卖家订单")
	@ResponseBody
	@RequestMapping(value="/seller/order",method=RequestMethod.GET)
	public Page<OrderLineSeller> listSeller(int page_no, int page_size , OrderQueryParam queryParam){
		
		Seller seller  = sellerManager.getSeller();
		
		if(seller == null ) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		//禁止按买家查询
		queryParam.setMember_id(null);
		queryParam.setSeller_id(seller.getMember_id());
		queryParam.setPage_no(page_no);
		queryParam.setPage_size(page_size);
		Page<OrderLineSeller> page =  this.orderQueryManager.query(queryParam);
		
		return  page;
	}

	
	@ApiOperation(value="查询所有订单", notes="需要管理员权限")
	@ResponseBody
	@RequestMapping(value="/admin/order",method=RequestMethod.GET)
	public GridJsonResult adminQueryAll(OrderQueryParam queryParam ,String keyword){
		
		AdminUser adminuser  = UserConext.getCurrentAdminUser();
		if(adminuser == null){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		if (StringUtil.notEmpty(keyword)) {
			queryParam.setOrder_sn(keyword);
		}
		queryParam.setPage_no(this.getPage());
		queryParam.setPage_size(this.getPageSize());
		Page<OrderLineSeller> page = this.orderQueryManager.query(queryParam);
		return  JsonResultUtil.getGridJson(page);
	}
	
}
