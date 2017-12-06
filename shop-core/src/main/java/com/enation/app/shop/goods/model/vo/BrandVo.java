package com.enation.app.shop.goods.model.vo;

/**
 * 品牌vo
 * @author fk
 * @version v1.0
 * 2017年4月6日 上午11:37:37
 */
public class BrandVo {
	/**
	 * 品牌id
	 */
	private Integer brand_id;
	/**
	 * 品牌名称
	 */
	private String name;
	/**
	 * 品牌 logo
	 */
	private String logo;
	
	
	public Integer getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
}
