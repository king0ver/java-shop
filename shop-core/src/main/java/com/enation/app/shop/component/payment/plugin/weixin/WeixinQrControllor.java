package com.enation.app.shop.component.payment.plugin.weixin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 微信二维码
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月10日下午3:16:54
 */
@Api(description="微信二维码页面")
@Controller
@RequestMapping("/order-pay")
@Validated
public class WeixinQrControllor {

	@Autowired
	private ICache cache;
	
	@ApiOperation(value="显示一个微信二维码" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "pr", value = "二维码短地址", required = true, dataType = "String" ,paramType="query"),
	 })
	@ResponseBody
	@GetMapping(value="/weixin/qr/{pr}"  )
	public byte[] qr(@NotNull(message="二维码短地址不能为空")  @PathVariable(name="pr") String pr) throws IOException, WriterException{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int width = 280;//图片的宽度
		int height = 280;//高度 
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix m = writer.encode("weixin://wxpay/bizpayurl?pr="+pr, BarcodeFormat.QR_CODE, height, width);
		MatrixToImageWriter.writeToStream(m, "png", stream);

		return stream.toByteArray();
	}
	
	@ApiOperation(value="获取微信扫描支付的状态" )
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sn", value = "订单或交易编号", required = true, dataType = "String" ,paramType="path"),
	 })
	@ResponseBody
	@GetMapping("/weixin/status/{sn}")
	public String payStatus(@NotNull(message="订单或交易编号不能为空")  @PathVariable(name="sn")  String sn){
		
		String status  = (String) cache.get(WeixinPuginConfig.CACHE_KEY_PREFIX+sn);
		if(!"ok".equals(status)){
			return "no";
		}else{
			return "ok";
		}
		
	}

	
	/**
	 * 测试用，设置一个订单的扫描状态为成功
	 * @param sn
	 * @return
	 */
	@ResponseBody
	@PostMapping("/weixin/status/{sn}")
	public String setStatus(@PathVariable(name="sn")  String sn){
		cache.put( WeixinPuginConfig.CACHE_KEY_PREFIX+sn,"ok",60);
		return "ok";
	}
	
	
	@ApiOperation(value="微信二维码显示页" )
	@ApiImplicitParams({
         @ApiImplicitParam(name = "sn", value = "订单或交易编号", required = true, dataType = "String" ,paramType="path"),
         @ApiImplicitParam(name = "pr", value = "二维码短地址", required = true, dataType = "String",paramType="path")
	 })
	@GetMapping("/weixin/qrpage/{tradeType}/{sn}/{pr}")
	public ModelAndView qrPage(@PathVariable(name="tradeType") String tradeType, @PathVariable(name="sn") String sn,@PathVariable(name="pr") String pr ) {
		HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
		ModelAndView view = new ModelAndView();
		
		view.addObject("pr", pr);
		view.addObject("sn", sn);
		view.addObject("pay_success_url", this.getPay_success_url(tradeType,sn));
		view.addObject("jquery_path", "http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js");
		view.addObject("ctx", request.getContextPath());
		
		view.setViewName("/shop/admin/order/weixin_qr"); 
		
		return view;
	}

	/**
	 * 获取支付成功调取页面
	 * @param tradetype
	 * @return
	 */
	private String getPay_success_url(String tradetype,String out_trade_no) {
		
		HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
		String serverName = request.getServerName();
		int port =request.getServerPort();
		String portstr = "";
		if(port!=80){
			portstr = ":"+port;
		}
		String contextPath = request.getContextPath();
		
		return "http://"+serverName+portstr+contextPath+"/"+tradetype+"_"+out_trade_no+"_payment-result.html" ;
	}

}
