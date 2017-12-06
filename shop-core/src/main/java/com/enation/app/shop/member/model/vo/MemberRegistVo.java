package com.enation.app.shop.member.model.vo;

import java.io.Serializable;

/**
 * 
 * 
 * 会员注册发送消息vo
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年11月6日 下午3:23:30
 */
public class MemberRegistVo implements Serializable{

	private static final long serialVersionUID = 1913944052387917137L;
	/** 会员id */
	private Integer member_id;
	/** session id */
	private String session_id;

	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}


}
