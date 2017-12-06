package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.trade.model.vo.Spec;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 缓存使用sku
 * @author fk
 * @version v1.0
 * 2017年6月8日 上午11:03:10
 */
@ApiModel
public class CacheSku implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1840007653750357293L;
	private Integer sku_id;
	@ApiModelProperty(value = "货号")
	private String sn;
	@ApiModelProperty(value = "库存")
	private Integer quantity;
	@ApiModelProperty(value = "可用库存")
	private Integer enable_quantity;
	@ApiModelProperty(value = "价格")
	private Double price;
	@ApiModelProperty(value = "商品名称")
	private String goods_name;
	@ApiModelProperty(value = "卖家id")
	private Integer seller_id;
	@ApiModelProperty(value = "卖家名称")
	private String seller_name;
	@ApiModelProperty(value = "缩略图")
	private String thumbnail;
	@ApiModelProperty(value = "分类id")
	private Integer category_id;
	@ApiModelProperty(value = "货品重量")
	private Double weight;
	@ApiModelProperty(value = "规格集合")
	private List<Spec> specList;
	
	public Integer getSku_id() {
		return sku_id;
	}
	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getEnable_quantity() {
		return enable_quantity;
	}
	public void setEnable_quantity(Integer enable_quantity) {
		this.enable_quantity = enable_quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public List<Spec> getSpecList() {
		return specList;
	}
	public void setSpecList(List<Spec> specList) {
		this.specList = specList;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public Integer getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	
}
