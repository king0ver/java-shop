package com.enation.app.cms.floor.model.floor;

import java.io.Serializable;
/**
 * 
 * 前台输出楼层数据使用,最终会以FrontFloor形式输出 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午8:57:01
 */
public class FrontPanel implements Serializable {
	
	private static final long serialVersionUID = 6995894521478111770L;
	/**模版名*/
	private String panel_name;
	/**模版内容*/
	private String panel_html;

	public String getPanel_name() {
		return panel_name;
	}

	public void setPanel_name(String panel_name) {
		this.panel_name = panel_name;
	}

	public String getPanel_html() {
		return panel_html;
	}

	public void setPanel_html(String panel_html) {
		this.panel_html = panel_html;
	}

}
