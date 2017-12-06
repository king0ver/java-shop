package com.enation.app.shop.member.model.po;

import com.enation.framework.database.NotDbField;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 积分历史记录
 * @author fk
 * @version v1.0
 * 2017年3月8日 下午4:21:50
 */
public class PointHistory  {
	
	private Integer id;
	private Integer member_id;
	private Integer point;
	private Long time;
	private String reason;
	private String cnreason;
	private Integer related_id;
	private Integer type;
	private String operator;
	private Integer mp; 
	private Integer point_type; //0为等级积分1为消费积分
	
	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer memberId) {
		member_id = memberId;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
 
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@NotDbField
	public String getCnreason() {
		if(reason.equals("order_pay_use")) cnreason = "订单消费积分";
		if(reason.equals("order_pay_get")) cnreason = "订单获得积分";
		if(reason.equals("order_refund_use")) cnreason = "退还订单消费积分";
		if(reason.equals("order_refund_get")) cnreason = "扣掉订单所得积分";
		if(reason.equals("order_cancel_refund_consume_gift")) cnreason = "Score deduction for gifts refunded for order cancelling.";
		if(reason.equals("exchange_coupon")) cnreason = "兑换优惠券";
		if(reason.equals("operator_adjust")) cnreason = "管理员改变积分";
		if(reason.equals("consume_gift")) cnreason = "积分换赠品";
		if(reason.equals("recommend_member")) cnreason = "发表评论奖励积分";
		return cnreason;
	}
	public void setCnreason(String cnreason) {
		this.cnreason = cnreason;
	}
	public Integer getRelated_id() {
		return related_id;
	}
	public void setRelated_id(Integer relatedId) {
		related_id = relatedId;
	}
	public Integer getMp() {
		return mp;
	}
	public void setMp(Integer mp) {
		this.mp = mp;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getPoint_type() {
		return point_type;
	}
	public void setPoint_type(Integer point_type) {
		this.point_type = point_type;
	}
}
