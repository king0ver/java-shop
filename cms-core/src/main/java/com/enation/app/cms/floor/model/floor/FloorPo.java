package com.enation.app.cms.floor.model.floor;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 楼层实体
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月13日 下午8:52:15
 */
@ApiModel
public class FloorPo implements Serializable {

	private static final long serialVersionUID = 8767541084879348185L;
	/**楼层id*/
	@ApiModelProperty(hidden = true)
	private Integer id;
	/**楼层类型 模版主键*/
	@ApiModelProperty(value = "模版主键")
	private Integer tpl_id;
	/**楼层名称*/
	@ApiModelProperty(value = "楼层名称", example = "服装鞋帽")
	@NotNull(message = "楼层名称不能为空")
	private String name;
	/**楼层排序*/
	@ApiModelProperty(value = "楼层排序", example = "1")
	@NotNull(message = "楼层排序不能为空")
	private Integer sort;
	/**楼层是否显示 1为显示,0为不显示*/
	@ApiModelProperty(value = "楼层是否显示 1为显示,0为不显示", example = "1")
	@Range(min = 0, max = 1, message = "楼层是否显示传入参数有误")
	private Integer is_display;
	/**楼层锚点文字 只有当楼层类型 type=1时才有*/
	@ApiModelProperty(value = "楼层锚点文字 只有当楼层类型 type=1时才有", example = "一楼服饰")
	private String anchor_words;
	/**楼层锚点图片 只有当楼层类型 type=1时才有*/
	@ApiModelProperty(value = "楼层锚点图片 只有当楼层类型 type=1时才有", example = "fs://adfaf")
	private String anchor_url;
	/**楼层颜色风格  只有当楼层类型 type=1时才有*/
	@ApiModelProperty(value = "楼层颜色风格  只有当楼层类型 type=1时才有", example = "#000000")
	private String floor_color;
	/**客户端类型*/
	private String client_type;

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTpl_id() {
		return tpl_id;
	}

	public void setTpl_id(Integer tpl_id) {
		this.tpl_id = tpl_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIs_display() {
		return is_display;
	}

	public void setIs_display(Integer is_display) {
		this.is_display = is_display;
	}

	public String getAnchor_words() {
		return anchor_words;
	}

	public void setAnchor_words(String anchor_words) {
		this.anchor_words = anchor_words;
	}

	public String getAnchor_url() {
		return anchor_url;
	}

	public void setAnchor_url(String anchor_url) {
		this.anchor_url = anchor_url;
	}

	public String getFloor_color() {
		return floor_color;
	}

	public void setFloor_color(String floor_color) {
		this.floor_color = floor_color;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

}
