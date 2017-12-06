package com.enation.app.shop.trade.model.vo;



/**
 * 配送方式
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月31日下午12:51:56
 */
public class ShippingType {

	private int type_id;
	private String type_name;
	private double type_price;

	private int is_used;
	
	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public double getType_price() {
		return type_price;
	}

	public void setType_price(double type_price) {
		this.type_price = type_price;
	}
	
	

	public int getIs_used() {
		return is_used;
	}

	public void setIs_used(int is_used) {
		this.is_used = is_used;
	}

	public ShippingType(){

	}

	public void finalize() throws Throwable {

	}

}