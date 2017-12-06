package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.enation.app.cms.floor.model.floor.PanelPo;
import com.enation.app.cms.floor.service.Element;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 楼层面板 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:32:12
 */
public class Panel extends PanelPo implements Serializable, Element {

	private static final long serialVersionUID = -3816556562229877447L;
 
	/**
	 * 用来返回给前台的
	 */
	@Override
	public void buildSelf(Map map) {

		this.id = StringUtil.toInt("" + map.get("id"), null);
//		this.panel_id = (String) map.get("panel_id");
		this.tpl_type = (String) map.get("tpl_type");
		this.tpl_content = (String) map.get("tpl_content");
		this.sort = StringUtil.toInt("" + map.get("sort"), 0);
		this.panel_name = (String) map.get("panel_name");

	}

	private String tpl_type;
	private String tpl_content;

	public Panel() {
		layoutList = new ArrayList();
	}
//	/**区块标识*/
//	@ApiModelProperty(value = "区块标识")
//	private String panel_id;
	/**排序*/
	@ApiModelProperty(value = "排序")
	private int sort;
	/**布局列表*/
	@ApiModelProperty(value = "布局列表")
	private List<Layout> layoutList;

	/**
	 * 添加一个布局
	 * 
	 * @param layout
	 */
	public void addLayout(Layout layout) {
		this.layoutList.add(layout);
	}

//	public String getPanel_id() {
//		return panel_id;
//	}
//
//	public void setPanel_id(String panel_id) {
//		this.panel_id = panel_id;
//	}

	 

	public List<Layout> getLayoutList() {
		return layoutList;
	}

	public void setLayoutList(List<Layout> layoutList) {
		this.layoutList = layoutList;
	}

	

	public String getTpl_type() {
		return tpl_type;
	}

	public void setTpl_type(String tpl_type) {
		this.tpl_type = tpl_type;
	}

	public String getTpl_content() {
		return tpl_content;
	}

	public void setTpl_content(String tpl_content) {
		this.tpl_content = tpl_content;
	}
 

}
