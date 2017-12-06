package com.enation.app.shop.trade.model.enums;

/**
 * 订单状态
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月31日下午2:44:54
 */
public enum OrderStatus{
	
	NEW("新订单"),
	INTODB_ERROR("出库失败"),
	CONFIRM("已确认"),
	PAID_OFF("已付款"),
	SHIPPED("已发货"),
	ROG("已收货"),
	COMPLETE("已完成"),
	CANCELLED("已取消"),
	AFTE_SERVICE("售后中");
	
	private String description;

	OrderStatus(String _description){
		  this.description=_description;
		  
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}


	
}
