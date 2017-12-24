package com.enation.app.shop.payment.controller.backend;

import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.payment.model.vo.PaymentPluginVo;
import com.enation.app.shop.payment.service.IPaymentMethodManager;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="支付方式controller")
@RestController
@RequestMapping("/order-pay/admin/payment")
@Validated
public class PaymentMethodController{

	@Autowired
	private IPaymentMethodManager paymentMethodManager;
	
	
	@ApiOperation("后台获取所有支付方式插件")
	@GetMapping("/list-json")
	public GridJsonResult listJson(){
		
		return JsonResultUtil.getGridJson(paymentMethodManager.getAllPlugins());
	}
	
	
	@ApiOperation("后台保存支付方式")
	@PostMapping
	public PaymentPluginVo save(@RequestBody PaymentPluginVo vo){
		AdminUser user  = UserConext.getCurrentAdminUser();
		if(EopSetting.IS_DEMO_SITE && user!= null && user.getFounder() != 1){
			throw new UnProccessableServiceException(ErrorCode.DEMO_SITE_TIP, EopSetting.DEMO_SITE_TIP);
		}
		paymentMethodManager.save(vo);
		
		return vo;
	}
	
}
