package com.enation.app.cms.floor.service;

import java.util.List;

import com.enation.app.cms.floor.model.po.PanelTpl;
import com.enation.framework.database.Page;

/**
 * 
 * 楼层模版操作接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * 2017年8月14日 下午1:24:39
 */
public interface IPanelTplManager {
	/**
	 * 楼层模版列表获取接口
	 * 
	 * @param page 页码
	 * @param pageSize 页码大小
	 * @param client_type 客户端类型
	 * @return Page
	 */
	public Page list(int page, int pageSize, String client_type);

	/**
	 * 楼层模版添加接口
	 * 
	 * @param panelTpl 面板模板
	 * @return PanelTpl 实体
	 */
	public PanelTpl add(PanelTpl panelTpl);

	/**
	 * 楼层模版删除接口
	 * 
	 * @param id 主键
	 */
	public void delete(Integer id);

	/**
	 * 楼层模版修改接口
	 * 
	 * @param panelTpl 面板模板
	 */
	public void edit(PanelTpl panelTpl);

	/**
	 * 根据id获取楼层模版接口
	 * 
	 * @param id 主键
	 * @return 面板模板
	 */
	public PanelTpl get(Integer id);

	/**
	 * 获取面板模板内容
	 * 
	 * @param tpl_id 模版id
	 * @return string
	 */
	String getPanelTplContent(Integer tpl_id);

	/**
	 * 获取所有主楼层面板
	 * 
	 * @return 面板模板集合
	 */
	public List<PanelTpl> getMain(String client_type);

	/**
	 * 获取所有普通楼层面板
	 * 
	 * @return PanelTpl集合
	 */
	public List<PanelTpl> getNormal();
}
