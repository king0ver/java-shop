package com.enation.app.shop.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 会员实体
 * @author fk
 * @version v1.0
 * 2017年3月6日 下午5:47:44
 */
@ApiModel(description = "会员")
public class MemberVo implements java.io.Serializable {
	
	
	private static final long serialVersionUID = -8940252135718026314L;
	
	@ApiModelProperty(hidden = true)
	private Integer member_id;	//会员ID
	@ApiModelProperty(value = "会员姓名")
	private String name;		//会员姓名
	@ApiModelProperty(value = "会员用户名")
	private String uname;		//会员用户名
	@ApiModelProperty(value = "会员性别")
	private Integer sex;		//性别
	@ApiModelProperty(hidden = true)
	private Long birthday;		//出生日期
	@ApiModelProperty(value = "省id")
	private Integer province_id;	//省ID
	@ApiModelProperty(value = "市id")
	private Integer city_id;		//市ID
	@ApiModelProperty(value = "区id")
	private Integer region_id;		//区ID
	@ApiModelProperty(value = "城镇id")
	private Integer town_id;        //城镇ID
	@ApiModelProperty(value = "省")
	private String province;	//省	
	@ApiModelProperty(value = "市")
	private String city;		//市
	@ApiModelProperty(value = "区")
	private String region;		//区
	@ApiModelProperty(value = "城镇")
	private String town;        //城镇 
	@ApiModelProperty(value = "详细地址")
	private String address;		//联系地址
	@ApiModelProperty(value = "邮编")
	private String zip;			//邮编
	@ApiModelProperty(value = "手机")
	private String mobile;		//手机
	@ApiModelProperty(value = "电话")
	private String tel;			//电话
	@ApiModelProperty(value = "头像")
	private String face;		//头像
	@ApiModelProperty(value = "是否完善了资料")
	private int info_full;	//是否完善了资料
	@ApiModelProperty(value = "等级名称")
	private String level_name;//会员等级
	@ApiModelProperty(value = "邮箱")
	private String email;//邮箱
	@ApiModelProperty(value = "生日")
	private String mybirthday;
	@ApiModelProperty(value = "绑定的qqid")
	private Integer   qq_id;//绑定的qqid
	@ApiModelProperty(value = "绑定的微信")
	private Integer  wechat_id;//绑定的微信
	@ApiModelProperty(value = "绑定的微博")
	private Integer  weibo_id;//绑定的微博
	
	
	public Integer getMember_id() {
		return member_id;
	}
	public String getMybirthday() {
		return mybirthday;
	}
	public void setMybirthday(String mybirthday) {
		this.mybirthday = mybirthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLevel_name() {
		return level_name;
	}
	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Long getBirthday() {
		return birthday;
	}
	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}
	public Integer getProvince_id() {
		return province_id;
	}
	public void setProvince_id(Integer province_id) {
		this.province_id = province_id;
	}
	public Integer getCity_id() {
		return city_id;
	}
	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}
	public Integer getRegion_id() {
		return region_id;
	}
	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}
	public Integer getTown_id() {
		return town_id;
	}
	public void setTown_id(Integer town_id) {
		this.town_id = town_id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public int getInfo_full() {
		return info_full;
	}
	public void setInfo_full(int info_full) {
		this.info_full = info_full;
	}
	public Integer getQq_id() {
		return qq_id;
	}
	public void setQq_id(Integer qq_id) {
		this.qq_id = qq_id;
	}
	public Integer getWechat_id() {
		return wechat_id;
	}
	public void setWechat_id(Integer wechat_id) {
		this.wechat_id = wechat_id;
	}
	public Integer getWeibo_id() {
		return weibo_id;
	}
	public void setWeibo_id(Integer weibo_id) {
		this.weibo_id = weibo_id;
	}
	
}