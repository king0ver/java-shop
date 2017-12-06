package com.enation.app.shop.aftersale.model.enums;


/**
 * 退货类型
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月24日下午4:44:35
 */
public enum RefuseType {
	
	return_money("退款"),
	return_goods("退货");
	
	private String description;
	
	RefuseType(String _description){
		this.description=_description;
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
	
}
