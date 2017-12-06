package com.enation.app.shop.component.payment.plugin.chinapay;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.aftersale.model.vo.RefundBill;
import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.payment.model.vo.PayBill;
import com.enation.app.shop.payment.model.vo.PaymentResult;
import com.enation.app.shop.payment.service.AbstractPaymentPlugin;
import com.enation.app.shop.payment.service.IPaymentMethodManager;
import com.enation.app.shop.payment.service.IPaymentPlugin;
import com.enation.app.shop.trade.model.enums.TradeType;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

@Component("chinaPay")
@Order(3)
public class ChinapayPlugin extends AbstractPaymentPlugin implements IPaymentPlugin{
	
	@Autowired
	private IPaymentMethodManager paymentMethodManager;
	
	/**
	 * 生成自动提交表单
	 * @param actionUrl
	 * @param paramMap
	 * @return
	 */
	private String generateAutoSubmitForm(String actionUrl, Map<String, String> paramMap) {
		StringBuilder html = new StringBuilder();
		html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
		html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"").append(actionUrl).append("\" method=\"post\">\n");

		for (String key : paramMap.keySet()) {
			html.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + paramMap.get(key) + "\">\n");
		}
		html.append("</form>\n");
		return html.toString();
	}
	
	protected String payBack(String ordertype) {
		Map<String, String> config = this.getConfig();
		String merPath = config.get("merPath");

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String merId = request.getParameter("merid");
		String orderno = request.getParameter("orderno");
		String transdate = request.getParameter("transdate");
		String amount = request.getParameter("amount");
		String currencycode = request.getParameter("currencycode");
		String transtype = request.getParameter("transtype");
		String status = request.getParameter("status");
		String checkvalue = request.getParameter("checkvalue");
		String Priv1 = request.getParameter("Priv1");

		chinapay.PrivateKey key = new chinapay.PrivateKey();
		chinapay.SecureLink t;
		boolean flag = key.buildKey(merId, 0, merPath);
		if (flag == false) {
			//System.out.println("build key error!");
			return "<div>系统设置错误</div>";
		}
		t = new chinapay.SecureLink(key);
		flag = t.verifyTransResponse(merId, orderno, amount, currencycode,
				transdate, transtype, status, checkvalue); // ChkValue为ChinaPay应答传回的域段
		if (flag == false) {
			// 签名验证错误处理
		}

		// 对一段字符串的签名验证
		String MsgBody = merId + orderno +  amount + currencycode + transdate + transtype + Priv1;
		flag = t.verifyAuthToken(MsgBody, checkvalue); // ChkValue2为ChinaPay应答传回的域段
		String ordersn = orderno.substring(2);
		if (flag) {
			// 签名验证错误处理
		} else {
			double payprice = StringUtil.toDouble(Priv1, 0d);
			PaymentResult paymentResult = new PaymentResult();
			paymentResult.setSn(orderno);
			paymentResult.setPay_order_no(currencycode);
			paymentResult.setPay_price(payprice);
			this.paySuccess(paymentResult);
		}

		return "";
	}
	

	@Override
	public String onPay(PayBill bill) {
		Map<String, String> config = this.getConfig();
		String merId = config.get("merId");
		String merPath = config.get("merPath");
		String payUrl = config.get("payUrl");
		
		DecimalFormat df_amount = new DecimalFormat("000000000000");
		
		String ordId = bill.getTrade_sn();
		if(ordId.length()>16) {
			ordId = ordId.substring(0,16);
		} else if(ordId.length()<16){
			String zero = "";
			for(int i=0;i<16-ordId.length();i++) {
				zero = zero + "0";
			}
			ordId = zero + ordId;
		}
		
		String amount = df_amount.format((int)(bill.getOrder_price() * 100));
		String CuryId = "156";
		String TransDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String TransType = "0001";
		String Priv1 = "memo";
		
		chinapay.PrivateKey key = new chinapay.PrivateKey();

		boolean flag = key.buildKey(merId, 0, merPath);
		if (flag == false) {
			//System.out.println("build key error!");
			return "<div>系统设置错误</div>";
		}
		chinapay.SecureLink t = new chinapay.SecureLink(key);
		String MsgBody = merId + ordId +  amount + CuryId + TransDate + TransType + Priv1;

		String ChkValue = t.Sign(MsgBody);

		Map<String,String> param = new HashMap<String,String>();
		param.put("MerId", merId);
		param.put("OrdId",  ordId);
		param.put("TransAmt", amount);
		param.put("CuryId", CuryId);
		param.put("TransDate", TransDate);
		param.put("TransType", TransType);
		param.put("Version", "20070129");
		param.put("BgRetUrl", this.getCallBackUrl(bill.getTradeType()));
		param.put("PageRetUrl", this.getReturnUrl(bill));
		param.put("Priv1", Priv1);
		param.put("ChkValue", ChkValue);
		String html = "<div style='margin:50px auto;width:500px'>正在跳转到银联在线支付平台，请稍等...</div>";
		html = html + generateAutoSubmitForm(payUrl,param);//跳转到银联页面支付
		
		return "";
	}

	@Override
	public String onReturn(TradeType tradeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String onCallback(TradeType tradeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean returnPay(RefundBill bill) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RefundPartVo queryRefundStatus(RefundPartVo refund) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPluginId() {
		return "chinaPay";
	}

	@Override
	public String getPluginName() {
		return "银联在线支付";
	}

	@Override
	public List<ConfigItem> definitionConfigItem() {
		List<ConfigItem> list = new ArrayList<>();
		ConfigItem seller_merIdItem = new ConfigItem();
		seller_merIdItem.setName("merId");
		seller_merIdItem.setText("商户代码");
		ConfigItem seller_payUrlItem = new ConfigItem();
		seller_payUrlItem.setName("payUrl");
		seller_payUrlItem.setText("支付接口地址");
		ConfigItem seller_merPathItem = new ConfigItem();
		seller_merPathItem.setName("merchant_private_key");
		seller_merPathItem.setText("商户密钥存放位置");
		list.add(seller_merIdItem);
		list.add(seller_payUrlItem);
		list.add(seller_merPathItem);
		return list;
	}

	@Override
	public Integer getIsRetrace() {
		return 0;
	}

}
