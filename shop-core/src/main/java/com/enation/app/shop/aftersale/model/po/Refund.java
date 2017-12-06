package com.enation.app.shop.aftersale.model.po;

import java.io.Serializable;

import com.enation.app.shop.aftersale.model.enums.RefuseType;
import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

/**
 * 退款
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月14日下午4:06:05
 */
@ApiModel(description = "退货(款)单")
public class Refund implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5163535920930862400L;

	private Integer id;

	@ApiModelProperty(value = "退货(款)单编号")
	private String sn;

	@ApiModelProperty(value = "会员id")
	private Integer member_id;

	@ApiModelProperty(value = "会员姓名")
	private String member_name;

	@ApiModelProperty(value = "卖家id")
	private Integer seller_id;

	@ApiModelProperty(value = "卖家姓名")
	private String seller_name;

	@ApiModelProperty(value = "订单编号")
	private String order_sn;

	@ApiModelProperty(hidden = true)
	private String refund_status;

	@ApiModelProperty(hidden = true)
	private Long create_time;

	@ApiModelProperty(value = "退款金额")
	private Double refund_price;
	@ApiModelProperty(value = "退还积分")
	private Integer refund_point;

	@ApiModelProperty(value = "退款方式")
	private String refund_way;

	@ApiModelProperty(value = "退款账户类型")
	private String account_type;

	@ApiModelProperty(value = "退款账户")
	private String return_account;

	@ApiModelProperty(value = "客户备注")
	private String customer_remark;

	@ApiModelProperty(value = "客服备注")
	private String seller_remark;

	@ApiModelProperty(value = "库管备注")
	private String warehouse_remark;

	@ApiModelProperty(value = "财务备注")
	private String finance_remark;

	@ApiModelProperty(value = "退款原因")
	private String refund_reason;

	@ApiModelProperty(value = "拒绝原因")
	private String refuse_reason;

	@ApiParam(value = "银行名称")
	private String bank_name;

	@ApiParam(value = "银行账号")
	private String bank_account_number;

	@ApiParam(value = "银行开户名")
	private String bank_account_name;

	@ApiParam(value = "银行开户行")
	private String bank_deposit_name;

	@ApiParam(value = "交易编号")
	private String trade_sn;

	/**
	 * 退款类型
	 * 
	 * @see RefuseType
	 */
	@ApiModelProperty(hidden = true)
	private String refuse_type;

	@ApiModelProperty(hidden = true)
	private String pay_order_no;// 支付结果返回的交易号

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
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

	public String getSeller_remark() {
		return seller_remark;
	}

	public void setSeller_remark(String seller_remark) {
		this.seller_remark = seller_remark;
	}

	public String getWarehouse_remark() {
		return warehouse_remark;
	}

	public void setWarehouse_remark(String warehouse_remark) {
		this.warehouse_remark = warehouse_remark;
	}

	public String getFinance_remark() {
		return finance_remark;
	}

	public void setFinance_remark(String finance_remark) {
		this.finance_remark = finance_remark;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public String getRefuse_reason() {
		return refuse_reason;
	}

	public void setRefuse_reason(String refuse_reason) {
		this.refuse_reason = refuse_reason;
	}

	public String getRefuse_type() {
		return refuse_type;
	}

	public void setRefuse_type(String refuse_type) {
		this.refuse_type = refuse_type;
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

	public String getTrade_sn() {
		return trade_sn;
	}

	public void setTrade_sn(String trade_sn) {
		this.trade_sn = trade_sn;
	}

	public String getPay_order_no() {
		return pay_order_no;
	}

	public void setPay_order_no(String pay_order_no) {
		this.pay_order_no = pay_order_no;
	}

	public Integer getRefund_point() {
		return refund_point;
	}

	public void setRefund_point(Integer refund_point) {
		this.refund_point = refund_point;
	}

}
