package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 楼层名及锚点
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月9日 下午3:31:51
 */
public class FloorAnchor implements Serializable {

	private static final long serialVersionUID = 7933663223091616606L;
	@ApiModelProperty(value = "楼层名称", example = "服装鞋帽")
	private String name;
	@ApiModelProperty(value = "楼层id", example = "1")
	private Integer id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
