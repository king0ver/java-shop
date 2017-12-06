package com.enation.app.shop.trade.model.vo.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 确认收货vo
 * @author kingapex
 * @version v1.0.0
 * @since v1.0.0
 * 2017年5月16日 下午8:29:46
 */
@ApiModel(description = "确认收货")
public class Rog {
	
	@ApiModelProperty(value = "订单编号" )
	private String order_sn;

	@ApiModelProperty(value = "操作者" )
	private String operator;

	public Rog() {
		
	}
	
	public Rog(String order_sn, String operator) {
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
