package com.enation.app.shop.aftersale.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.aftersale.model.vo.RefundDetail;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;



/**
 * 退货（款）单详细API
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月25日下午5:41:40
 */
@Api(description = "退货（款）单详细API")
@RestController
@RequestMapping("/after-sale")
@Validated
public class RefundDetailController {
	
	@Autowired
	private IAfterSaleManager afterSaleManager;
	
	@ApiOperation(value = "管理员的退款(货)详细", response = RefundDetail.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sn", value = "退款(货)编号", required =true, dataType = "String" ,paramType="path")
	 })
	@GetMapping(value = "/admin/refund/{sn}")
	public   RefundDetail  adminDetail( @PathVariable  String sn  ) {
		
		AdminUser adminUser  = UserConext.getCurrentAdminUser();
		
		if(adminUser == null){
			throw new NoPermissionException("无权访问此订单");
		}
		
		RefundDetail detail  = this.afterSaleManager.getDetail(sn);
		
		return detail;
	}
	
	
	
}
