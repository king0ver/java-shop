package com.enation.app.shop.payment.service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.payment.model.po.PaymentMethod;
import com.enation.app.shop.payment.model.vo.OrderPayReturnParam;
import com.enation.app.shop.payment.model.vo.PayBill;
import com.enation.app.shop.payment.model.vo.PaymentResult;
import com.enation.app.shop.trade.model.enums.TradeType;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 支付插件父类<br>
 * 具有读取配置的能力
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月3日下午11:38:38
 */
public abstract class AbstractPaymentPlugin {
	
	protected final Log logger = LogFactory.getLog(getClass());
	

	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IOrderOperateManager orderOperateManager;
	
	@Autowired
	private IPaymentMethodManager paymentMethodManager ;
	
	
	/**
	 * 获取插件的配置方式
	 * @return
	 */
	protected Map<String, String> getConfig(){
		//获取当前支付插件的id
		String paymentMethodId = this.getPluginId();
		String config  = daoSupport.queryForString("select config from es_payment_method where plugin_id=?", paymentMethodId);
		if(StringUtil.isEmpty(config)){
			return new HashMap<>();
		}
		Gson gson = new Gson();
		List<ConfigItem> list = gson.fromJson(config,   new TypeToken<List<ConfigItem>>() {  
                }.getType());
		Map<String, String> result = new HashMap<>();
		if(list!=null){
			for(ConfigItem item : list){
				result.put(item.getName(), item.getValue());
			}
		}
		return result;
	}

	
	protected void paySuccess(PaymentResult paymentResult){

		//查询支付方式
		PaymentMethod payment = paymentMethodManager.getByPluginId(this.getPluginId());
		String sn  = paymentResult.getSn();
		TradeType tradeType = paymentResult.getTrade_type();
		
		double payPrice  = paymentResult.getPay_price();
		
		OrderPayReturnParam param = new OrderPayReturnParam();
		
		param.setPayment_method_id(payment.getMethod_id());
		param.setPayment_method_name(payment.getMethod_name());
		param.setPayment_plugin_id(payment.getPlugin_id());
		param.setPayprice(payPrice);
		param.setPay_key(sn);
		param.setPay_order_no(paymentResult.getPay_order_no());
		//根据不同的订单类型进行付款确认
		if( paymentResult.getTrade_type().name().equals( tradeType.order.name()) ){//订单
			orderOperateManager.payOrder(param, null );
		}
		
		if( paymentResult.getTrade_type().name().equals( tradeType.trade.name()) ){//交易
			
			orderOperateManager.payTrade(param, null );
		}

		if(paymentResult.getTrade_type().name().equals(TradeType.recharge.name())){//充值
			orderOperateManager.payRecharge(param, null);
		}
		
		
	}

	/**
	 * 获取同步通知url
	 * @param tradeType 交易类型
	 * @param payMode 支付类型：<br>
	 * normal:正常的网页跳转
	 * qr:二维码扫描
	 * @return
	 */
	protected String getReturnUrl( PayBill bill){
		String tradeType = bill.getTradeType().name();
		String payMode = bill.getPay_mode();
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String serverName =request.getServerName();
		int port = request.getServerPort();
		String portstr = "";
		if(port!=80){
			portstr = ":"+port;
		}
		String contextPath = request.getContextPath();
		
		return "http://"+serverName+portstr+contextPath+"/order-pay/return/" +tradeType+"/"+payMode+"/" + this.getPluginId()+".do";
	}
	

	
	
	/**
	 * 供支付插件获取显示url
	 * @return
	 */
	private String showUrlPrefix;
	
	
	/**
	 * 显示账单详细url
	 * @param bill
	 * @return
	 */
	protected String getShowUrl(PayBill bill){
		return showUrlPrefix+bill.getOrder_id();
	}
	
	protected String getCallBackUrl( TradeType tradeType){
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String portstr = "";
		if(port!=80){
			portstr = ":"+port;
		}
		String contextPath = request.getContextPath();
		
		return "http://"+serverName+portstr+contextPath+"/order-pay/callback/"+tradeType+"/" + this.getPluginId()+".do";
	}	
	
	/**
	 * 返回价格字符串
	 * @param price
	 * @return
	 */
	protected String formatPrice(Double price){
		NumberFormat nFormat=NumberFormat.getNumberInstance();
        nFormat.setMaximumFractionDigits(0); 
        nFormat.setGroupingUsed(false);
        return nFormat.format(price);
	}

	
	public String getRefundErrorMessageCode() {
		return "REFUND_ERROR_MESSAGE";
	}

	protected abstract String getPluginId(); 
	
}
