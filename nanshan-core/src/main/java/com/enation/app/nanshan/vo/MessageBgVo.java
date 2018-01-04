package com.enation.app.nanshan.vo;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 站内通知消息后台
 * @author jianjianming
 * @version $Id: MessageBgVo.java,v 0.1 2018年1月4日 上午11:11:19$
 */
public class MessageBgVo {
	
	/**
	 * 消息主键
	 */
	private Integer msg_id;
	
	/**
	 * 消息标题
	 */
	private String msg_title;
	
	/**
	 * 消息内容
	 */
	private String msg_content;
	
	/**
	 * 会员标号集合
	 */
	private String member_ids;
	
	/**
	 * 管理员标号
	 */
	private Integer adminuser_id;
	
	/**
	 * 管理员姓名
	 */
	private String adminuser_name;
	
	/**
	 * 消息发送时间
	 */
	private Long send_time;
	
	/**
	 * 0  全站   1 部分
	 */
	private Integer send_type;

	@PrimaryKeyField
	public Integer getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(Integer msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsg_content() {
		return msg_content;
	}

	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}

	public String getMember_ids() {
		return member_ids;
	}

	public void setMember_ids(String member_ids) {
		this.member_ids = member_ids;
	}

	public Integer getAdminuser_id() {
		return adminuser_id;
	}

	public void setAdminuser_id(Integer adminuser_id) {
		this.adminuser_id = adminuser_id;
	}

	public Long getSend_time() {
		return send_time;
	}

	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}

	public Integer getSend_type() {
		return send_type;
	}

	public void setSend_type(Integer send_type) {
		this.send_type = send_type;
	}

	public String getMsg_title() {
		return msg_title;
	}

	public void setMsg_title(String msg_title) {
		this.msg_title = msg_title;
	}

	public String getAdminuser_name() {
		return adminuser_name;
	}

	public void setAdminuser_name(String adminuser_name) {
		this.adminuser_name = adminuser_name;
	}

	
}
