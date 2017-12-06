package com.enation.app.shop.trade.service;

import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.Receipt;
import com.enation.app.shop.trade.support.CheckoutParam;


/**
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月20日下午2:03:07
 */
public interface ICheckoutParamManager {

	/**
	 * 获取订单的创建参数<br>
	 * 如果没有设置过参数，则用默认
	 * @return 结算参数
	 */
	public CheckoutParam getParam();

	
	/**
	 * 设置收货地址id
	 * @param shippingId    收货地址id
	 */	
	public void setAddressId(Integer addressId);
	
	
	/**
	 * 设置支付式
	 * @param paymentType 支付方式
	 */
	public void setPaymentType(PaymentType paymentType);
	
	
	/**
	 * 设置发票
	 * @param receipt 发票vo {@link  Receipt }
	 * 
	 */
	public void setReceipt(Receipt receipt);

	
	/**
	 * 设置送货时间
	 * @param 送货时间
	 */
	public void setReceiveTime(String receiveTime);
	
	
	/**
	 * 设置订单备注
	 * @param 送货时间
	 */
	public void setRemark(String remark);
	
	/**
	 * 设置订单来源
	 * @param 送货时间
	 */
	public void setClientType(String clientType);
	
	
	/**
	 * 批量设置所有参数
	 * @param param
	 */
	public void setAll(CheckoutParam param);
}