package com.enation.app.cms.floor.model.vo;
/**
 * 
 * 商品选择器用来返回的信息 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午9:18:32
 */
public class Goods {
	/**商品id*/
	private int goods_id;
	/**商品名字*/
	private String goods_name;
	/**商品缩略图*/
	private String thumbnail;
	/**商品价格*/
	private double goods_price;
	/**商品路径*/
	private String goods_url;
	
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public double getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}
