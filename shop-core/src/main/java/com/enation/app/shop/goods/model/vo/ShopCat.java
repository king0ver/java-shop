package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiParam;

/**
 * 店铺分类
 * 
 * @author Chopper
 * @version v1.0
 * @since v6.2 
 * 2017年4月10日 下午8:37:21
 *
 */
public class ShopCat implements Serializable {
	/**
	 * <p> Title:</p>
	 * <p>Description:</p>
	 */
	private static final long serialVersionUID = 5097366401240953988L;
	/** 分类id */
	@ApiParam(value = "分类id", hidden = true)
	private Integer shop_cat_id;
	/** 父类id */
	@ApiParam("父类id")
	private Integer shop_cat_pid;
	/** 商家id */
	@ApiParam("商家id")
	private Integer seller_id;
	/** 分类名称 */
	@ApiParam("分类名称")
	private String shop_cat_name;
	/** 是否启用 1启用 0 禁用 */
	@ApiParam("是否启用 1启用 0 禁用")
	private Integer disable;
	/** 排序 */
	@ApiParam("排序")
	private Integer sort;
	/** 分类路径 */
	@ApiParam("分类路径")
	private String cat_path;

	public Integer getShop_cat_id() {
		return shop_cat_id;
	}

	public void setShop_cat_id(Integer shop_cat_id) {
		this.shop_cat_id = shop_cat_id;
	}

	public Integer getShop_cat_pid() {
		return shop_cat_pid;
	}

	public void setShop_cat_pid(Integer shop_cat_pid) {
		this.shop_cat_pid = shop_cat_pid;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public String getShop_cat_name() {
		return shop_cat_name;
	}

	public void setShop_cat_name(String shop_cat_name) {
		this.shop_cat_name = shop_cat_name;
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getCat_path() {
		return cat_path;
	}

	public void setCat_path(String cat_path) {
		this.cat_path = cat_path;
	}

}
