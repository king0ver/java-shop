package com.enation.app.shop.message.model;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 消息模板
 * @author kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-3
 */
public class MessageTemplate implements Serializable{

	private static final long serialVersionUID = -6521973855628260418L;

	/**
	 * ID
	 */
	private Integer id;
	
	/**
	 * 模板编号
	 */
	private String tpl_code;
	
	/**
	 * 模板名称
	 */
	private String tpl_name;
	
	/**
	 * 类型：1：会员 2：店铺 3：其他
	 */
	private Integer type;
	
	/**
	 * 邮件标题
	 */
	private String email_title;
	
	/**
	 * 短信提醒是否开启
	 */
	private Integer sms_state;
	
	/**
	 * 站内信提醒是否开启
	 */
	private Integer notice_state;
	
	/**
	 * 站内信提醒是否开启
	 */
	private Integer email_state;
	
	/**
	 * 站内信内容
	 */
	private String content;
	
	/**
	 * 短信内容
	 */
	private String sms_content;
	
	/**
	 * 邮件内容
	 */
	private String email_content;

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTpl_code() {
		return tpl_code;
	}

	public void setTpl_code(String tpl_code) {
		this.tpl_code = tpl_code;
	}

	public String getTpl_name() {
		return tpl_name;
	}

	public void setTpl_name(String tpl_name) {
		this.tpl_name = tpl_name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSms_state() {
		return sms_state;
	}

	public void setSms_state(Integer sms_state) {
		this.sms_state = sms_state;
	}

	public Integer getNotice_state() {
		return notice_state;
	}

	public void setNotice_state(Integer notice_state) {
		this.notice_state = notice_state;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSms_content() {
		return sms_content;
	}

	public void setSms_content(String sms_content) {
		this.sms_content = sms_content;
	}

	public Integer getEmail_state() {
		return email_state;
	}

	public void setEmail_state(Integer email_state) {
		this.email_state = email_state;
	}

	public String getEmail_content() {
		return email_content;
	}

	public void setEmail_content(String email_content) {
		this.email_content = email_content;
	}

	public String getEmail_title() {
		return email_title;
	}

	public void setEmail_title(String email_title) {
		this.email_title = email_title;
	}
}
