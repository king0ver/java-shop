package com.enation.app.shop.promotion.exchange.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 积分商品分类实体
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月24日 下午4:56:25
 */
public class ExchangeGoodsCategory implements Serializable {
	private static final long serialVersionUID = -7878650879284562326L;
	/**
	 * 分类id
	 */
	@ApiModelProperty(hidden = true)
	private Integer category_id;
	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称", required = true)
	private String name;
	/**
	 * 父分类
	 */
	@ApiModelProperty(value = "父分类ID，顶级分类时不必传", required = false)
	private Integer parent_id;
	/**
	 * 分类id路径，此字段是以"|"分隔cat_id组成的字符串，如：0|12|25|
	 */
	@ApiModelProperty(hidden = true)
	private String category_path;
	/**
	 * 商品数量
	 */
	@ApiModelProperty(hidden = true)
	private Integer goods_count;
	/**
	 * 分类排序
	 */
	@ApiModelProperty(value = "分类排序，整数 ", required = true)
	private Integer category_order;
	/**
	 * 是否在页面上显示，0：不显示，1：显示。
	 */
	@ApiModelProperty(hidden = true)
	private Integer list_show;
	/**
	 * 分类图片
	 */
	@ApiModelProperty(hidden = true)
	private String image;

	@PrimaryKeyField
	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public String getCategory_path() {
		return category_path;
	}

	public void setCategory_path(String category_path) {
		this.category_path = category_path;
	}

	public Integer getGoods_count() {
		return goods_count;
	}

	public void setGoods_count(Integer goods_count) {
		this.goods_count = goods_count;
	}

	public Integer getCategory_order() {
		return category_order;
	}

	public void setCategory_order(Integer category_order) {
		this.category_order = category_order;
	}

	public Integer getList_show() {
		return list_show;
	}

	public void setList_show(Integer list_show) {
		this.list_show = list_show;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
