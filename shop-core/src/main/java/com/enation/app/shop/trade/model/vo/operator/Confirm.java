package com.enation.app.shop.trade.model.vo.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单确认 Vo
 * @author kingapex
 * @version v1.0.0
 * @since v1.0.0
 * 2017年5月16日 上午11:31:05
 */
@ApiModel
public class Confirm {
	
	@ApiModelProperty(value = "订单编号" )
	private String order_sn;
	
	@ApiModelProperty(value = "是否同意确认此订单" )
	private boolean is_agree;
	
	@ApiModelProperty(value = "操作者" )
	private String operator;
	

	public boolean isIs_agree() {
		return is_agree;
	}

	public void setIs_agree(boolean is_agree) {
		this.is_agree = is_agree;
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
