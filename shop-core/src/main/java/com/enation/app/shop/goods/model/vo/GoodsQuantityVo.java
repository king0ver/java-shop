package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;

/**
 * 商品库存vo
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月7日 上午11:23:16
 */
public class GoodsQuantityVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2657189345989798891L;

	private Integer goods_id;
	
	private Integer sku_id;
	
	private Integer quantity;
	
	private Integer enable_quantity;
	
	public GoodsQuantityVo() {
		// TODO Auto-generated constructor stub
	}

	
	public GoodsQuantityVo(Integer goods_id, Integer sku_id, Integer quantity, Integer enable_quantity) {
		super();
		this.goods_id = goods_id;
		this.sku_id = sku_id;
		this.quantity = quantity;
		this.enable_quantity = enable_quantity;
	}


	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
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
	
}
