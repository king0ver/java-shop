package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 优惠券
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月22日下午1:03:52
 */
@ApiModel( description = "优惠券")
public class Coupon implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5236361119763282449L;

	@ApiModelProperty(value = "优惠卷id" )
	private int coupon_id;
	
	@ApiModelProperty(value = "卖家id" )
	private int seller_id;
	
	@ApiModelProperty(value = "金额" )
	private double amount;
	
	@ApiModelProperty(value = "有效期" )
	private String end_time;
	
	@ApiModelProperty(value = "使用条件" )
	private String use_term;

	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getUse_term() {
		return use_term;
	}

	public void setUse_term(String use_term) {
		this.use_term = use_term;
	}

	
	public int getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(int coupon_id) {
		this.coupon_id = coupon_id;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public Coupon(){

	}

	public void finalize() throws Throwable {

	}

}