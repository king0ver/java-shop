package com.enation.app.shop.member.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 收藏店铺表
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年10月13日17:59:13
 */
@ApiModel(description = "会员收藏店铺表")
public class MemberCollect implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3611072385708540242L;
	
	@ApiModelProperty(value = "主键ID" )
	private Integer id;
	
	@ApiModelProperty(value = "会员ID" )
	private Integer member_id;
	
	@ApiModelProperty(value = "店铺ID" )
	private Integer store_id;
	
	@ApiModelProperty(value = "收藏时间" )
	private Long create_time;
	
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
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
}
