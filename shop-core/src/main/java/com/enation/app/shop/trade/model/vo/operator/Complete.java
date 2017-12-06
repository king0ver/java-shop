package com.enation.app.shop.trade.model.vo.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单完成vo
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月20日下午4:41:39
 */
@ApiModel(description = "订单完成")
public class Complete {
	
	@ApiModelProperty(value = "订单编号" )
	private String order_sn;
	

	@ApiModelProperty(hidden=true )
	private String operator;

	public Complete() {
		
	}

	public Complete(String order_sn, String operator) {
		super();
		this.order_sn = order_sn;
		this.operator = operator;
	}


	public String getOrder_sn() {
		return order_sn;
	}


	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	
	
}
