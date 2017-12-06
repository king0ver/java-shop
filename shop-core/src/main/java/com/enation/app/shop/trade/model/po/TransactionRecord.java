package com.enation.app.shop.trade.model.po;

import java.io.Serializable;

/**
 * 交易记录
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月28日 下午2:21:04
 */
public class TransactionRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -656633339624097355L;

	private Integer record_id;
	private String order_sn;
	private Integer goods_id;
	private Integer goods_num;
	private Long rog_time;
	private String uname;
	private Double price;
	private Integer member_id;
	
	public Integer getRecord_id() {
		return record_id;
	}
	public void setRecord_id(Integer record_id) {
		this.record_id = record_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public Long getRog_time() {
		return rog_time;
	}
	public void setRog_time(Long rog_time) {
		this.rog_time = rog_time;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

}
