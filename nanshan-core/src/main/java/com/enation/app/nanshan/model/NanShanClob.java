package com.enation.app.nanshan.model;

import java.io.Serializable;

/**  
*
* @Description:文章内容 
* @author luyanfen
* @date 2017年12月14日 下午3:33:11
*  
*/ 
public  class NanShanClob implements Serializable {
	
	private int id;
	private int category;
	private String content;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	

}
