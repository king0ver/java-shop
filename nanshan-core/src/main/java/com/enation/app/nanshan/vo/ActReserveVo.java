package com.enation.app.nanshan.vo;

import java.io.Serializable;
import java.util.Date;

public class ActReserveVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int activityId;//活动ID
	private String activityName;//活动名称
	private long activityTime;//活动时间
	private int isDel;//预约状态 0、预约成功 1、已取消
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
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
	
	
	
	
	

}
