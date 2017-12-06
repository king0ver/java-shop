package com.enation.app.shop.goodssearch.model;
/**
 * 
 * 商品搜索po类
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月19日 上午10:22:38
 */
public class GoodsSearchLine {
	/**商品id*/
	private int goods_id;
	
	/**商品名称*/
	private String name;
	
	/**缩略图*/
	private String thumbnail;
	
	/**小图*/
	private String small;
	
	/**商品价格*/
	private Double price;
	
	/**购买数*/
	private Integer buy_count;
	
	/**评论数*/
	private Integer comment_num;
	
	/**库存数*/
	private Integer quantity;

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(Integer buy_count) {
		this.buy_count = buy_count;
	}

	public Integer getComment_num() {
		return comment_num;
	}

	public void setComment_num(Integer comment_num) {
		this.comment_num = comment_num;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}
	
	
}
