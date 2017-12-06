package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * app panel
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月20日 下午7:42:55
 */
public class MobilePanel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6142618253691288947L;
	
	
	@ApiModelProperty(value = "面板模板id")
	private int panel_tpl_id;
	
	@ApiModelProperty(value = "区块集合")
	private List<Map> blockList;
	
	public int getPanel_tpl_id() {
		return panel_tpl_id;
	}
	public void setPanel_tpl_id(int panel_tpl_id) {
		this.panel_tpl_id = panel_tpl_id;
	}
	public List<Map> getBlockList() {
		return blockList;
	}
	public void setBlockList(List<Map> blockList) {
		this.blockList = blockList;
	}
	
	
	
}
