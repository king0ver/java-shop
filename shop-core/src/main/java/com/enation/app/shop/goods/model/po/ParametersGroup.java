package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分类参数组
 * @author fk
 * @version v1.0
 * 2017年4月10日 下午3:28:51
 */
@ApiModel
public class ParametersGroup  implements Serializable{

	private static final long serialVersionUID = 4328553988776827487L;
	
	/**
	 * 参数组id
	 */
	@ApiModelProperty(hidden = true)
	private Integer group_id;
	
	@ApiModelProperty(required=true,value="参数组名字")
	private String group_name;
	
	@ApiModelProperty(required=true,value="分类id")
	private Integer category_id;
	
	@ApiModelProperty(value="排序")
	private Integer sort;
	
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
