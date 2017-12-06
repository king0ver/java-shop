package com.enation.app.shop.promotion.groupbuy.model.po;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 团购地区
 * 
 * @author Chopper
 * @version v1.0
 * @since v6.4 2017年8月22日 下午2:16:16
 *
 */
public class GroupBuyArea {

	private int area_id; // 地区ID
	private int parent_id; // 地区父节点
	private String area_name; // 地区名字
	private String area_path; // 地区路径结构
	private int area_order; // 地区排序

	@PrimaryKeyField
	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getArea_path() {
		return area_path;
	}

	public void setArea_path(String area_path) {
		this.area_path = area_path;
	}

	public int getArea_order() {
		return area_order;
	}

	public void setArea_order(int area_order) {
		this.area_order = area_order;
	}

}
