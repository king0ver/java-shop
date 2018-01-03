package com.enation.app.nanshan.core.model;

import java.io.Serializable;

/**
 * 充值记录信息
 * @author jianjianming
 * @version $Id: RechargeRecordVo.java,v 0.1 2018年1月3日 下午2:53:57$
 */
public class RechargeRecordVo implements Serializable{
	
	private static final long serialVersionUID = -3816309210306507769L;

	/**
	 * 充值ID
	 */
	private Integer rechargeId;
	
	/**
	 * 充值编号
	 */
	private String  rechargeSn;

	/**
	 * 会员ID
	 */
	private Integer memberId;
	
	/**
	 * 会员名称
	 */
	private String memberName;
	
	/**
	 * 点数
	 */
	private Integer points;
	
	/**
	 * 游戏名称
	 */
	private String gameAccount;
	
	/**
	 * 价格
	 */
	private Double price;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 支付状态
	 */
	private String payStatus;
	
	/**
	 * 支付类型
	 */
	private String paymentType;
	
	/**
	 * 创建时间
	 */
	private Integer creationTime;

	/**
	 * 购买订单号
	 */
	private String payOrderNo;
	
	/**
	 * 支付时间
	 */
	private Integer paymentTime;
	
	/**
	 * 客户端类型
	 */
	private String clientType;
	

	public Integer getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Integer rechargeId) {
		this.rechargeId = rechargeId;
	}

	public String getRechargeSn() {
		return rechargeSn;
	}

	public void setRechargeSn(String rechargeSn) {
		this.rechargeSn = rechargeSn;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getGameAccount() {
		return gameAccount;
	}

	public void setGameAccount(String gameAccount) {
		this.gameAccount = gameAccount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Integer creationTime) {
		this.creationTime = creationTime;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public Integer getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Integer paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
}
