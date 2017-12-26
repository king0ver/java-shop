package com.enation.app.nanshan.model;

public class NActReserve {
	
	private int    article_id;
	private long   act_time;
	private String member_name;
	private String age;
	private String phone_number;
	private String email;
	

	
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
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
	public long getAct_time() {
		return act_time;
	}
	public void setAct_time(long act_time) {
		this.act_time = act_time;
	}
	
	

}
