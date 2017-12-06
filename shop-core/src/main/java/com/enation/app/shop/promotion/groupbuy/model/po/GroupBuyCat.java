package com.enation.app.shop.promotion.groupbuy.model.po;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 团购分类
 * 
 * @author Chopper
 * @version v1.0
 * @since v6.x 2017年8月22日 下午2:17:17
 *
 */
public class GroupBuyCat {

	private int catid;// 分类id
	private int parentid;// 父类id
	private String cat_name;// 分类名称
	private String cat_path;// 分类结构目录
	private int cat_order;// 分类排序

	@PrimaryKeyField
	public int getCatid() {
		return catid;
	}

	public void setCatid(int catid) {
		this.catid = catid;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public String getCat_name() {
		return cat_name;
	}

	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}

	public String getCat_path() {
		return cat_path;
	}

	public void setCat_path(String cat_path) {
		this.cat_path = cat_path;
	}

	public int getCat_order() {
		return cat_order;
	}

	public void setCat_order(int cat_order) {
		this.cat_order = cat_order;
	}
}
