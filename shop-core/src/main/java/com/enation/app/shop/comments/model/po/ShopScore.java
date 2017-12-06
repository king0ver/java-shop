package com.enation.app.shop.comments.model.po;

import java.io.Serializable;

import com.enation.app.shop.comments.model.vo.CommentVo;
import com.enation.framework.util.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *	店铺动态评分表实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午1:46:16
 */
@ApiModel
public class ShopScore implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "评论时间")
	private long create_time;
	
	@ApiModelProperty(value = "会员ID")
	private Integer member_id;
	
	@ApiModelProperty(value = "订单编号")
	private String order_sn;
	
	@ApiModelProperty(value = "主键ID")
	private Integer score_id;
	
	@ApiModelProperty(value = "发货速度评分")
	private Integer shop_deliverycredit;
	
	@ApiModelProperty(value = "描述相符度评分")
	private Integer shop_desccredit;
	
	@ApiModelProperty(value = "店铺id")
	private Integer shop_id;
	
	@ApiModelProperty(value = "服务态度评分")
	private Integer shop_servicecredit;
	
	
	
	
	
	
	
	public ShopScore() {
		super();
	}
	
	public ShopScore(CommentVo commentVo) {
		super();
		this.create_time =DateUtil.getDateline();
		this.order_sn = commentVo.getOrder_sn();
		this.shop_deliverycredit = commentVo.getShop_deliverycredit();
		this.shop_desccredit = commentVo.getShop_desccredit();
		this.shop_servicecredit = commentVo.getShop_servicecredit();
	}

	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public Integer getScore_id() {
		return score_id;
	}
	public void setScore_id(Integer score_id) {
		this.score_id = score_id;
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
	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public Integer getShop_servicecredit() {
		return shop_servicecredit;
	}
	public void setShop_servicecredit(Integer shop_servicecredit) {
		this.shop_servicecredit = shop_servicecredit;
	}
	
	
	
}
