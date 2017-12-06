package com.enation.app.shop.trade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.enums.ReceiptType;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.Receipt;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.service.ICheckoutSession;
import com.enation.app.shop.trade.support.CheckoutParam;
import com.enation.app.shop.trade.support.CheckoutParamName;



/**
 * 结算参数业务类
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月18日上午10:42:08
 */
@Service
public class CheckoutParamManager implements ICheckoutParamManager {
	
 
	@Autowired
	private ICheckoutSession checkoutSession;

 
	public CheckoutParamManager(){
		
	
	}

	public void finalize() throws Throwable {

	}

	@Override
	public CheckoutParam getParam(){
		 
		CheckoutParam  param = this.checkoutSession.read();
		
		//如果session中没有 new一个，并赋给默认值 
		if(param== null ){
			
			param=  new CheckoutParam();
			//默认配送地址
			param.setAddressId( this.getDefaultAddressId() );
			
			//默认支付方式
			param.setPaymentType(  PaymentType.defaultType() );
			
			//默认不需要发票
			Receipt receipt = new Receipt();
			receipt.setNeed_receipt("no");
			param.setReceipt(receipt);
			
			//默认时间
			param.setReceive_time("任意时间");
			
			this.checkoutSession.write( param);
			return param;
		}
		
		return param;
	}

 
	
 
	
	
	@Override
	public void setAddressId(Integer addressId) {
		this.checkoutSession.write(CheckoutParamName.ADDRESSID, addressId);
		
	}
	
	
	@Override
	public void setPaymentType(PaymentType paymentType) {
		this.checkoutSession.write(CheckoutParamName.PAYMENTTYPE, paymentType);
	}
	
	 

	@Override
	public void setReceiveTime(String receiveTime) {
		this.checkoutSession.write(CheckoutParamName.RECEIVETIME, receiveTime);
	}
	
	
	@Override
	public void setReceipt(Receipt receipt) {
		if(receipt.getTitle().equals("个人")){
			receipt.setType(ReceiptType.PERSON.name());
		}else{
			receipt.setType(ReceiptType.COMPANY.name());
		}
		this.checkoutSession.write(CheckoutParamName.RECEIPT, receipt);

	}

	@Override
	public void setRemark(String remark) {
		this.checkoutSession.write(CheckoutParamName.REMART, remark);
	}
	
	@Override
	public void setClientType(String clientType) {
		this.checkoutSession.write(CheckoutParamName.CLIENTTYPE, clientType);
	}

	
	@Override
	public void setAll(CheckoutParam param) {
//		checkoutSession.unlock();
		this.checkoutSession.write(param);
	}
	
	

	private Integer getDefaultAddressId(){
		
		
		//TODO 要调会员服务 获取转变收货地址
		return 0;
	}


}