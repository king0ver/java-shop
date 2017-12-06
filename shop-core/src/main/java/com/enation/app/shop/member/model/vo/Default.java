package com.enation.app.shop.member.model.vo;

import java.io.Serializable;

/**
 * 
 * default的sdk
 * @author mengyuanming
 * @version v1.0.0
 * @since v1.0.0
 * 2017年5月28日 下午11:38:05
 */

public class Default implements Serializable {
	
	private Integer id;
	private String title;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
