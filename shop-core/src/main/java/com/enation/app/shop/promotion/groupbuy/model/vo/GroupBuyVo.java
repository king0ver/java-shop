package com.enation.app.shop.promotion.groupbuy.model.vo;

import java.io.Serializable;

import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;

/**
 * 团购商品vo
 * @author Chopper
 * @version v1.0
 * @since v6.4
 * 2017年9月7日 下午5:33:50 
 *
 */
public class GroupBuyVo  implements Serializable{
	/** 
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	private static final long serialVersionUID = -2884485856581006165L;
	/**
	 * 地区id
	 */
	private Integer area_id;
	/**
	 * 已经购买的数量
	 */
	private Integer buy_num;
	/**
	 * 活动id
	 */
	private Integer ga_id;
	/**
	 * 商品活动id
	 */
	private Integer gb_id;
	/**
	 * 参与数量
	 */
	private Integer goods_num;
	/**
	 * 限购
	 */
	private Integer limit_num;
	/**
	 * 价格
	 */
	private double original_price;
	/**
	 * 优惠价
	 */
	private double price;
	private PromotionGoodsVo promotion_goods;
	/**
	 * 显示购买的数量
	 */
	private Integer visual_num;
	
	
	
	public Integer getArea_id() {
		return area_id;
	}
	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}
	public Integer getBuy_num() {
		return buy_num;
	}
	public void setBuy_num(Integer buy_num) {
		this.buy_num = buy_num;
	}
	public Integer getGa_id() {
		return ga_id;
	}
	public void setGa_id(Integer ga_id) {
		this.ga_id = ga_id;
	}
	public Integer getGb_id() {
		return gb_id;
	}
	public void setGb_id(Integer gb_id) {
		this.gb_id = gb_id;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public Integer getLimit_num() {
		return limit_num;
	}
	public void setLimit_num(Integer limit_num) {
		this.limit_num = limit_num;
	}
	public Integer getVisual_num() {
		return visual_num;
	}
	public void setVisual_num(Integer visual_num) {
		this.visual_num = visual_num;
	}
	public double getOriginal_price() {
		return original_price;
	}
	public void setOriginal_price(double original_price) {
		this.original_price = original_price;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public PromotionGoodsVo getPromotion_goods() {
		return promotion_goods;
	}
	public void setPromotion_goods(PromotionGoodsVo promotion_goods) {
		this.promotion_goods = promotion_goods;
	}
	

}
