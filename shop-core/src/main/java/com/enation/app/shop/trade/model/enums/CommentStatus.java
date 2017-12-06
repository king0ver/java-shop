package com.enation.app.shop.trade.model.enums;


/**
 * 评论状态
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年6月5日下午9:13:55
 */
public enum CommentStatus {
	
	UNFINISHED("未完成评论"),
	FINISHED("已经完成评化");
	
	private String description;

	CommentStatus(String _description){
		  this.description=_description;
		  
	}
	
	public String description(){
		return this.description;
	}
	
	public String value(){
		return this.name();
	}
 
}
