package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 
 * 草稿箱sku po
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午10:34:31
 */
public class DraftGoodsSku implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2957245625455857414L;
	private Integer sku_id;
	private Integer draft_goods_id;
	private String goods_name;
	private String sn;
	private Integer quantity;
	private Double price;
	private String specs_ids;
	private Double cost;
	private Double weight;

	public DraftGoodsSku() {

	}

	public DraftGoodsSku(GoodsSkuVo goodsSkuVo) {
		this.setDraft_goods_id(goodsSkuVo.getGoods_id());
		this.setGoods_name(goodsSkuVo.getGoods_name());
		this.setSn(goodsSkuVo.getSn());
		this.setPrice(goodsSkuVo.getPrice());
		this.setCost(goodsSkuVo.getCost());
		this.setWeight(goodsSkuVo.getWeight());
		this.setQuantity(goodsSkuVo.getQuantity());
		this.setSpecs_ids(goodsSkuVo.getSpecs());
	}

	public DraftGoodsSku(DraftGoods goods) {
		this.setDraft_goods_id(goods.getDraft_goods_id());
		this.setGoods_name(goods.getGoods_name());
		this.setSn(goods.getSn());
		this.setPrice(goods.getPrice());
		this.setCost(goods.getCost());
		this.setWeight(goods.getWeight());
		this.setQuantity(goods.getQuantity());
	}

	@PrimaryKeyField
	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public Integer getDraft_goods_id() {
		return draft_goods_id;
	}

	public void setDraft_goods_id(Integer draft_goods_id) {
		this.draft_goods_id = draft_goods_id;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpecs_ids() {
		return specs_ids;
	}

	public void setSpecs_ids(String specs_ids) {
		this.specs_ids = specs_ids;
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

}
