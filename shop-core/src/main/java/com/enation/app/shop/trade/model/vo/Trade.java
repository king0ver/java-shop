package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 交易
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月23日上午11:11:57
 */
public class Trade implements Serializable {
 
	private static final long serialVersionUID = 4047798487807152455L;
	
	private String trade_sn;
	private int member_id;
	private String member_name;
	private String payment_type;
	private PriceDetail price_detail;
	private Consignee consignee;
	private List<Coupon> couponList;
	private List<Order> orderList;
	private List<Gift> giftList;
	
	public String getTrade_sn() {
		return trade_sn;
	}

	public void setTrade_sn(String trade_no) {
		this.trade_sn = trade_no;
	}

 

	public PriceDetail getPrice_detail() {
		return price_detail;
	}

	public void setPrice_detail(PriceDetail price_detail) {
		this.price_detail = price_detail;
	}


	public Consignee getConsignee() {
		return consignee;
	}

	public void setConsignee(Consignee consignee) {
		this.consignee = consignee;
	}

	public List<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Coupon> couponList) {
		this.couponList = couponList;
	}
	

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public List<Gift> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<Gift> giftList) {
		this.giftList = giftList;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}


	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public Trade(){

	}

	public void finalize() throws Throwable {

	}
	
	

}