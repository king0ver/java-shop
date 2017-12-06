package com.enation.app.shop.payment.service;

import com.enation.app.shop.payment.model.po.PaymentBill;

/**
 * 支付流水manager
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月30日 上午11:15:27
 */
public interface IPaymentBillManager {

	/**
	 * 添加流水
	 * @param paymentStream
	 * @return
	 */
	public PaymentBill add(PaymentBill paymentStream);

	/**
	 * 使用支付编号查询某个流水
	 * @param pay_key
	 * @return
	 */
	public PaymentBill getByPayKey(String pay_key);

	/**
	 * 更新支付流水中的交易流水号
	 * @param paymentStream
	 */
	public void update(PaymentBill paymentStream);

	/**
	 * 使用sn查询某个流水
	 * @param sn
	 * @return
	 */
	public PaymentBill getBySn(String sn);

}
