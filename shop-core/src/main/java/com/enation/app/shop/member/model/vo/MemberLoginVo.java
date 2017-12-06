package com.enation.app.shop.member.model.vo;

/**
 * 会员-传递数据
 * @author fk
 * @version v1.0
 * 2017年3月6日 下午5:47:44
 */
public class MemberLoginVo implements java.io.Serializable {
	
	
	private static final long serialVersionUID = -6467802271915940102L;
	
	private Integer member_id;	//会员ID
	private String username;		//会员用户名
	private boolean islogin;    //是否登录
	private String session_id;   //sessionId
	private Integer seller_id; //商家id
	private String mobile;//手机号
	private String level_name;//会员等级名称
	private String face;//头像
	
	
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getLevel_name() {
		return level_name;
	}
	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public boolean isIslogin() {
		return islogin;
	}
	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}
	public Integer getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}
	
}