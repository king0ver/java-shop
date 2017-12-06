package com.enation.app.shop.trade.model.vo;


/**
 * 支付类型
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月28日上午10:37:46
 */
public enum PaymentType {
	
	online("在线支付"),   
	offline("线下支付"), 
	cod("货到付款"); 
	
	private String description;

	
	PaymentType(String _description){
		  this.description=_description;
	}
	
	public static PaymentType defaultType(){
		
		return online;
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
	
	
}
