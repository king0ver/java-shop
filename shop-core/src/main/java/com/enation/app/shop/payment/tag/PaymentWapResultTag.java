package com.enation.app.shop.payment.tag;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.payment.model.po.PaymentBill;
import com.enation.app.shop.payment.model.vo.PaymentOrderResult;
import com.enation.app.shop.payment.model.vo.PaymentResult;
import com.enation.app.shop.payment.service.IPaymentBillManager;
import com.enation.app.shop.trade.model.enums.TradeType;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.service.ITradeQueryManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;

/**
 * 支付结果标签
 * @author kingapex
 * 2013-9-5上午9:34:50
 */
@Component
@Scope("prototype")
public class PaymentWapResultTag extends BaseFreeMarkerTag {

	@Autowired
	private IPaymentBillManager paymentBillManager;
	
	@Autowired
	private IOrderQueryManager orderQueryManager;
	
	@Autowired
	private ITradeQueryManager tradeQueryManager;
	
	/**
	 * 支付结果标签
	 * 些标签必须写在路径为：/payment_wap_result.html的模板中。用于第三方支付后，显示支付结果。
	 * @param 无
	 * @return 支付结果，PaymentResult型
	 * {@link PaymentResult}
	 */
	@Override
	protected Object exec(Map p) throws TemplateModelException {
		
		PaymentOrderResult result = new PaymentOrderResult();
		
		try{
			HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
			String operation = request.getParameter("operation");//success 支付成功   fail 支付失敗
			if("fail".equals(operation)){
				result.setResult(0);
				result.setError("用户遇到错误或者主动放弃");
				return result;
			}
			String url = RequestUtil.getRequestUrl(request);
			String sn = null;
			String ordertype =null;
			String[] params =this.getPluginid(url);
			
			ordertype = params[0];
			sn = params[1];
			if (null == sn) {
				result.setResult(0);
				result.setError("参数不正确");
			} else {
				PaymentBill bill = paymentBillManager.getByPayKey(sn);
				result.setResult(1);
				
				if(TradeType.order.name().equals(ordertype)){//订单
					
					result.setOrderDetail(orderQueryManager.getOneBySn(bill.getSn()));
				}else if(TradeType.trade.name().equals(ordertype)){//交易
					
					result.setTradePo(tradeQueryManager.getOneBySn(bill.getSn()));
				}
				
				result.setType(ordertype);
			}
			
			
		}catch(Exception e){
			this.logger.error("支付失败",e);
//			paymentResult.setResult(0);
//			paymentResult.setError(e.getMessage());
			
		}		
		
		return result;
	}
	
	private String[] getPluginid(String url) {
		String pluginid = null;
		String ordertype= null;
		String[] params = new String[2];
		String pattern = ".*/(\\w+)_(\\w+)_(payment-wap-result).html(.*)";
		Pattern p = Pattern.compile(pattern, 2 | Pattern.DOTALL);
		Matcher m = p.matcher(url);
		if (m.find()) {
			ordertype = m.replaceAll("$1");
			pluginid = m.replaceAll("$2");
			params[0]=ordertype;
			params[1]=pluginid;
			return params;
		} else {
			return null;
		}
	}
}
