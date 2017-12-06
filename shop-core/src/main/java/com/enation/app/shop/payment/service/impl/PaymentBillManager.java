package com.enation.app.shop.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.payment.model.po.PaymentBill;
import com.enation.app.shop.payment.service.IPaymentBillManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 支付流水manager
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月30日 上午11:15:58
 */
@Service
public class PaymentBillManager implements IPaymentBillManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public PaymentBill add(PaymentBill paymentStream) {
		
		this.daoSupport.insert("es_payment_bill", paymentStream);
		
		return paymentStream;
	}

	@Override
	public PaymentBill getByPayKey(String pay_key) {
		
		String sql = "select * from es_payment_bill where pay_key = ? ";
		
		return this.daoSupport.queryForObject(sql, PaymentBill.class, pay_key);
	}

	@Override
	public void update(PaymentBill paymentStream) {
		
		this.daoSupport.update("es_payment_bill", paymentStream, "id="+paymentStream.getId());
	}

	@Override
	public PaymentBill getBySn(String sn) {

		String sql = "select * from es_payment_bill where sn = ? order by id desc limit 0,1";
		
		return this.daoSupport.queryForObject(sql, PaymentBill.class, sn);
	}

}
