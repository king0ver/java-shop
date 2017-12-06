package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.enation.app.cms.floor.model.floor.FloorPo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 为后台楼层装修提供数据用
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月11日 下午5:06:56
 */
public class Floor extends FloorPo implements Serializable {

	private static final long serialVersionUID = 8053517748509372356L;
	/**面板列表*/
	@ApiModelProperty(value = "面板列表")
	private List<Panel> panelList;
	public Floor() {
		panelList = new ArrayList();
	}
	/**
	 * 添加楼层时向cms_floorcontent添加的数据模型
	 * @param panel
	 */
	public void addPanel(Panel panel) {
		this.panelList.add(panel);
	}

	public List<Panel> getPanelList() {
		return panelList;
	}

	public void setPanelList(List<Panel> panelList) {
		this.panelList = panelList;
	}

}
