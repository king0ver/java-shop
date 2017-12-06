package com.enation.app.shop.message.model;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 店铺短信提醒日志
 * @author Kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-3
 */
public class StoreSMSLog implements Serializable{
	
	private static final long serialVersionUID = -8663026021340519263L;

	/**
	 * ID
	 */
	private Integer id;
	
	/**
	 * 店铺ID
	 */
	private Integer store_id;
	
	/**
	 * 短信内容
	 */
	private String sms_content;

	/**
	 * 发送时间
	 */
	private Long send_time;
	
	@PrimaryKeyField	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public String getSms_content() {
		return sms_content;
	}

	public void setSms_content(String sms_content) {
		this.sms_content = sms_content;
	}

	public Long getSend_time() {
		return send_time;
	}

	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}
}
