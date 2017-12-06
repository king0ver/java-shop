package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.enation.app.cms.floor.service.Element;
import com.enation.framework.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonRawValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 楼层布局 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:30:38
 */
@ApiModel(description = "楼层布局")
public class Layout implements Serializable, Element {

	private static final long serialVersionUID = -5078038431207226277L;

	@Override
	public void buildSelf(Map map) {
		this.layout_id = (String) map.get("layout_id");
		this.sort = StringUtil.toInt("" + map.get("sort"), 0);
	}

	public Layout() {
		block_list = new ArrayList();
	}
	/**区块标识*/
	@ApiModelProperty(value = "区块标识")
	private String layout_id;
	/**区块列表*/
	@ApiModelProperty(value = "区块列表")
	private List<Block> block_list;
	/**排序*/
	@ApiModelProperty(value = "排序")
	private int sort;

	@JsonRawValue
	private String panel_data;

	public void addBlock(Block block) {
		this.block_list.add(block);
	}

	public String getLayout_id() {
		return layout_id;
	}

	public void setLayout_id(String layout_id) {
		this.layout_id = layout_id;
	}

	public List<Block> getBlock_list() {
		return block_list;
	}

	public void setBlock_list(List<Block> block_list) {
		this.block_list = block_list;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getPanel_data() {
		return panel_data;
	}

	public void setPanel_data(String panel_data) {
		this.panel_data = panel_data;
	}

	

}
