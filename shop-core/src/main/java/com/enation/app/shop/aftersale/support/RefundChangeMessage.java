package com.enation.app.shop.aftersale.support;

import java.io.Serializable;

import com.enation.app.shop.aftersale.model.po.Refund;

public class RefundChangeMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5608209655474949712L;
	public final static int ADMIN_AUTH = 3;// 管理员审核
	public final static int APPLY = 1;// 申请
	public final static int AUTH = 2;// 卖家审核
	private Refund refund;
	public final static int SELLER_IN_STOCK = 4;// 卖家审核入库
	private Integer operation_type;// 操作类型

	public RefundChangeMessage(Refund refund, Integer operation_type) {
		super();
		this.refund = refund;
		this.operation_type = operation_type;
	}

	public Refund getRefund() {
		return refund;
	}

	public void setRefund(Refund refund) {
		this.refund = refund;
	}

	public Integer getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(Integer operation_type) {
		this.operation_type = operation_type;
	}
}
