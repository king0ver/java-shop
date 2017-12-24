package com.enation.app.shop.promotion.fulldiscount.model.po;

import java.io.Serializable;

/**
 * 
 * 满优惠活动实体
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月30日 下午2:46:27
 */
public class FullDiscount implements Serializable{
	
	private static final long serialVersionUID = 8661875858634098748L;

	/**活动id*/
	private Integer fd_id;
	
	/**优惠门槛金额*/
	private Double full_money;
	
	/**减现金*/
	private Double minus_value;
	
	/**送积分*/
	private Integer point_value;
	
	/**活动是否减现金*/
	private Integer is_full_minus;
	
	/**是否免邮*/
	private Integer is_free_ship;
	
	/**是否送积分*/
	private Integer is_send_gift;
	
	/**是否有赠品*/
	private Integer is_send_point;
	
	/**是否增优惠券*/
	private Integer is_send_bonus;
	
	/**赠品id*/
	private Integer gift_id;
	
	/**优惠券id*/
	private Integer bonus_id;
	
	/**是否打折*/
	private Integer is_discount;
	
	/**打多少折*/
	private Double discount_value;
	
	/**活动开始时间*/
	private Long start_time;
	
	/**活动结束时间*/
	private Long end_time;
	
	/**活动标题*/
	private String title;
	
	/**是否全部商品参与*/
	private Integer range_type;
	
	/**是否停用*/
	private Integer disabled;
	
	/**活动说明*/
	private String description;
	
	/**商家id*/
	private Integer shop_id;
	
	public Integer getFd_id() {
		return fd_id;
	}

	public void setFd_id(Integer fd_id) {
		this.fd_id = fd_id;
	}

	public Double getFull_money() {
		return full_money;
	}

	public void setFull_money(Double full_money) {
		this.full_money = full_money;
	}

	public Double getMinus_value() {
		return minus_value;
	}

	public void setMinus_value(Double minus_value) {
		this.minus_value = minus_value;
	}

	public Integer getPoint_value() {
		return point_value;
	}

	public void setPoint_value(Integer point_value) {
		this.point_value = point_value;
	}

	public Integer getIs_full_minus() {
		return is_full_minus;
	}

	public void setIs_full_minus(Integer is_full_minus) {
		this.is_full_minus = is_full_minus;
	}

	public Integer getIs_free_ship() {
		return is_free_ship;
	}

	public void setIs_free_ship(Integer is_free_ship) {
		this.is_free_ship = is_free_ship;
	}

	public Integer getIs_send_gift() {
		return is_send_gift;
	}

	public void setIs_send_gift(Integer is_send_gift) {
		this.is_send_gift = is_send_gift;
	}

	public Integer getIs_send_point() {
		return is_send_point;
	}

	public void setIs_send_point(Integer is_send_point) {
		this.is_send_point = is_send_point;
	}

	public Integer getIs_send_bonus() {
		return is_send_bonus;
	}

	public void setIs_send_bonus(Integer is_send_bonus) {
		this.is_send_bonus = is_send_bonus;
	}

	public Integer getGift_id() {
		return gift_id;
	}

	public void setGift_id(Integer gift_id) {
		this.gift_id = gift_id;
	}

	public Integer getBonus_id() {
		return bonus_id;
	}

	public void setBonus_id(Integer bonus_id) {
		this.bonus_id = bonus_id;
	}

	public Integer getIs_discount() {
		return is_discount;
	}

	public void setIs_discount(Integer is_discount) {
		this.is_discount = is_discount;
	}

	public Double getDiscount_value() {
		return discount_value;
	}

	public void setDiscount_value(Double discount_value) {
		this.discount_value = discount_value;
	}

	public Long getStart_time() {
		return start_time;
	}

	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}

	public Long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getRange_type() {
		return range_type;
	}

	public void setRange_type(Integer range_type) {
		this.range_type = range_type;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
