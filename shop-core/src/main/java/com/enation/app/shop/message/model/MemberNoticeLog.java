package com.enation.app.shop.message.model;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 会员站内信消息历史表
 * @author kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-3
 */
public class MemberNoticeLog implements Serializable{

	private static final long serialVersionUID = 6179683129317909194L;

	/**
	 * ID
	 */
	private Integer id;
	
	/**
	 * 会员ID编号
	 */
	private Integer member_id;
	
	/**
	 * 站内信内容
	 */
	private String notice_content;

	/**
	 * 站内信发送时间
	 */
	private Long send_time;
	
	/**
	 * 0 删除   1  没有删除
	 */
	private Integer is_delete;
	
	/**
	 * 0 未读   1   已读
	 */
	private Integer is_read;
	
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

	public String getNotice_content() {
		return notice_content;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public Long getSend_time() {
		return send_time;
	}

	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}

	public Integer getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(Integer is_delete) {
		this.is_delete = is_delete;
	}

	public Integer getIs_read() {
		return is_read;
	}

	public void setIs_read(Integer is_read) {
		this.is_read = is_read;
	}
}
