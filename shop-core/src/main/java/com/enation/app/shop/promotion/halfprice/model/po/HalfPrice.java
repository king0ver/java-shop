package com.enation.app.shop.promotion.halfprice.model.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 第二件半价活动实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月20日 下午12:43:59
 */
@ApiModel(value="HalfPrice", description = "第二件半价活动")
public class HalfPrice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1469266586239844236L;

	@ApiModelProperty(value = "第二件半价活动ID" )
	private Integer hp_id;
	
	@ApiModelProperty(value = "活动开始时间" )
	private Long start_time;
	
	@ApiModelProperty(value = "活动结束时间" )
	private Long end_time;
	
	@ApiModelProperty(value = "活动标题" )
	private String title;
	
	@ApiModelProperty(value = "是否全部商品参与 1.全部商品参与 2.部分商品参与" )
	private Integer range_type;
	
	@ApiModelProperty(value = "是否停用 0.没有停用 1.停用" )
	private Integer disabled;
	
	@ApiModelProperty(value = "活动说明" )
	private String description;
	
	@ApiModelProperty(value = "商家ID" )
	private Integer shop_id;

	
	public Integer getHp_id() {
		return hp_id;
	}
	public void setHp_id(Integer hp_id) {
		this.hp_id = hp_id;
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
	
	
	
	
	
	
}
