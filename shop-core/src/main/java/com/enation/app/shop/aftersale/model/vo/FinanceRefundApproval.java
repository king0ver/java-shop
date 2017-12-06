package com.enation.app.shop.aftersale.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 财务批准（执行）退款
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月27日下午1:01:56
 */
@ApiModel( description = "财务批准（执行）退款")
public class FinanceRefundApproval {
	
	
	@ApiModelProperty(value = "退货(款)单编号" )
	private String sn;
	
	
	@ApiModelProperty(value = "退款金额" )
	private Double refund_price;
	
	@ApiModelProperty(hidden = true)
	private String error_code;
	
	@ApiModelProperty(hidden = true)
	private boolean is_success;
	@ApiModelProperty(value = "退还积分" )
	private Integer refund_point;
	
	
	public String getError_code() {
		return error_code;
	}


	public void setError_code(String error_code) {
		this.error_code = error_code;
	}


	public boolean isIs_success() {
		return is_success;
	}


	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}


	public Double getRefund_price() {
		return refund_price;
	}


	public void setRefund_price(Double refund_price) {
		this.refund_price = refund_price;
	}


	public Integer getRefund_point() {
		return refund_point;
	}


	public void setRefund_point(Integer refund_point) {
		this.refund_point = refund_point;
	}
	
	
	
	
}
