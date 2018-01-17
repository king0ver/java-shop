package com.enation.app.nanshan.vo;

import java.io.Serializable;
import java.util.Date;

public class ActReserveVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//活动ID
	private String title;//活动名称
	private long activityTime;//活动时间
	private int isDel;//预约状态 0、预约成功 1、已取消

	private String imgUrl;

	private Long expiryDate; //截止时间

	private String  actName;//  活动名称
	private String actCost;//活动费用
	private String actAddress;//活动地址



	public long getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(long activityTime) {
		this.activityTime = activityTime;
	}
	public int getIsDel() {
		return isDel;
	}
	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Long expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActCost() {
		return actCost;
	}

	public void setActCost(String actCost) {
		this.actCost = actCost;
	}

	public String getActAddress() {
		return actAddress;
	}

	public void setActAddress(String actAddress) {
		this.actAddress = actAddress;
	}
}
