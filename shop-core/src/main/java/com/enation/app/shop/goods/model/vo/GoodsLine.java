package com.enation.app.shop.goods.model.vo;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class GoodsLine {
	
	@ApiModelProperty(hidden = true)
	private Integer goods_id;
	@ApiModelProperty(value="商品名称",example="春装特价",required = true)
	@NotBlank(message = "商品名称不能为空")
	private String  goods_name;
	
	@ApiModelProperty(value="商品编号",example="26735627",required = true)
	@NotBlank(message = "商品编号不能为空")
	private String sn;
	
	private String thumbnail;
	
	private Double price ;
	
	private Integer quantity;

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
 

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	
}
