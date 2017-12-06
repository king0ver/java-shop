package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 活动的Vo
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月24日14:48:36
 */
@ApiModel(description = "购物车中活动Vo")
public class CartPromotionGoodsVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6422522292057193992L;

	@ApiModelProperty(value = "活动开始时间")
	private Long start_time;
	
	@ApiModelProperty(value = "活动结束时间")
	private Long end_time;
	
	@ApiModelProperty(value = "活动id")
	private Integer activity_id;
	
	@ApiModelProperty(value = "活动工具类型")
	private String promotion_type;
	
	@ApiModelProperty(value = "活动名称")
	private String title;
	
	/**
	 * 1为是 0为否
	 */
	@ApiModelProperty(value = "是否选中参与这个活动")
	private Integer is_check;

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

	public Integer getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(Integer activity_id) {
		this.activity_id = activity_id;
	}

	public String getPromotion_type() {
		return promotion_type;
	}

	public void setPromotion_type(String promotion_type) {
		this.promotion_type = promotion_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getIs_check() {
		if(is_check==null){
			return 0;
		}
		return is_check;
	}

	public void setIs_check(Integer is_check) {
		this.is_check = is_check;
	}
	
	
	
	
}
