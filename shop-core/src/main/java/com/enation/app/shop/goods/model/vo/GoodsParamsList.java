package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;
import java.util.List;


/**
 * 参数list
 * @author fk
 * @version v1.0
 * 2017年4月10日 下午6:45:32
 */
public class GoodsParamsList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3463041452613225306L;
	
	private List<GoodsParamsVo> params;
	private String group_name;
	private Integer group_id;
	
	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	public List<GoodsParamsVo> getParams() {
		return params;
	}

	public void setParams(List<GoodsParamsVo> params) {
		this.params = params;
	}

	
}
