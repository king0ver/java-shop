package com.enation.app.shop.trade.model.enums;

/**
 * 订单状态
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月31日下午2:44:54
 */
public enum ShipStatus{
	
	SHIP_NO("未发货"),
	SHIP_YES("已发货"),
	SHIP_ROG("已收货");
	
	
	private String description;

	ShipStatus(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
 
	
}
