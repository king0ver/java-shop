package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;

import com.enation.app.shop.goods.model.po.Category;

public class CategoryPluginVo extends Category implements Serializable {

	/**
	 * 分类获取的插件返回的vo
	 */
	private static final long serialVersionUID = -8428052730649034814L;

	private Integer id;
	private String text;
	public CategoryPluginVo() {
	}

	public Integer getId() {
		id=this.getCategory_id();
		return id;
	}


	public String getText() {
		text=this.getName();
		return text;
	}


}
