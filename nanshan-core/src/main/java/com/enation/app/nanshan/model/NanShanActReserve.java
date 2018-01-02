package com.enation.app.nanshan.model;

public class NanShanActReserve {
	
	private int activity_id;
	private long activity_time;
	private String attend_name;
	private String age;
	private String phone_number;
	private String email;
	private int member_id;


	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public int getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}
	public long getActivity_time() {
		return activity_time;
	}
	public void setActivity_time(long activity_time) {
		this.activity_time = activity_time;
	}

	public String getAttend_name() {
		return attend_name;
	}

	public void setAttend_name(String attend_name) {
		this.attend_name = attend_name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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
	
	

}
