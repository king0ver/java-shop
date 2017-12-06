package com.enation.app.shop.trade.model.vo.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单取消vo
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月19日下午10:23:34
 */
@ApiModel(description = "订单取消")
public class Cancel {
	
	@ApiModelProperty(value = "订单编号" )
	private String order_sn;
	

	@ApiModelProperty(value = "取消原因" )
	private String reson;


	@ApiModelProperty(hidden=true )
	private String operator;
	
	
	public Cancel() {
		
	}
	
	public Cancel(String order_sn, String reson, String operator) {
		super();
		this.order_sn = order_sn;
		this.reson = reson;
		this.operator = operator;
	}


	public String getOrder_sn() {
		return order_sn;
	}


	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}


	public String getReson() {
		return reson;
	}


	public void setReson(String reson) {
		this.reson = reson;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	
	
}
