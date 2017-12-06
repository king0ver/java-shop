package com.enation.app.cms.floor.model.floor;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
/**
 * 
 * 为保存楼层设计的数据(FloorDesign)提供模型 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:23:17
 */
public class PanelPo implements Serializable {

	private static final long serialVersionUID = 7288139584061995200L;
	/** 楼层id*/
	@ApiModelProperty(value = "楼层id")
	protected int id;
	
	private int floor_id;
	/** 排序*/
	@ApiModelProperty(value = "排序")
	protected int sort;
	/** 模版名字*/
	protected String panel_name;
	/** 每一个楼层的模版数据*/
	private String panel_data;
	/** 面板模板id*/
	@ApiModelProperty(value = "面板模板id")
	private int panel_tpl_id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(int floor_id) {
		this.floor_id = floor_id;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getPanel_name() {
		return panel_name;
	}

	public void setPanel_name(String panel_name) {
		this.panel_name = panel_name;
	}

	public String getPanel_data() {
		return panel_data;
	}

	public void setPanel_data(String panel_data) {
		this.panel_data = panel_data;
	}

	public int getPanel_tpl_id() {
		return panel_tpl_id;
	}

	public void setPanel_tpl_id(int panel_tpl_id) {
		this.panel_tpl_id = panel_tpl_id;
	}

	
}
