package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;


import io.swagger.annotations.ApiModelProperty;

/**
 * 订单收货人vo
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月5日 下午3:17:14
 */
public class OrderConsigneeVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8235135936585521903L;

	@ApiModelProperty(value = "订单号")
	private String sn;
	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String ship_name;
	@ApiModelProperty(value = "订单备注")
	private String remark;

	/**
	 * 收货地址
	 */
	@ApiModelProperty(value = "会员id")
	private String ship_addr;


	/**
	 * 收货人手机号
	 */
	@ApiModelProperty(value = "收货人手机号")
	private String ship_mobile;

	/**
	 * 收货人电
	 */
	@ApiModelProperty(value = "会员id")
	private String ship_tel;

	/**
	 * 送货时间
	 */
	@ApiModelProperty(value = "送货时间")
	private String receive_time;
	
	/**
	 * 收货地址省Id
	 */
	@ApiModelProperty(value = "收货地址省Id")
	private Integer province_id;

	/**
	 * 收货地址市Id
	 */
	@ApiModelProperty(value = "收货地址市Id")
	private Integer city_id;

	/**
	 * 收货地址区Id
	 */
	@ApiModelProperty(value = "收货地址区Id")
	private Integer region_id;

	/**
	 * 收货地址街道Id
	 */
	@ApiModelProperty(value = "收货地址街道Id")
	private Integer town_id;

	/**
	 * 收货省
	 */
	@ApiModelProperty(value = "收货省")
	private String province;

	/**
	 * 收货地址市
	 */
	@ApiModelProperty(value = "收货地址市")
	private String city;

	/**
	 * 收货地址区
	 */
	@ApiModelProperty(value = "收货地址区")
	private String region;

	/**
	 * 收货地址街道
	 */
	@ApiModelProperty(value = "收货地址街道")
	private String town;
	

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getShip_name() {
		return ship_name;
	}

	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}

	public String getShip_addr() {
		return ship_addr;
	}

	public void setShip_addr(String ship_addr) {
		this.ship_addr = ship_addr;
	}

	public String getShip_mobile() {
		return ship_mobile;
	}

	public void setShip_mobile(String ship_mobile) {
		this.ship_mobile = ship_mobile;
	}

	public String getShip_tel() {
		return ship_tel;
	}

	public void setShip_tel(String ship_tel) {
		this.ship_tel = ship_tel;
	}

	public String getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}

	public Integer getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Integer province_id) {
		this.province_id = province_id;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public Integer getTown_id() {
		return town_id;
	}

	public void setTown_id(Integer town_id) {
		this.town_id = town_id;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
