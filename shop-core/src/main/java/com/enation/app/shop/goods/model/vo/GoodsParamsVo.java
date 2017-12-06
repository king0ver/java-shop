package com.enation.app.shop.goods.model.vo;


import com.enation.app.shop.goods.model.po.GoodsParams;


public class GoodsParamsVo extends GoodsParams{
	
	private static final long serialVersionUID = -4904700751774005326L;
	private Integer param_type; //'1 输入项   2 选择项'
	private String options;
	private String unit;    
	private Integer required;//'是  1    否   0',
	private Integer group_id;
	private Integer is_index;//是否可索引   1 可以   0不可以
	
	
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
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
	public String[] getOptionAr(){
		if(options!=null){
			return options.replaceAll("\r|\n", "").split(",");
		}
		return null;
	}
	public Integer getIs_index() {
		return is_index;
	}
	public void setIs_index(Integer is_index) {
		this.is_index = is_index;
	}

}
