package com.enation.app.shop.message.model;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 会员短信消息日志
 * @author kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-3
 */
public class MemberSMSLog implements Serializable{
	
	private static final long serialVersionUID = 8425025947754303148L;

	/**
	 * ID
	 */
	private Integer id;
	
	/**
	 * 会员ID
	 */
	private Integer member_id;
	
	/**
	 * 短信内容
	 */
	private String sms_content;

	/**
	 * 发送时间
	 */
	private Long send_time;
	
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

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String getSms_content() {
		return sms_content;
	}

	public void setSms_content(String sms_content) {
		this.sms_content = sms_content;
	}

	public Long getSend_time() {
		return send_time;
	}

	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}
	
}
