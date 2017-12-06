package com.enation.app.shop.trade.controllor.front;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.Receipt;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.support.CheckoutParam;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 结算参数控制器
 * @author xulipeng
 * @version 1.0
 * @since v6.4
 * 2017年10月12日15:40:51
 */
@Api(value="checkout" , description = "结算API")
@RestController
@RequestMapping("/api/shop/order-create")
public class CheckoutParamControllor {

	@Autowired
	private ICheckoutParamManager checkoutParamManager;

	@Autowired
	private ICartWriteManager cartWriteManager;
	
	@ApiOperation(value="设置收货地址id" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "addressid", value = "收货地址id", required = true, dataType = "int" ,paramType="path"),
	 })
	@PostMapping(value="/checkout-param/addressid/{addressid}")
	public JsonResult setAddressid(@NotNull(message="必须指定收货地址id") @PathVariable(value="addressid") Integer addressid){
		
		this.checkoutParamManager.setAddressId(addressid);
		
		//设置配送方式
		this.cartWriteManager.setShipping();
		
		return JsonResultUtil.getSuccessJson("设置成功");
	}
	
	
	
	@ApiOperation(value="设置支付类型" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "payment_type", value = "支付类型", required = true, dataType = "String" ,paramType="query",allowableValues="online,cod,offline")
	 })
	@PostMapping(value="/checkout-param/payment-type")
	public JsonResult setPaymentType(@NotNull(message="必须指定支付类型")   String payment_type){
		
		PaymentType paymentType= 	PaymentType.valueOf( payment_type);
		
		this.checkoutParamManager.setPaymentType(paymentType);
		
		return JsonResultUtil.getSuccessJson("设置成功");
	}
	
	
	@ApiOperation(value="设置发票信息" ,response=Receipt.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "need_receipt", value = "是否需要发票", required = true, dataType = "String" ,paramType="query",allowableValues="yes,no"),
        @ApiImplicitParam(name = "title", value = "抬头", required = false, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "content", value = "内容", required = false, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "duty_invoice", value = "税号", required = false, dataType = "String" ,paramType="query"),
	 })	
	@PostMapping(value="/checkout-param/receipt")
	public JsonResult setReceipt( @Valid @ApiIgnore  Receipt receipt){
		
		this.checkoutParamManager.setReceipt(receipt);
		
		return JsonResultUtil.getSuccessJson("设置成功");
	}
	
	@ApiOperation(value="设置送货时间" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "receivetime", value = "送货时间", required = true, dataType = "String" ,paramType="query"),
	 })
	@PostMapping(value="/checkout-param/receivetime")
	public JsonResult setReceiveTime(@NotNull(message="必须指定送货时间")   String receivetime){
		
		this.checkoutParamManager.setReceiveTime(receivetime);
		
		return JsonResultUtil.getSuccessJson("设置成功");
	}
	
	
	@ApiOperation(value="设置订单备注" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "remark", value = "订单备注", required = true, dataType = "String" ,paramType="query"),
	 })
	@PostMapping(value="/checkout-param/remark")
	public JsonResult setRemark(@NotNull(message="必须指定备注")   String remark){
		
		this.checkoutParamManager.setRemark(remark);
		
		return JsonResultUtil.getSuccessJson("设置成功");
	}
	
	
	@ApiOperation(value="获取结算参数" ,response=CheckoutParam.class)
	@ResponseBody
	@RequestMapping(value="/checkout-param", produces = MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET)
	public CheckoutParam get( ){
		return this.checkoutParamManager.getParam();
	}
	
}