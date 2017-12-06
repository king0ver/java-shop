package com.enation.app.shop.trade.model.enums;

/**
 * 申请售后的状态
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月6日 下午4:16:53
 */
public enum ServiceStatus {

	NOT_APPLY("未申请"),
	APPLY("已申请"),
	EXPIRED("已失效");
	
	private String description;

	ServiceStatus(String _description){
		this.description=_description;
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
}
