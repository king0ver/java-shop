package com.enation.app.shop.promotion.minus.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 单品立减Vo
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月18日下午8:39:27
 *
 */
@ApiModel(description = "单品立减vo")
public class MinusVo implements Serializable {

	private static final long serialVersionUID = 2262185663510143477L;

	@ApiModelProperty(value = "单品立减活动id")
	private Integer minus_id;

	@ApiModelProperty(value = "单品立减活动标题")
	private String title;

	@ApiModelProperty(value = "单品立减金额")
	private double minus_price;

	@ApiModelProperty(value = "起始时间时间戳")
	private long start_time;

	@ApiModelProperty(value = "结束时间时间戳")
	private long end_time;

	@ApiModelProperty(value = "起始时间字符串")
	private String start_time_str;

	@ApiModelProperty(value = "结束时间字符串")
	private String end_time_str;

	@ApiModelProperty(value = "活动描述")
	private String description;

	@ApiModelProperty(value = "是否全部商品参与")
	private Integer range_type;

	@ApiModelProperty(value = "是否停用")
	private Integer disabled;

	@ApiModelProperty(value = "商家id")
	private Integer shop_id;

	@ApiModelProperty(value="促销商品列表")
	private List<PromotionGoodsVo> goodsList;

	public Integer getMinus_id() {
		return minus_id;
	}

	public void setMinus_id(Integer minus_id) {
		this.minus_id = minus_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getMinus_price() {
		return minus_price;
	}

	public void setMinus_price(double minus_price) {
		this.minus_price = minus_price;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public List<PromotionGoodsVo> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<PromotionGoodsVo> goodsList) {
		this.goodsList = goodsList;
	}

	public String getStart_time_str() {
		return start_time_str;
	}

	public void setStart_time_str(String start_time_str) {
		this.start_time_str = start_time_str;
	}

	public String getEnd_time_str() {
		return end_time_str;
	}

	public void setEnd_time_str(String end_time_str) {
		this.end_time_str = end_time_str;
	}

}
