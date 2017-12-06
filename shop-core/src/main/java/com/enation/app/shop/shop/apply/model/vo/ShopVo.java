package com.enation.app.shop.shop.apply.model.vo;

import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.framework.util.DateUtil;

/**
 * 
 * (店铺信息) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月20日 下午1:54:05
 */
public class ShopVo extends ShopDetail{
	
	/**会员Id*/
	private Integer  member_id;	
	/**会员名称*/
	private String  member_name;	
	/**店铺名称*/
	private String shop_name;	
	/**店铺状态*/
	private String shop_disable;	 /**-1 未通过审核  0 待审核 1 已审核 2 已关闭*/
	/**店铺创建时间*/
	private Long  shop_createtime;	
	/**店铺关闭时间*/
	private Long  shop_endtime;
	
	/**
	 * 店铺id
	 */
	private Integer shop_id;
	
	
	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getShop_name() {
		return shop_name;
	}
	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	public String getShop_disable() {
		return shop_disable;
	}
	public void setShop_disable(String shop_disable) {
		this.shop_disable = shop_disable;
	}
	public Long getShop_createtime() {
		return shop_createtime;
	}
	public void setShop_createtime(Long shop_createtime) {
		this.shop_createtime = shop_createtime;
	}
	public Long getShop_endtime() {
		return shop_endtime;
	}
	public void setShop_endtime(Long shop_endtime) {
		this.shop_endtime = shop_endtime;
	}	
	/**
	 * 获取转换格式后的店铺创建时间
	 * @return  店铺创建时间
	 */
	public String shop_createtime_text() {
		return DateUtil.toString(this.getShop_createtime(), "yyyy-MM-dd HH:mm");
	}
	/**
	 * 获取转换格式后的店铺结束时间
	 * @return 店铺结束时间
	 */
	public String shop_endtime_text() {
		return DateUtil.toString(this.getShop_endtime(), "yyyy-MM-dd HH:mm");
	}
	/**
	 * 获取转换格式后的成立时间
	 * @return 成立时间
	 */
	public String establish_date_text() {
		return DateUtil.toString(this.getEstablish_date(), "yyyy-MM-dd HH:mm");
	}
	/**
	 * 获取转换格式后的营业执照有效期开始
	 * @return 营业执照有效期开始
	 */
	public String licence_start_text() {
		return DateUtil.toString(this.getLicence_start(), "yyyy-MM-dd HH:mm");
	}
	/**
	 * 获取转换格式后的营业执照有效期结束
	 * @return 营业执照有效期结束
	 */
	public String licence_end_text() {
		return DateUtil.toString(this.getLicence_end(), "yyyy-MM-dd HH:mm");
	}
}
