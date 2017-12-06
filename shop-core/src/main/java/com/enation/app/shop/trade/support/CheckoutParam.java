package com.enation.app.shop.trade.support;

import java.io.Serializable;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.Receipt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 结算参数
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月20日下午1:10:11
 */
@ApiModel( description = "结算参数")
public class CheckoutParam  implements Serializable{

	private static final long serialVersionUID = 3648981161644514030L;
	
	@ApiModelProperty(value = "收货地址id" )
	private Integer addressId;
	
	@ApiModelProperty(value = "支付方式" )
	private PaymentType paymentType;

	@ApiModelProperty(value = "发票信息" )
	private Receipt receipt;
	
	
	@ApiModelProperty(value = "收货时间" )
	private String receive_time;
	
	@ApiModelProperty(value = "订单备注" )
	private String remark;
	
	@ApiModelProperty(value = "客户端类型" )
	private String client_type;
	
	
	public CheckoutParam(){

	}


	public Receipt getReceipt() {
		return receipt;
	}




	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}




	public PaymentType getPaymentType() {
		return paymentType;
	}




	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}




	public Integer getAddressId() {
		return addressId;
	}


	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

 

	public String getReceive_time() {
		return receive_time;
	}


	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}
	


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getClient_type() {
		if(client_type==null){
			client_type = ClientType.PC.name();
		}
		return client_type;
	}


	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}


	public void finalize() throws Throwable {

	}

}