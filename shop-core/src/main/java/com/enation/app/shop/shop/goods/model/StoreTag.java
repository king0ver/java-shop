package com.enation.app.shop.shop.goods.model;

import com.enation.app.shop.goods.model.vo.Tag;

/**
 * 店铺商品标签
 * @author LiFenLong
 *
 */
public class StoreTag extends Tag {

	private Integer store_id;	//店铺Id
	private String mark;	//店铺标签标识
	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
