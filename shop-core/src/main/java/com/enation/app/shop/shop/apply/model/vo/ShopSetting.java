package com.enation.app.shop.shop.apply.model.vo;

import javax.validation.constraints.Min;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 
 * (店铺设置信息) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午1:31:50
 */
public class ShopSetting{
	/**店铺id*/
	private Integer shop_id;	
	
	/**店铺所在省id*/
	private Integer shop_province_id;
	
	/**店铺所在市id*/
	private Integer shop_city_id;
	
	/**店铺所在县id*/
	private Integer shop_region_id;
	
	/**店铺所在镇id*/
	private Integer shop_town_id;
	
	/**店铺所在省*/
	private String shop_province;
	
	/**店铺所在市*/
	private String shop_city;
	
	/**店铺所在县*/
	private String shop_region;
	
	/**店铺所在镇*/
	private String shop_town;
	
	/**店铺详细地址*/
	private String shop_add;
	
	/**联系人电话*/
	private String link_phone;
	
	/**店铺logo*/
	private String shop_logo;
	
	/**店铺横幅*/
	private String shop_banner;
	
	/**店铺简介*/
	private String shop_desc;
	
	/**店铺客服qq*/
	private String shop_qq;

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public Integer getShop_province_id() {
		return shop_province_id;
	}

	public void setShop_province_id(Integer shop_province_id) {
		this.shop_province_id = shop_province_id;
	}

	public Integer getShop_city_id() {
		return shop_city_id;
	}

	public void setShop_city_id(Integer shop_city_id) {
		this.shop_city_id = shop_city_id;
	}

	public Integer getShop_region_id() {
		return shop_region_id;
	}

	public void setShop_region_id(Integer shop_region_id) {
		this.shop_region_id = shop_region_id;
	}

	public Integer getShop_town_id() {
		return shop_town_id;
	}

	public void setShop_town_id(Integer shop_town_id) {
		this.shop_town_id = shop_town_id;
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

	public String getShop_add() {
		return shop_add;
	}

	public void setShop_add(String shop_add) {
		this.shop_add = shop_add;
	}

	public String getLink_phone() {
		return link_phone;
	}

	public void setLink_phone(String link_phone) {
		this.link_phone = link_phone;
	}

	public String getShop_logo() {
		return shop_logo;
	}

	public void setShop_logo(String shop_logo) {
		this.shop_logo = shop_logo;
	}

	public String getShop_banner() {
		return shop_banner;
	}

	public void setShop_banner(String shop_banner) {
		this.shop_banner = shop_banner;
	}

	public String getShop_desc() {
		return shop_desc;
	}

	public void setShop_desc(String shop_desc) {
		this.shop_desc = shop_desc;
	}

	public String getShop_qq() {
		return shop_qq;
	}

	public void setShop_qq(String shop_qq) {
		this.shop_qq = shop_qq;
	}
	
	
	
}
