package com.enation.app.shop.aftersale.model.vo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退款申请
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月14日下午4:19:50
 */
// TODO 需要校验
@ApiModel(description = "退款/退货申请")
public class BuyerRefundApply {

	@ApiModelProperty(value = "订单编号")
	@NotBlank(message = "必须提供订单编号")
	private String order_sn;

	@ApiModelProperty(value = "退款金额")
	@NotNull(message = "必须输入退款金额")
	@Valid
	private Double refund_price;
	@ApiModelProperty(value = "退还积分")
	private Integer refund_point;
	@ApiModelProperty(value = "退款方式", example = "原路退回   original , 线下打款    offline")
	private String refund_way;
	@ApiModelProperty(value = "skuid")
	private Integer sku_id;
	@ApiModelProperty(value = "退货数量")
	private Integer return_num;
	@ApiModelProperty(value = "账号类型", example = "alipayDirectPlugin 支付宝 ,   weixinPayPlugin 微信  , 银行转账   银行转账")
	private String account_type;

	@ApiModelProperty(value = "退款账号")
	private String return_account;

	@ApiModelProperty(value = "客户备注")
	private String customer_remark;

	@ApiModelProperty(value = "退款原因")
	private String refund_reason;

	@ApiModelProperty(value = "银行名称")
	private String bank_name;

	@ApiModelProperty(value = "银行账号")
	private String bank_account_number;

	@ApiModelProperty(value = "银行开户名")
	private String bank_account_name;

	@ApiModelProperty(value = "银行开户行")
	private String bank_deposit_name;
	@ApiModelProperty(value = "退款/退货")
	private String refuseType;

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public Double getRefund_price() {
		return refund_price;
	}

	public void setRefund_price(Double refund_price) {
		this.refund_price = refund_price;
	}

	public String getRefund_way() {
		return refund_way;
	}

	public void setRefund_way(String refund_way) {
		this.refund_way = refund_way;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getReturn_account() {
		return return_account;
	}

	public void setReturn_account(String return_account) {
		this.return_account = return_account;
	}

	public String getCustomer_remark() {
		return customer_remark;
	}

	public void setCustomer_remark(String customer_remark) {
		this.customer_remark = customer_remark;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_account_number() {
		return bank_account_number;
	}

	public void setBank_account_number(String bank_account_number) {
		this.bank_account_number = bank_account_number;
	}

	public String getBank_account_name() {
		return bank_account_name;
	}

	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}

	public String getBank_deposit_name() {
		return bank_deposit_name;
	}

	public void setBank_deposit_name(String bank_deposit_name) {
		this.bank_deposit_name = bank_deposit_name;
	}

	public Integer getRefund_point() {
		return refund_point;
	}

	public void setRefund_point(Integer refund_point) {
		this.refund_point = refund_point;
	}

	public String getRefuseType() {
		return refuseType;
	}

	public void setRefuseType(String refuseType) {
		this.refuseType = refuseType;
	}

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public Integer getReturn_num() {
		return return_num;
	}

	public void setReturn_num(Integer return_num) {
		this.return_num = return_num;
	}

}
