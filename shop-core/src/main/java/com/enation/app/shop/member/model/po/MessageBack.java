package com.enation.app.shop.member.model.po;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 站内消息后台
 * @author fk
 * @version v1.0
 * 2017年3月15日 下午3:12:22
 */
@ApiModel
public class MessageBack implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4215702775970604925L;

	/**
	 * 消息主键
	 */
	@ApiModelProperty(hidden = true)
	private Integer msg_id;
	
	/**
	 * 消息标题
	 */
	@ApiModelProperty(value = "消息标题" , required = true)
	@NotBlank(message = "消息标题不能为空")
	private String msg_title;
	
	/**
	 * 消息内容
	 */
	@ApiModelProperty(value = "消息内容" , required = true)
	@NotBlank(message = "消息内容不能为空")
	private String msg_content;
	
	/**
	 * 会员标号集合
	 */
	@ApiModelProperty(value = "会员标号集合,部分会员时必传" , required = false)
	private String member_ids;
	
	/**
	 * 管理员标号
	 */
	@ApiModelProperty(hidden = true)
	private Integer adminuser_id;
	
	/**
	 * 管理员姓名
	 */
	@ApiModelProperty(hidden = true)
	private String adminuser_name;
	
	/**
	 * 消息发送时间
	 */
	@ApiModelProperty(hidden = true)
	private Long send_time;
	
	/**
	 * 0  全站   1 部分
	 */
	@ApiModelProperty(value = "发送类型，0  全站   1 部分" , required = true)
	@NotNull(message = "发送类型不能为空")
	private Integer send_type;

	@PrimaryKeyField
	public Integer getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(Integer msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsg_content() {
		return msg_content;
	}

	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}

	public String getMember_ids() {
		return member_ids;
	}

	public void setMember_ids(String member_ids) {
		this.member_ids = member_ids;
	}

	public Integer getAdminuser_id() {
		return adminuser_id;
	}

	public void setAdminuser_id(Integer adminuser_id) {
		this.adminuser_id = adminuser_id;
	}

	public Long getSend_time() {
		return send_time;
	}

	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}

	public Integer getSend_type() {
		return send_type;
	}

	public void setSend_type(Integer send_type) {
		this.send_type = send_type;
	}

	public String getMsg_title() {
		return msg_title;
	}

	public void setMsg_title(String msg_title) {
		this.msg_title = msg_title;
	}

	public String getAdminuser_name() {
		return adminuser_name;
	}

	public void setAdminuser_name(String adminuser_name) {
		this.adminuser_name = adminuser_name;
	}
}
