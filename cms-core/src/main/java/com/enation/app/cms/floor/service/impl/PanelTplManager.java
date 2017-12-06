package com.enation.app.cms.floor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.po.PanelTpl;
import com.enation.app.cms.floor.service.IPanelTplManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

/**
 * 
 * 楼层面板功能的实现 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午9:31:55
 */
@Service("panelTplManager")
public class PanelTplManager implements IPanelTplManager {
	@Autowired
	private IDaoSupport daoSupport;

	/**
	 * 根据客户端类型获得面板列表
	 */
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.cms.floor.service.IPanelTplManager#list(int, int, java.lang.String)
	 */
	@Override
	public Page list(int page, int pageSize, String client_type) {
		return this.daoSupport.queryForPage("select * from es_cms_panel_tpl where client_type=?", page, pageSize,
				client_type);
	}

	/**
	 * 添加面板
	 */
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.cms.floor.service.IPanelTplManager#add(com.enation.app.cms.floor.model.po.PanelTpl)
	 */
	@Override
	public PanelTpl add(PanelTpl panelTpl) {
		this.daoSupport.insert("es_cms_panel_tpl", panelTpl);
		Integer tpr_id = this.getFirst();
		panelTpl.setTpl_id(tpr_id);
		return panelTpl;
	}
	/**
	 * 得到id最大的数据，为了添加完跳转到当前添加数据的修改页面
	 * @return
	 */
	private Integer getFirst() {
		return this.daoSupport.queryForInt("select max(tpl_id) from es_cms_panel_tpl");
	}
/*
 * (non-Javadoc)
 * @see com.enation.app.cms.floor.service.IPanelTplManager#delete(java.lang.Integer)
 */
	@Override
	public void delete(Integer id) {
		this.daoSupport.execute("delete from es_cms_panel_tpl where tpl_id=?", id);
	}
/*
 * (non-Javadoc)
 * @see com.enation.app.cms.floor.service.IPanelTplManager#edit(com.enation.app.cms.floor.model.po.PanelTpl)
 */
	@Override
	public void edit(PanelTpl panelTpl) {
		this.daoSupport.update("es_cms_panel_tpl", panelTpl, "tpl_id=" + panelTpl.getTpl_id());
	}
/*
 * (non-Javadoc)
 * @see com.enation.app.cms.floor.service.IPanelTplManager#get(java.lang.Integer)
 */
	@Override
	public PanelTpl get(Integer id) {
		return this.daoSupport.queryForObject("select * from es_cms_panel_tpl where tpl_id=?", PanelTpl.class, id);
	}

	/**
	 * 得到主面板
	 */
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.cms.floor.service.IPanelTplManager#getMain(java.lang.String)
	 */
	@Override
	public List<PanelTpl> getMain(String client_type) {
		StringBuffer sql = new StringBuffer("select * from es_cms_panel_tpl where tpl_type = 'main' ");
		List<String> paramList = new ArrayList<String>();
		sql.append(" and client_type = ? ");
		paramList.add(client_type);
		return this.daoSupport.queryForList(sql.toString(), PanelTpl.class, paramList.toArray());
	}

	/**
	 * 得到所有普通模版
	 */
/*
 * (non-Javadoc)
 * @see com.enation.app.cms.floor.service.IPanelTplManager#getNormal()
 */
	@Override
	public List<PanelTpl> getNormal() {
		String sql = "select * from es_cms_panel_tpl where tpl_type = 'normal'";
		return this.daoSupport.queryForList(sql, PanelTpl.class);
	}

	/**
	 * 根据主键获取面板内容
	 */
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.cms.floor.service.IPanelTplManager#getPanelTplContent(java.lang.Integer)
	 */
	@Override
	public String getPanelTplContent(Integer tpl_id) {
		String sql = "select tpl_content from es_cms_panel_tpl where tpl_id=?";
		String content = this.daoSupport.queryForString(sql, tpl_id);
		return content;
	}
}
