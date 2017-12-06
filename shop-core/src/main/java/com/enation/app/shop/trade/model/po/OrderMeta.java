package com.enation.app.shop.trade.model.po;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单扩展信息
 * 
 * @author kingapex
 * 
 */
public class OrderMeta {
	private Integer metaid;
	private String order_sn;
	private String meta_key;
	private String meta_value;
	
	@ApiModelProperty(value = "售后状态")
	private String status;

	@PrimaryKeyField
	public Integer getMetaid() {
		return metaid;
	}

	public void setMetaid(Integer metaid) {
		this.metaid = metaid;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getMeta_key() {
		return meta_key;
	}

	public void setMeta_key(String meta_key) {
		this.meta_key = meta_key;
	}

	public String getMeta_value() {
		return meta_value;
	}

	public void setMeta_value(String meta_value) {
		this.meta_value = meta_value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
