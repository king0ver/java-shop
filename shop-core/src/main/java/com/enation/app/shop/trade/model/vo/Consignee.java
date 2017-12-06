package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

/**
 * 收货人实体
 * @author kingapex
 * @version 1.0
 * @created 2017年08月03日14:39:25
 */
public class Consignee implements Serializable{

	private static final long serialVersionUID = 2499675140677613044L;
	private int consignee_id;
	private String name;
	private String county;
	
	private String province;
	private String city;
	private String town;
	private String address;
	private String mobile;
	private String telephone;
	 
	private int county_id;
	private int province_id;
	private int city_id;
	
	private String district;
	private int district_id;
	private int town_id;

	public int getConsignee_id() {
		return consignee_id;
	}

	public void setConsignee_id(int consignee_id) {
		this.consignee_id = consignee_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getCounty_id() {
		return county_id;
	}

	public void setCounty_id(int county_id) {
		this.county_id = county_id;
	}

	public int getProvince_id() {
		return province_id;
	}

	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public int getTown_id() {
		return town_id;
	}

	public void setTown_id(int town_id) {
		this.town_id = town_id;
	}

	public Consignee(){

	}

	public void finalize() throws Throwable {

	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(int district_id) {
		this.district_id = district_id;
	}

}