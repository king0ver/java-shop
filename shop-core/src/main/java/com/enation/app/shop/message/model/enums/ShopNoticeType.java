package com.enation.app.shop.message.model.enums;

/**
 * 店铺消息类型
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月13日 下午3:23:30
 */
public enum ShopNoticeType {

	ORDER("订单"),
	GOODS("商品"),
	AFTERSALE("退换货");
	
	
	private String description;

	ShopNoticeType(String _description){
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
