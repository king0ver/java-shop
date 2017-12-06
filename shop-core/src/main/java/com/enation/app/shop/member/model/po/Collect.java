package com.enation.app.shop.member.model.po;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 店铺收藏
 * @author fk
 * @version v1.0
 * 2017年5月16日 下午9:10:29
 */
public class Collect implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2459561462864671586L;
	private Integer id           ;    
	private Integer member_id    ;    
	private Integer shop_id     ;    
	private long create_time  ;       
	private String shop_name    ;     
	private String shop_province;     
	private String shop_city    ;     
	private String shop_region  ;     
	private String shop_town    ;     
	private String attr         ;     
	private String shop_logo    ;
	private Integer goods_num;
	private String shop_tel;
	
	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public String getShop_province() {
		return shop_province;
	}
	public void setShop_province(String shop_province) {
		this.shop_province = shop_province;
	}
	public String getShop_city() {
		return shop_city;
	}
	public void setShop_city(String shop_city) {
		this.shop_city = shop_city;
	}
	public String getShop_region() {
		return shop_region;
	}
	public void setShop_region(String shop_region) {
		this.shop_region = shop_region;
	}
	public String getShop_town() {
		return shop_town;
	}
	public void setShop_town(String shop_town) {
		this.shop_town = shop_town;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getShop_logo() {
		return shop_logo;
	}
	public void setShop_logo(String shop_logo) {
		this.shop_logo = shop_logo;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public String getShop_tel() {
		return shop_tel;
	}
	public void setShop_tel(String shop_tel) {
		this.shop_tel = shop_tel;
	}
}
