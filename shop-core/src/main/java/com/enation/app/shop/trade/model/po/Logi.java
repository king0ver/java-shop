package com.enation.app.shop.trade.model.po;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 
 * 物流公司
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月10日 下午2:45:04
 */
public class Logi implements java.io.Serializable {

	private Integer id;
	/** 物流公司名称 */
	private String name; 
	/** 物流公司code */
	private String code; 
	/** 快递鸟物流公司code */
	private String kdcode; 
	/** 是否支持电子面单1：支持 0：不支持  */
	private String is_waybill; 
	/** 物流公司客户号 */
	private String customer_name; 
	/** 物流公司电子面单密码 */
	private String customer_pwd; 
	

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public String getKdcode() {
		return kdcode;
	}

	public void setKdcode(String kdcode) {
		this.kdcode = kdcode;
	}

	public String getIs_waybill() {
		return is_waybill;
	}

	public void setIs_waybill(String is_waybill) {
		this.is_waybill = is_waybill;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_pwd() {
		return customer_pwd;
	}

	public void setCustomer_pwd(String customer_pwd) {
		this.customer_pwd = customer_pwd;
	}
    
	
	
}