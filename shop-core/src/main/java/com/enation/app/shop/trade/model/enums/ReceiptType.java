package com.enation.app.shop.trade.model.enums;


/**
 * 发票类型
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月20日 下午3:39:00
 */
public enum ReceiptType {
	
	PERSON("个人"),
	COMPANY("单位");
	
	private String description;

	ReceiptType(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
 
}
