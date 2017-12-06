package com.enation.app.cms.floor.model.floor;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;

/**
 * 
 * 前台输出楼层数据使用，以前的cmsfrontfloor
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午8:59:34
 */
@ApiModel
public class FrontFloor extends FloorPo implements Serializable {

	private static final long serialVersionUID = -3804123154843548603L;

	private List<FrontPanel> panelList;

	public List<FrontPanel> getPanelList() {
		return panelList;
	}

	public void setPanelList(List<FrontPanel> panelList) {
		this.panelList = panelList;
	}

}
