package com.enation.app.shop.goods.model.po;


import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 品牌领域模型
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年3月5日 下午1:24:43
 */
@ApiModel
public class Brand implements java.io.Serializable{


	private static final long serialVersionUID = 6148861393956655160L;
	/**
	 * id
	 */
	@ApiModelProperty(hidden=true)
	private Integer brand_id;
	/**
	 * 品牌名称
	 */
	@ApiModelProperty(required=true,value="品牌名称")
	private String name;
	/**
	 * 品牌 logo
	 */
	@ApiModelProperty(required=false,value="品牌logo")
	private String logo;
	/**
	 * 品牌关键字
	 */
	@ApiModelProperty(hidden=true)
	private String keywords;
	/**
	 * 品牌详细说明
	 */
	@ApiModelProperty(required=false,value="品牌详细说明")
	private String brief;
	/**
	 * 品牌网址
	 */
	@ApiModelProperty(required=true,value="品牌网址，输入有效的url")
	private String url;
	/**
	 * 品牌是否可用
	 */
	@ApiModelProperty(hidden=true)
	private Integer disabled;
	/**
	 * 品牌排序
	 */
	@ApiModelProperty(hidden=true)
	private Integer ordernum;
	public Brand() {
	}

	@PrimaryKeyField
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
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public Integer getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}





}
