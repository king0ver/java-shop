package com.enation.app.shop.trade.controllor.front;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.model.vo.Receipt;
import com.enation.app.shop.trade.model.vo.Trade;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.service.ITradeCreator;
import com.enation.app.shop.trade.service.ITradePriceManager;
import com.enation.app.shop.trade.support.CheckoutParam;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 购物车控制器
 * 
 * @author xulipeng
 * @version 1.0
 * @since v6.4 2017年10月12日15:40:38
 */
@Api(description = "交易API")
@RestController
@RequestMapping("/api/shop/order-create")
@Validated
public class TradeController {

	@Autowired
	private ITradeCreator tradeCreator;

	@Autowired
	private ITradePriceManager tradePriceManager;

	@Autowired
	private ICheckoutParamManager checkoutParamManager;

	@Autowired
	private ICartWriteManager cartWriteManager;

	private final Logger logger = Logger.getLogger(getClass());

	@ApiOperation(value = "创建一个交易", response = Trade.class)
	@ResponseBody
	@RequestMapping(value = "/trade", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult create() {
		Trade trade = new Trade();
		
		String client_type = null;
		if(isWap()){
			client_type = ClientType.WAP.name();
		}else{
			client_type = ClientType.PC.name();
		}
		this.checkoutParamManager.setClientType(client_type);
		
		try {
			trade = tradeCreator.createTrade();
		} catch (Exception e) {
			if (!StringUtil.isEmpty(e.getMessage())) {
				return JsonResultUtil.getErrorJson(e.getMessage());
			}
			return JsonResultUtil.getErrorJson("创建订单失败");
		}
		return JsonResultUtil.getObjectJson(trade);
	}

	@ApiOperation(value = "创建一个交易(可以传结算参数)", response = Trade.class)
	@ResponseBody
	@RequestMapping(value = "/trade-with-param", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public Trade create(CheckoutParam param) {
		CheckoutParam oldparam = checkoutParamManager.getParam();

		if (param.getAddressId() != null) {
			oldparam.setAddressId(param.getAddressId());
		}

		if (param.getReceive_time() != null) {
			oldparam.setReceive_time(param.getReceive_time());
		}

		if (param.getPaymentType() != null) {
			oldparam.setPaymentType(param.getPaymentType());
		}
		Receipt receipt = param.getReceipt();
		Receipt oldReceipt = oldparam.getReceipt();
		if (param.getReceipt() != null) {
			if (StringUtil.notEmpty(receipt.getNeed_receipt())) {
				oldReceipt.setNeed_receipt(receipt.getNeed_receipt());
			}

			if (StringUtil.notEmpty(receipt.getTitle())) {
				oldReceipt.setTitle(receipt.getTitle());
			}

			if (StringUtil.notEmpty(receipt.getContent())) {
				oldReceipt.setContent(receipt.getContent());
			}

			oldparam.setReceipt(oldReceipt);
		}
		if (param.getRemark() != null) {
			oldparam.setRemark(param.getRemark());
		}

		checkoutParamManager.setAll(oldparam);
		Trade trade = tradeCreator.createTrade();
		return trade;
	}

	/**
	 * 因为当用户进入过结算页后，会计算配送费用，返回到购物车，购物车展示的价格不应该计算配送费用
	 * @return
	 */
	@ApiOperation(value="获取当前交易的价格" ,response=Trade.class)
	@ResponseBody
	@RequestMapping(value="/trade/price", produces = MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET)
	public JsonResult price(){
		try {

			PriceDetail price = tradePriceManager.getTradePrice();
			return JsonResultUtil.getObjectJson(price);
			
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("读取价格出现错误");
		}
	}
	
	/**
	 * 判断是否是wap访问
	 * @param header
	 * @return 是否是wap
	 */
	public static boolean isWap(){
		String header = ThreadContextHolder.getHttpRequest().getHeader("User-Agent");
		boolean flag = false;
		String[] keywords = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser","Mobile" };
		for (String s : keywords) {
			if(header.contains(s)){
				flag = true;
				break;
			}
		}
		return flag;
	}

}
