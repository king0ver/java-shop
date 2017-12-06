package com.enation.app.shop.goods.model.po;

import java.io.Serializable;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 商品sku
 * 
 * @author fk
 * @version v1.0 2017年4月1日 下午3:32:38
 */
public class GoodsSku implements Serializable {

	private static final long serialVersionUID = -1948319228925387702L;

	private Integer sku_id;
	private Integer goods_id;
	private String goods_name;
	private String sn;
	private Integer quantity;
	private Integer enable_quantity;
	private Double price;
	private String specs;
	private Double cost;
	private Double weight;
	private Integer category_id;
	private String thumbnail;
	private String seller_name;
	private Integer seller_id;

	public GoodsSku() {

	}

	public GoodsSku(Goods goods) {
		this.setCategory_id(goods.getCategory_id());
		this.setGoods_id(goods.getGoods_id());
		this.setGoods_name(goods.getGoods_name());
		this.setSn(goods.getSn());
		this.setSeller_id(goods.getSeller_id());
		this.setPrice(goods.getPrice());
		this.setCost(goods.getCost());
		this.setWeight(goods.getWeight());
		this.setThumbnail(goods.getThumbnail());
		this.setQuantity(goods.getQuantity());
		this.setEnable_quantity(goods.getEnable_quantity());
	}

	@PrimaryKeyField
	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
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

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

}
