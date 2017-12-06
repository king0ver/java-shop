package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import com.enation.framework.database.NotDbField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分类参数实体
 * @author fk
 * @version v1.0
 * 2017年3月31日 下午1:46:22
 */
@ApiModel
public class Parameters  implements Serializable{

	private static final long serialVersionUID = 7404676110335007049L;

	/**
	 * 主键
	 */
	@ApiModelProperty(hidden = true)
	private Integer param_id; 
	/**
	 * 参数名字
	 */
	@ApiModelProperty(required=true,value="参数名字")
	private String param_name;
	/**
	 * 参数类型
	 */
	@ApiModelProperty(required=true,value="参数类型，1 输入项   2 选择项")
	private Integer param_type; //'1 输入项   2 选择项'
	/**
	 * 选择项，当参数类型是选择项时，使用
	 */
	@ApiModelProperty(required=false,value="选择项，当参数类型是选择项2时，必填，逗号分隔")
	private String options;
	/**
	 * 外键，分类ID
	 */
	@ApiModelProperty(required=true,value="分类ID")
	private Integer category_id;
	/**
	 * 外键，分组ID
	 */
	@ApiModelProperty(required=true,value="分组ID")
	private Integer group_id;
	/**
	 * 是否可索引
	 */
	@ApiModelProperty(required=true,value="是否可索引，0 不显示 1 显示")
	private Integer is_index; //'0 不显示 1 显示'
	/**
	 * 参数单位
	 */
	@ApiModelProperty(required=false,value="参数单位")
	private String unit;    
	/**
	 * 是否必填
	 */
	@ApiModelProperty(required=true,value="是否必填，是  1    否   0")
	private Integer required;//'是  1    否   0',
	
	
	@ApiModelProperty(hidden = true)
	private Integer sort;
	
	
	public Integer getParam_id() {
		return param_id;
	}
	public void setParam_id(Integer param_id) {
		this.param_id = param_id;
	}
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public Integer getParam_type() {
		return param_type;
	}
	public void setParam_type(Integer param_type) {
		this.param_type = param_type;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public Integer getIs_index() {
		return is_index;
	}
	public void setIs_index(Integer is_index) {
		this.is_index = is_index;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getRequired() {
		return required;
	}
	public void setRequired(Integer required) {
		this.required = required;
	}
	@NotDbField
	public String[] getOptionAr(){
		if(options==null || options.equals("")){
			return new String[]{};
		}
		
		String[] ar_options = options.split(",");
		
		return ar_options;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
}
