package com.enation.app.shop.waybill.model;


/**
 * 
 * 电子面单
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月9日 下午6:35:30
 */
public class WayBill implements java.io.Serializable{
	/** 电子面单id */
	private Integer id; 
	/** 名称*/
	private String bill_name; 
	/** 是否开启0为未开启 1为已开启 */
	private Integer is_open; 
	/** 参数 */
	private String bill_config;  
	/** 插件beanid */
	private String bill_bean; 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBill_name() {
		return bill_name;
	}
	public void setBill_name(String bill_name) {
		this.bill_name = bill_name;
	}
	public Integer getIs_open() {
		return is_open;
	}
	public void setIs_open(Integer is_open) {
		this.is_open = is_open;
	}
	public String getBill_config() {
		return bill_config;
	}
	public void setBill_config(String bill_config) {
		this.bill_config = bill_config;
	}
	public String getBill_bean() {
		return bill_bean;
	}
	public void setBill_bean(String bill_bean) {
		this.bill_bean = bill_bean;
	}
	
	
	
	
	
	
}
