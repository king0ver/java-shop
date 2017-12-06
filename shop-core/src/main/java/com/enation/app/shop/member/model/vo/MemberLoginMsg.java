package com.enation.app.shop.member.model.vo;

import java.io.Serializable;

/**
 * 会员登陆消息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月18日 下午9:39:06
 */
public class MemberLoginMsg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8173084471934834777L;
	
	private Integer member_id;//会员id
	private Long last_login_time;//上次登录时间
	
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Long getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(Long last_login_time) {
		this.last_login_time = last_login_time;
	}

	
	
}
