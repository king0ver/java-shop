package com.enation.app.shop.aftersale.model.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 卖家退货（款）批准
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月26日下午1:51:36
 */
public class SellerRefundApproval {

	@ApiModelProperty(value = "退款单号")
	private String sn;

	@ApiModelProperty(value = "是否同意退款")
	private boolean agree;

	@ApiModelProperty(value = "退款金额")
	private Double refund_price;

	@ApiModelProperty(value = "退款备注")
	private String remark;
	@ApiModelProperty(value = "退还积分")
	private Integer refund_point;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRefund_point() {
		return refund_point;
	}

	public void setRefund_point(Integer refund_point) {
		this.refund_point = refund_point;
	}

	public Double getRefund_price() {
		return refund_price;
	}

	public void setRefund_price(Double refund_price) {
		this.refund_price = refund_price;
	}

}
