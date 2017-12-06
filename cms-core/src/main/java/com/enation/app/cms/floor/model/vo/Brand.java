package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 品牌 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:27:28
 */
@ApiModel
public class Brand implements Serializable{

	private static final long serialVersionUID = -6413529775714276828L;
	/**品牌id*/
	@ApiModelProperty(value = "品牌id")
	private Integer brand_id;
	/**品牌图片*/
	@ApiModelProperty(value = "品牌图片")
	private String brand_image;

	public Brand() {
		
	}
	
	public Brand(Integer brand_id, String brand_image) {
		super();
		this.brand_id = brand_id;
		this.brand_image = brand_image;
	}

	public Integer getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}

	public String getBrand_image() {
		return brand_image;
	}

	public void setBrand_image(String brand_image) {
		this.brand_image = brand_image;
	}
	
	

}
