package com.enation.app.shop.shop.apply.model.po;

import java.io.Serializable;

/**
 * 
 * (店铺) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月14日 下午3:09:54
 */
public class Shop implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2913901493111934067L;
	/**店铺Id*/
	private Integer shop_id;	
	/**会员Id*/
	private Integer  member_id;	
	/**会员名称*/
	private String  member_name;	
	/**店铺名称*/
	private String shop_name;	
	/**店铺状态*/
	private String shop_disable;	 /**-1 未通过审核  0 待审核 1 已审核 2 已关闭*/
	/**店铺创建时间*/
	private Long  shop_createtime;	
	/**店铺关闭时间*/
	private Long  shop_endtime;	
	

	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public String getShop_disable() {
		return shop_disable;
	}
	public void setShop_disable(String shop_disable) {
		this.shop_disable = shop_disable;
	}
	public Long getShop_createtime() {
		return shop_createtime;
	}
	public void setShop_createtime(Long shop_createtime) {
		this.shop_createtime = shop_createtime;
	}
	public Long getShop_endtime() {
		return shop_endtime;
	}
	public void setShop_endtime(Long shop_endtime) {
		this.shop_endtime = shop_endtime;
	}
	
	
}
