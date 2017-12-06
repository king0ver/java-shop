package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 自动规则
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月12日 上午11:44:13
 */
@ApiModel
public class AutoRule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -300940992158145938L;
	
	@ApiModelProperty(value = "一级分类id")
	private Integer category_id1;
	
	@ApiModelProperty(value = "二级分类id")
	private Integer category_id2;
	
	@ApiModelProperty(value = "三级分类id")
	private Integer category_id3;
	
	@ApiModelProperty(value = "分类名字")
	private String category_name;
	
	@ApiModelProperty(value = "规则，如销量buy_count，价格price，上新create_time")
	private String sort;
	
	@ApiModelProperty(value = "排序，如升序 asc ，降序desc")
	private String order;

	@ApiModelProperty(value = "要显示的商品的数量")
	private Integer number;

	public Integer getCategory_id1() {
		return category_id1;
	}

	public void setCategory_id1(Integer category_id1) {
		this.category_id1 = category_id1;
	}

	public Integer getCategory_id2() {
		return category_id2;
	}

	public void setCategory_id2(Integer category_id2) {
		this.category_id2 = category_id2;
	}

	public Integer getCategory_id3() {
		return category_id3;
	}

	public void setCategory_id3(Integer category_id3) {
		this.category_id3 = category_id3;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
}
