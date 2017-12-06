package com.enation.app.shop.trade.model.vo.operator;

import io.swagger.annotations.ApiModelProperty;

/**
 * 交易取消vo
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月29日 上午11:10:18
 */
public class TradeCancel {

	@ApiModelProperty(value = "交易编号" )
	private String trade_sn;
	

	@ApiModelProperty(value = "取消原因" )
	private String reason;


	@ApiModelProperty(hidden=true )
	private String operator;


	public String getTrade_sn() {
		return trade_sn;
	}


	public void setTrade_sn(String trade_sn) {
		this.trade_sn = trade_sn;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
