package com.enation.app.shop.member.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 发票
 * @author mengyuanming
 * @version v1.0.0
 * @since v1.0.0
 * 2017年5月28日 下午10:37:37
 */
@ApiModel
public class ReceiptPo implements Serializable {
	
	@ApiModelProperty(hidden = true)
	private Integer id;
	@ApiModelProperty(hidden = true)
	private Integer member_id;
	@ApiModelProperty(value = "发票抬头")
	private String title;
	@ApiModelProperty(value = "发票内容")
	private String content;
	@ApiModelProperty(hidden = true)
	private int is_default;
	@ApiModelProperty(hidden = true)
	private long add_time;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIs_default() {
		return is_default;
	}
	public void setIs_default(int is_default) {
		this.is_default = is_default;
	}
	public long getAdd_time() {
		return add_time;
	}
	public void setAdd_time(long add_time) {
		this.add_time = add_time;
	}
	
	
	

}
