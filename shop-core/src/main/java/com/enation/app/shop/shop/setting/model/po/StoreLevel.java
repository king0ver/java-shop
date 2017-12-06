package com.enation.app.shop.shop.setting.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 店铺等级
 * @author LiFenLong
 *
 */
public class StoreLevel implements Serializable{
	private Integer level_id; //店铺等级
	private String level_name; //等级名称
	private Integer space_capacity; //空间容量
	@PrimaryKeyField
	public Integer getLevel_id() {
		return level_id;
	}
	public void setLevel_id(Integer level_id) {
		this.level_id = level_id;
	}
	public String getLevel_name() {
		return level_name;
	}
	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}
	public Integer getSpace_capacity() {
		return space_capacity;
	}
	public void setSpace_capacity(Integer space_capacity) {
		this.space_capacity = space_capacity;
	}
	
}
