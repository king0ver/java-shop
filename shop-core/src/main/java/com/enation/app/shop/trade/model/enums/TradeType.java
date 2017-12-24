package com.enation.app.shop.trade.model.enums;

/**
 * 交易类型
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月5日下午5:12:55
 */
public enum TradeType {
	
	order("订单"),
	trade("交易"),
	recharge("充值");
	
	private String description;

	TradeType(String _description){
		  this.description=_description;
	}
}
