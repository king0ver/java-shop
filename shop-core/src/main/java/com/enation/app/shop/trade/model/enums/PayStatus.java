package com.enation.app.shop.trade.model.enums;

/**
 * 订单状态
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月31日下午2:44:54
 */
public enum PayStatus{
	
	PAY_NO("新订单"),
	PAY_PARTIAL("部分支付"),
	PAY_YES("已付款");
	
 
	
	private String description;

	PayStatus(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
 
	
}
