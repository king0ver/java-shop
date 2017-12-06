package com.enation.app.shop.aftersale.model.po;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 退货（款）日志
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月26日下午2:06:21
 */
public class RefundLog {

	private Integer id;
	private String refund_sn;
	private Long logtime;
	private String logdetail;
	private String operator;

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRefund_sn() {
		return refund_sn;
	}

	public void setRefund_sn(String refund_sn) {
		this.refund_sn = refund_sn;
	}

	public Long getLogtime() {
		return logtime;
	}

	public void setLogtime(Long logtime) {
		this.logtime = logtime;
	}

	public String getLogdetail() {
		return logdetail;
	}

	public void setLogdetail(String logdetail) {
		this.logdetail = logdetail;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
