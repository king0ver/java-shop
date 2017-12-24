package com.enation.app.shop.trade.controllor.front;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.po.OrderLog;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderLogManager;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 订单详细控制器
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月1日下午5:30:25
 */
@Api(description="订单明细查询API")
@RestController
@RequestMapping("/order-query")
public class OrderDetailController extends GridController{
	

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private ISellerManager sellerManager;
	
	@Autowired
	private IOrderLogManager orderLogManager;

	
	@ApiOperation(value="买家根据编号获取自己的订单", notes="买家根据编号获取自己的订单")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path")
	 })
	@ResponseBody
	@GetMapping(value="/mine/order/{ordersn}")
	public OrderDetail iGetBySn(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn ){

		Member member  = UserConext.getCurrentMember();
		
		if(   member ==null  || member.getMember_id()==null  ) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		OrderDetail po  = this.orderQueryManager.getOneBySn(ordersn);
		
		int memberid = po.getMember_id();
		if(memberid != member.getMember_id().intValue()){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		return  po;
	}
	
	
	
	
	@ApiOperation(value="商家根据编号获取自己的订单", notes="商家根据编号获取自己的订单")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path")
	 })
	@ResponseBody
	@GetMapping(value="/seller/order/{ordersn}")
	public OrderDetail sellerGetBySn(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn ){
		
		Seller seller  = sellerManager.getSeller();
		
		if(seller == null ) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		OrderDetail po  = this.orderQueryManager.getOneBySn(ordersn);
		
		int sellerid = po.getSeller_id();
		if(sellerid != seller.getMember_id()){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		return  po;
	}
	
	
	@ApiOperation(value="管理员根据编号获取订单", notes="管理员根据编号获取订单")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path")
	 })
	@GetMapping(value="/admin/order/{ordersn}")
	public OrderDetail  adminGetById(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn ){
		AdminUser adminuser  = UserConext.getCurrentAdminUser();
		
		if(adminuser == null){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		OrderDetail po  = this.orderQueryManager.getOneBySn(ordersn);
		
		return  po;
	}
	
	@ApiOperation(value="管理员根据订单号获取订单日志", notes="管理员根据编号获取订单日志")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path")
	})
	@GetMapping(value="/admin/order/{ordersn}/log")
	public List<OrderLog>  adminGetLog(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn ){
		AdminUser adminuser  = UserConext.getCurrentAdminUser();
		
		if(adminuser == null){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		return  orderLogManager.getOrderLogs(ordersn);
	}
	
	
}
