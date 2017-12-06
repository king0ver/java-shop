package com.enation.app.cms.floor.model.floor;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * 楼层设计模型 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午8:51:13
 */
public class FloorDesign extends FloorPo implements Serializable {
	
	private static final long serialVersionUID = -9128375250173273766L;
	private List<PanelPo> panelList;

	public List<PanelPo> getPanelList() {
		return panelList;
	}

	public void setPanelList(List<PanelPo> panelList) {
		this.panelList = panelList;
	}

}
