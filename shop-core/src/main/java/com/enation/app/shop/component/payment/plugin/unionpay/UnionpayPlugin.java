package com.enation.app.shop.component.payment.plugin.unionpay;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.aftersale.model.vo.RefundBill;
import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.payment.model.vo.PayBill;
import com.enation.app.shop.payment.service.IPaymentPlugin;
import com.enation.app.shop.trade.model.enums.TradeType;
 
 
/**
 * 中国银联在线支付
 * @author xulipeng
 *	2015年09月03日13:29:34
 *
 * v2.0 ：重构，采用界面化配置参数，并将sdk源码引入，去掉jar的依赖
 * by kingapex 2017年01月22日 13:29:34
 */
@Component("unPay")
public class UnionpayPlugin implements IPaymentPlugin{
	
	public static String encoding = "UTF-8";
	
	/**
	 * 5.0.0
	 */
	public static String version = "5.0.0";
	
	private static int is_load=0;
	

	@Override
	public String onPay(PayBill bill) {
		// TODO Auto-generated method stub
		return null;
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
		return "unPay";
	}

	@Override
	public String getPluginName() {
		return "中国银联支付";
	}

	@Override
	public List<ConfigItem> definitionConfigItem() {
		List<ConfigItem> list = new ArrayList<>();
		ConfigItem seller_merIdItem = new ConfigItem();
		seller_merIdItem.setName("merId");
		seller_merIdItem.setText("中国银联商户代码");
		ConfigItem seller_signCertItem = new ConfigItem();
		seller_signCertItem.setName("signCert");
		seller_signCertItem.setText("签名证书路径");
		ConfigItem seller_pwdItem = new ConfigItem();
		seller_pwdItem.setName("pwd");
		seller_pwdItem.setText("签名证书密");
		ConfigItem seller_validateCertItem = new ConfigItem();
		seller_validateCertItem.setName("validateCert");
		seller_validateCertItem.setText("验证签名证书目录");
		ConfigItem seller_encryptCertItem = new ConfigItem();
		seller_encryptCertItem.setName("encryptCert");
		seller_encryptCertItem.setText("敏感信息加密证书路径");
		list.add(seller_merIdItem);
		list.add(seller_signCertItem);
		list.add(seller_pwdItem);
		list.add(seller_validateCertItem);
		list.add(seller_encryptCertItem);
		return list;
	}

	@Override
	public Integer getIsRetrace() {
		return 0;
	}
}
