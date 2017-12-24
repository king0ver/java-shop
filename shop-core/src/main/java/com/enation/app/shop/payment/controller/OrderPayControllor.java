package com.enation.app.shop.payment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.payment.service.IOrderPayManager;
import com.enation.app.shop.trade.model.enums.TradeType;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description="订单支付API")
@RestController
@RequestMapping("/order-pay")
@Validated
public class OrderPayControllor {
	
	@Autowired
	private IOrderPayManager orderPayManager;
	
	
	@ApiOperation(value="对一个交易发起支付" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "trade_sn", value = "要支付的交易sn", required = true, dataType = "String" ,paramType="path"),
         @ApiImplicitParam(name = "payment_method_id", value = "支付方式id，如果不传递则使用订单中记录的支付方式", required = false, dataType = "Integer",paramType="query"),
         @ApiImplicitParam(name = "pay_mode", value = "支付模式，如果不传递使用normal正常模式", required = false, dataType = "String",paramType="query",allowableValues="normal,qr"),
         @ApiImplicitParam(name = "client_type", value = "客户端类型", required = false, dataType = "String",paramType="query",allowableValues="PC,WAP,APP,JSAPI")
	 })
	@ResponseBody
	@RequestMapping(value="/trade/{trade_sn}", produces = MediaType.TEXT_HTML_VALUE,method=RequestMethod.GET)
	public String payTrade(@NotNull(message="交易sn不能为空") @PathVariable(name="trade_sn")  String trade_sn, Integer payment_method_id,String pay_mode,
			String client_type){
		//默认值为normal
		if( StringUtil.isEmpty( pay_mode) ){
			pay_mode = "normal";
		}
		String html = orderPayManager.payTrade(trade_sn,payment_method_id, pay_mode,client_type);
		return html;
	}
	
	
	@ApiOperation(value="对一个订单发起支付" )
	@ApiImplicitParams({
		@ApiImplicitParam(name = "order_sn", value = "要支付的订单sn", required = true, dataType = "String" ,paramType="path"),
		@ApiImplicitParam(name = "payment_method_id", value = "支付方式id，如果不传递则使用订单中记录的支付方式", required = false, dataType = "Integer",paramType="query"),
		@ApiImplicitParam(name = "pay_mode", value = "支付模式，如果不传递使用normal正常模式", required = false, dataType = "String",paramType="query",allowableValues="normal,qr"),
		 @ApiImplicitParam(name = "client_type", value = "客户端类型", required = false, dataType = "String",paramType="query",allowableValues="PC,WAP,APP")
		
	})
	@ResponseBody
	@RequestMapping(value="/order/{order_sn}", produces = MediaType.TEXT_HTML_VALUE,method=RequestMethod.GET)
	public String payOrder(@NotNull(message="交易sn不能为空") @PathVariable(name="order_sn")  String order_sn, Integer payment_method_id,String pay_mode,
			String client_type){
		//默认值为normal
		if( StringUtil.isEmpty( pay_mode) ){
			pay_mode = "normal";
		}
		
		String html = orderPayManager.payOrder(order_sn, payment_method_id, pay_mode,client_type);
		
		return html;
	}

	@ApiOperation(value="对一个订单发起支付" )
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order_sn", value = "要支付的订单sn", required = true, dataType = "String" ,paramType="path"),
			@ApiImplicitParam(name = "payment_method_id", value = "支付方式id，如果不传递则使用订单中记录的支付方式", required = false, dataType = "Integer",paramType="query"),
			@ApiImplicitParam(name = "pay_mode", value = "支付模式，如果不传递使用normal正常模式", required = false, dataType = "String",paramType="query",allowableValues="normal,qr"),
			@ApiImplicitParam(name = "client_type", value = "客户端类型", required = false, dataType = "String",paramType="query",allowableValues="PC,WAP,APP")

	})
	@ResponseBody
	@RequestMapping(value="/order/{recharge_sn}", produces = MediaType.TEXT_HTML_VALUE,method=RequestMethod.GET)
	public String payRecharge(@NotNull(message="充值sn不能为空") @PathVariable(name="recharge_sn")  String recharge_sn, Integer payment_method_id,String pay_mode,
						   String client_type){
		//默认值为normal
		if( StringUtil.isEmpty( pay_mode) ){
			pay_mode = "normal";
		}

		String html = orderPayManager.payRecharge(recharge_sn, payment_method_id, pay_mode,client_type);

		return html;
	}
	
	@ApiOperation(value="接收支付同步回调" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "pluginid", value = "插件id", required = true, dataType = "Integer" ,paramType="path"),
	 })
	@ResponseBody
	@RequestMapping(value="/return/{tradetype}/{paymode}/{pluginid}", produces = MediaType.TEXT_HTML_VALUE,method=RequestMethod.GET)
	public String payReturn(
			@NotNull(message = "交易类型不能为空") @PathVariable(name="tradetype")  String tradetype,
			@NotNull(message = "支付插件类型不能为空") @PathVariable(name="pluginid") String payment_plugin_id,
			@PathVariable(name="paymode") String paymode){
		
		
				
		String out_trade_no = this.orderPayManager.payReturn(TradeType.valueOf(tradetype),payment_plugin_id);
		
		String jump_html ="<script>";
		//扫码支付
		if("qr".equals(paymode)){
			jump_html+="window.parent.location.href='"+getPay_success_url(tradetype,out_trade_no) +"'" ;
		}else{
			jump_html+="location.href='"+getPay_success_url(tradetype,out_trade_no) +"'" ;
		}
		
		jump_html+="</script>";
		
		return jump_html;
	}
	
	
	@ApiOperation(value="接收支付异步回调")
	@ApiImplicitParams({
         @ApiImplicitParam(name = "pluginid", value = "插件id", required = true, dataType = "Integer" ,paramType="path"),
	 })
	@ResponseBody
	@RequestMapping(value="/callback/{tradetype}/{pluginid}", produces = MediaType.TEXT_HTML_VALUE,method=RequestMethod.POST)
	public String payCallback(
			@NotNull(message="交易类型不能为空") @PathVariable(name="tradetype")  String tradetype,
			@NotNull(message="支付插件类型不能为空") @PathVariable(name="pluginid") String payment_plugin_id){
		 
		String result  = this.orderPayManager.payCallback(TradeType.valueOf(tradetype),payment_plugin_id);
		return result;
	}


	/**
	 * 获取支付成功调取页面
	 * @param tradetype
	 * @return
	 */
	private String getPay_success_url(String tradetype,String out_trade_no) {
		
		HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
		String serverName =request.getServerName();
		int port =request.getServerPort();
		String portstr = "";
		if(port!=80){
			portstr = ":"+port;
		}
		String contextPath = request.getContextPath();
		
		return "http://"+serverName+portstr+contextPath+"/"+tradetype+"_"+out_trade_no+"_payment-result.html" ;
	}
	
}
