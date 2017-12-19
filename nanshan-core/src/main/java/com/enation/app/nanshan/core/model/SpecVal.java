package com.enation.app.nanshan.core.model;

import java.io.Serializable;

/**
 * 属性规格值
 * @author jianjianming
 * @version $Id: Spec.java,v 0.1 2017年12月13日 下午5:02:38$
 */
public class SpecVal implements Serializable{

	private static final long serialVersionUID = -4023765979399019986L;

	/**
	 * 规格属性ID
	 */
	private Integer spec_id;
	
	/**
	 * 属性值ID
	 */
	private Integer specval_id;
	
	/**
	 * 属性值
	 */
	private String specval_name;
	
	/**
	 * 是否有效
	 */
	private Integer isValid;
	
	/**
	 * 创建时间
	 */
	private Long creation_time;
	
	/**
	 * 更新时间
	 */
	private Long update_time;
	
	/**
	 * 操作人
	 */
	private String operator;
	

	public Integer getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(Integer spec_id) {
		this.spec_id = spec_id;
	}

	public Integer getSpecval_id() {
		return specval_id;
	}

	public void setSpecval_id(Integer specval_id) {
		this.specval_id = specval_id;
	}

	public String getSpecval_name() {
		return specval_name;
	}

	public void setSpecval_name(String specval_name) {
		this.specval_name = specval_name;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Long getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(Long creation_time) {
		this.creation_time = creation_time;
	}

	public Long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Long update_time) {
		this.update_time = update_time;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
