package com.enation.app.nanshan.vo;

import java.io.Serializable;
import java.util.Date;

public class ActReserveVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int articleId;//活动id
	private Date actTime;//参与时间
	private String memberName;//参加成员姓名
	private String phoneNumber;//电话号码
	private String email;
	private String age;// 年龄
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	
	public Date getActTime() {
		return actTime;
	}
	public void setActTime(Date actTime) {
		this.actTime = actTime;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
	

}
