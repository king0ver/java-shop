package com.enation.app.shop.promotion.minus.model.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 单品立减活动po
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月22日下午4:04:41
 *
 */
@ApiModel(description = "单品立减po")
public class Minus implements Serializable {

	private static final long serialVersionUID = -4970086520853263344L;

	@ApiModelProperty(value = "单品立减活动id")
	private Integer minus_id;

	@ApiModelProperty(value = "单品立减活动标题")
	private String title;

	@ApiModelProperty(value = "单品立减金额")
	private Double single_reduction_value;

	@ApiModelProperty(value = "起始时间")
	private long start_time;

	@ApiModelProperty(value = "结束时间")
	private long end_time;

	@ApiModelProperty(value = "起始时间字符串")
	private String start_time_str;

	@ApiModelProperty(value = "结束时间字符串")
	private String end_time_str;

	@ApiModelProperty(value = "活动描述")
	private String description;

	@ApiModelProperty(value = "是否选择全部商品")
	private Integer range_type;

	@ApiModelProperty(value = "是否停用")
	private Integer disabled;

	@ApiModelProperty(value = "商家id")
	private Integer shop_id;

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

	public Double getSingle_reduction_value() {
		return single_reduction_value;
	}

	public void setSingle_reduction_value(Double single_reduction_value) {
		this.single_reduction_value = single_reduction_value;
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
