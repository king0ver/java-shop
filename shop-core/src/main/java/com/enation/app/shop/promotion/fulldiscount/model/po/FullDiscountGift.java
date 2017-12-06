package com.enation.app.shop.promotion.fulldiscount.model.po;

import java.io.Serializable;
/**
 * 
 * 满优惠赠品实体类
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月30日 下午3:24:28
 */
public class FullDiscountGift implements Serializable{

	private static final long serialVersionUID = 7042955543466583659L;
	
	/**赠品id*/
	private Integer gift_id;
	
	/**赠品名称*/
	private String gift_name;
	
	/**赠品金额*/
	private Double gift_price;
	
	/**赠品图片*/
	private String gift_img;
	
	/**赠品类型*/
	private Integer gift_type;
	
	/**库存*/
	private Integer actual_store;
	
	/**可用库存*/
	private Integer enable_store;
	
	/**活动时间*/
	private Long create_time;
	
	/**活动商品id*/
	private Integer goods_id;
	
	/***/
	private Integer disabled;
	
	/**店铺id*/
	private Integer shop_id;
	
	
	public Integer getGift_id() {
		return gift_id;
	}

	public void setGift_id(Integer gift_id) {
		this.gift_id = gift_id;
	}

	public String getGift_name() {
		return gift_name;
	}

	public void setGift_name(String gift_name) {
		this.gift_name = gift_name;
	}

	public Double getGift_price() {
		return gift_price;
	}

	public void setGift_price(Double gigt_price) {
		this.gift_price = gigt_price;
	}

	public String getGift_img() {
		return gift_img;
	}

	public void setGift_img(String gift_img) {
		this.gift_img = gift_img;
	}

	public Integer getGift_type() {
		return gift_type;
	}

	public void setGift_type(Integer gift_type) {
		this.gift_type = gift_type;
	}

	public Integer getActual_store() {
		return actual_store;
	}

	public void setActual_store(Integer actual_store) {
		this.actual_store = actual_store;
	}

	public Integer getEnable_store() {
		return enable_store;
	}

	public void setEnable_store(Integer enable_store) {
		this.enable_store = enable_store;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	

}
