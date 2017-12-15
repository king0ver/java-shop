package com.enation.app.nanshan.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 属性规格信息
 * @author jianjianming
 * @version $Id: Spec.java,v 0.1 2017年12月13日 下午5:02:38$
 */
public class Spec implements Serializable{
	
	private static final long serialVersionUID = 2144422798166919905L;

	/**
	 * 规格属性ID
	 */
	private Integer spec_id;
	
	/**
	 * 名称
	 */
	private String spec_name;
	
	/**
	 * 创建时间
	 */
	private Integer creation_time;

	public Integer getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(Integer spec_id) {
		this.spec_id = spec_id;
	}

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

	public Integer getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(Integer creation_time) {
		this.creation_time = creation_time;
	}

	
}
