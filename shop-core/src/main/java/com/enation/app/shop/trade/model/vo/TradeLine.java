package com.enation.app.shop.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 交易列表项
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月30日下午8:55:20
 */
public class TradeLine implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7052632903191042756L;
	
	private int trade_id;
	private String  trade_sn;
	private String  trade_status; //交易状态
	
	private int member_id;
	private int payment_method_id;
	private String payment_plugin_id;
	private String payment_method_name;
	private String payment_type;
	private double total_price;
	private double goods_price;
	private double freight_price;
	private double discount_price;
	
	private int consignee_id;
	private String consignee_name;
	private String consignee_county;
	private String consignee_province;
	private String consignee_city;
	private String consignee_town;
	private String consignee_address;
	private String consignee_mobile;
	private String consignee_telephone;
	private long create_time;

	
	@JsonRawValue
	private String order_json;
	
	private List<OrderLine> orderDetailList;

	public int getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(int trade_id) {
		this.trade_id = trade_id;
	}

	public String getTrade_sn() {
		return trade_sn;
	}

	public void setTrade_sn(String trade_sn) {
		this.trade_sn = trade_sn;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public int getPayment_method_id() {
		return payment_method_id;
	}

	public void setPayment_method_id(int payment_method_id) {
		this.payment_method_id = payment_method_id;
	}

	public String getPayment_plugin_id() {
		return payment_plugin_id;
	}

	public void setPayment_plugin_id(String payment_plugin_id) {
		this.payment_plugin_id = payment_plugin_id;
	}

	public String getPayment_method_name() {
		return payment_method_name;
	}

	public void setPayment_method_name(String payment_method_name) {
		this.payment_method_name = payment_method_name;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public double getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}

	public double getFreight_price() {
		return freight_price;
	}

	public void setFreight_price(double freight_price) {
		this.freight_price = freight_price;
	}

	public double getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(double discount_price) {
		this.discount_price = discount_price;
	}

	public int getConsignee_id() {
		return consignee_id;
	}

	public void setConsignee_id(int consignee_id) {
		this.consignee_id = consignee_id;
	}

	public String getConsignee_name() {
		return consignee_name;
	}

	public void setConsignee_name(String consignee_name) {
		this.consignee_name = consignee_name;
	}

	public String getConsignee_county() {
		return consignee_county;
	}

	public void setConsignee_county(String consignee_county) {
		this.consignee_county = consignee_county;
	}

	public String getConsignee_province() {
		return consignee_province;
	}

	public void setConsignee_province(String consignee_province) {
		this.consignee_province = consignee_province;
	}

	public String getConsignee_city() {
		return consignee_city;
	}

	public void setConsignee_city(String consignee_city) {
		this.consignee_city = consignee_city;
	}

	public String getConsignee_town() {
		return consignee_town;
	}

	public void setConsignee_town(String consignee_town) {
		this.consignee_town = consignee_town;
	}

	public String getConsignee_address() {
		return consignee_address;
	}

	public void setConsignee_address(String consignee_address) {
		this.consignee_address = consignee_address;
	}

	public String getConsignee_mobile() {
		return consignee_mobile;
	}

	public void setConsignee_mobile(String consignee_mobile) {
		this.consignee_mobile = consignee_mobile;
	}

	public String getConsignee_telephone() {
		return consignee_telephone;
	}

	public void setConsignee_telephone(String consignee_telephone) {
		this.consignee_telephone = consignee_telephone;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getOrder_json() {
		return order_json;
	}

	public void setOrder_json(String order_json) {
		this.order_json = order_json;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public List<OrderLine> getOrderDetailList() {
		Gson gson = new Gson();
		this.orderDetailList = gson.fromJson(this.getOrder_json(),  new TypeToken< List<OrderLine> >() {  }.getType());	 
		
		return orderDetailList;
	}

	public void setOrderDetailList(List<OrderLine> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}

	
}
