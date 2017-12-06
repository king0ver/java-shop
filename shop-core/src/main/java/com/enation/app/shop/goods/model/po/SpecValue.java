package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 
 * 规格值实体
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月25日 下午5:47:08
 */
public class SpecValue implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1426807099688672502L;
	/**
	 * 主键
	 */
	private Integer spec_value_id;
	/**
	 * 规格id
	 */
	private Integer spec_id;
	/**
	 * 规格name
	 */
	private String spec_name;
	/**
	 * 规格值
	 */
	private String spec_value;
	/**
	 * 规格图片
	 */
	private String spec_image;
	/**
	 * 类型 0文字 1图片
	 */
	private Integer spec_type;
	/**
	 * 商家id
	 */
	private Integer seller_id;

	@PrimaryKeyField
	public Integer getSpec_value_id() {
		return spec_value_id;
	}

	public void setSpec_value_id(Integer specValueId) {
		spec_value_id = specValueId;
	}

	public Integer getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(Integer specId) {
		spec_id = specId;
	}

	public String getSpec_value() {
		return spec_value;
	}

	public void setSpec_value(String specValue) {
		spec_value = specValue;
	}

	public String getSpec_image() {
		return spec_image;
	}

	public void setSpec_image(String specImage) {
		spec_image = specImage;
	}

	public Integer getSpec_type() {
		return spec_type;
	}

	public void setSpec_type(Integer spec_type) {
		this.spec_type = spec_type;
	}

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

}
