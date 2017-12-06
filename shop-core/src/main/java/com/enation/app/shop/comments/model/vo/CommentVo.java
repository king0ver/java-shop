package com.enation.app.shop.comments.model.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 评论动态评分vo
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午1:56:45
 */
@ApiModel(description = "评论动态评分vo")
public class CommentVo {

	@ApiModelProperty(value = "会员评论vo的list"  )
	private List<MemberCommentVo> comments;
	
	@ApiModelProperty(value = "订单编号"  )
	private String order_sn;
	
	@ApiModelProperty(value = "发货速度评分"  )
	private Integer shop_deliverycredit;
	
	@ApiModelProperty(value = "描述相符度评分"  )
	private Integer shop_desccredit;
	
	@ApiModelProperty(value = "服务态度评分"  )
	private Integer shop_servicecredit;
	public List<MemberCommentVo> getComments() {
		return comments;
	}
	public void setComments(List<MemberCommentVo> comments) {
		this.comments = comments;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public Integer getShop_deliverycredit() {
		return shop_deliverycredit;
	}
	public void setShop_deliverycredit(Integer shop_deliverycredit) {
		this.shop_deliverycredit = shop_deliverycredit;
	}
	public Integer getShop_desccredit() {
		return shop_desccredit;
	}
	public void setShop_desccredit(Integer shop_desccredit) {
		this.shop_desccredit = shop_desccredit;
	}
	public Integer getShop_servicecredit() {
		return shop_servicecredit;
	}
	public void setShop_servicecredit(Integer shop_servicecredit) {
		this.shop_servicecredit = shop_servicecredit;
	}
	
	
	
}
