package com.enation.app.shop.promotion.halfprice.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 第二件半价活动vo实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月21日 下午7:40:13
 */
@ApiModel(value="HalfPriceVo", description = "第二件半价活动vo实体")
public class HalfPriceVo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5455804180071676902L;

	@ApiModelProperty(value = "活动说明" )
	private String description;
	
	@ApiModelProperty(value = "是否停用 0.没有停用 1.停用" )
	private Integer disabled;
	
	@ApiModelProperty(value = "long 活动结束时间" )
	private Long end_time;
	
	@ApiModelProperty(value = "string 活动结束时间" )
	private String end_time_str;
	
	@ApiModelProperty(value = "long 活动开始时间" )
	private Long start_time;
	
	@ApiModelProperty(value = "string 活动开始时间" )
	private String start_time_str;
	
	@ApiModelProperty(value = "参与活动商品对照" )
	private List<PromotionGoodsVo> goodsList;
	
	@ApiModelProperty(value = "活动id" )
	private Integer hp_id;
	
	@ApiModelProperty(value = "是否全部商品参与 1.全部商品参与 2.部分商品参与" )
	private Integer range_type;
	
	@ApiModelProperty(value = "店铺id" )
	private Integer shop_id;
	
	@ApiModelProperty(value = "活动标题" )
	private String title;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public Long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	public String getEnd_time_str() {
		return end_time_str;
	}
	public void setEnd_time_str(String end_time_str) {
		this.end_time_str = end_time_str;
	}
	public Long getStart_time() {
		return start_time;
	}
	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}
	public String getStart_time_str() {
		return start_time_str;
	}
	public void setStart_time_str(String start_time_str) {
		this.start_time_str = start_time_str;
	}
	public List<PromotionGoodsVo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<PromotionGoodsVo> goodsList) {
		this.goodsList = goodsList;
	}
	public Integer getHp_id() {
		return hp_id;
	}
	public void setHp_id(Integer hp_id) {
		this.hp_id = hp_id;
	}
	public Integer getRange_type() {
		return range_type;
	}
	public void setRange_type(Integer range_type) {
		this.range_type = range_type;
	}
	
	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
