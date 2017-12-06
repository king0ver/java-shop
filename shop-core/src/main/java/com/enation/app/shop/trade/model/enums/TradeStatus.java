package com.enation.app.shop.trade.model.enums;

public enum TradeStatus {

	NEW("新订单"),
	PAID_OFF("已付款");
	
	private String description;

	TradeStatus(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
 
}
