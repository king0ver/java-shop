package com.enation.app.nanshan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ArticleQueryParam {
	/** 页码 */
	@ApiModelProperty(value = "页码")
	private Integer page_no;
	/** 分页数 */
	@ApiModelProperty(value = "分页数")
	private Integer page_size;
	public Integer getPage_no() {
		return page_no;
	}
	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}
	public Integer getPage_size() {
		return page_size;
	}
	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
	
	

}
