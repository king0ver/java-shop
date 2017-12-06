package com.enation.app.shop.trade.model.enums;


/**
 * 订单的操作方式
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月19日下午10:43:06
 */
public enum OrderOperate {
	
	
	confirm("确认"),pay("支付"),ship("发货"),rog("确认收货"),cancel("取消"),comment("评论"),complete("完成");
	
	private String description;

	OrderOperate(String _description){
		  this.description=_description;
	}
	
	public String description(){
		return this.description;
	}
	
}
