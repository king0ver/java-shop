package com.enation.app.shop.trade.model.vo.operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 货运单
 * @author kingapex
 * @version v1.0.0
 * @since v1.0.0
 * 2017年5月16日 下午4:40:49
 */
@ApiModel(description = "货运单")
public class Delivery {
	
	@ApiModelProperty(value = "订单编号" )
	private String order_sn;
	
	@ApiModelProperty(value = "货运单号" )
	private String delivery_no;
	
	@ApiModelProperty(value = "物流公司" )
	private int logi_id;
	
	@ApiModelProperty(value = "物流公司名称" )
	private String logi_name;

	@ApiModelProperty(hidden=true )
	private String operator;
	
	
	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getDelivery_no() {
		return delivery_no;
	}

	public void setDelivery_no(String delivery_no) {
		this.delivery_no = delivery_no;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getLogi_id() {
		return logi_id;
	}

	public void setLogi_id(int logi_id) {
		this.logi_id = logi_id;
	}

	public String getLogi_name() {
		return logi_name;
	}

	public void setLogi_name(String logi_name) {
		this.logi_name = logi_name;
	}
	
}
