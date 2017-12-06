package com.enation.app.shop.trade.model.enums;

/**
 * 订单前端状态
 * @author fk
 * @version 1.0
 * @since pangu1.0
 * 2017年7月4日21:29:16
 */
public enum OrderFrontStatus {

	wait_ship("待发货"),
	wait_pay("待付款"),
	wait_rog("待收货"),
	wait_comment("待评论"),
	refund("售后中"),
	cancel("已取消"),
	complete("已完成");
	
	private String description;
	
	OrderFrontStatus(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
	
}
