package com.enation.app.shop.aftersale.model.enums;

public enum RefundWay {
	
	original("原路退回"),
	online("在线支付"),
	offline("线下支付");
	
	private String description;

	RefundWay(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
	
}
