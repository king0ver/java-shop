package com.enation.app.shop.trade.model.po;

import java.io.Serializable;

/**
 * 各状态下订单数量
 * @author fk
 * @version 1.0
 * @since pangu1.0
 * 2017年7月4日21:24:59
 */
public class OrderCount implements Serializable{

	private static final long serialVersionUID = -8404793906287358808L;
	private Integer id;
	private Integer seller_id;
	private String order_status;
	private Integer count;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
