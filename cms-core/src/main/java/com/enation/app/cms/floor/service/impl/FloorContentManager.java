package com.enation.app.cms.floor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.cms.floor.model.enumeration.ClientType;
import com.enation.app.cms.floor.model.enumeration.CmsManageMsgType;
import com.enation.app.cms.floor.model.floor.FloorDesign;
import com.enation.app.cms.floor.model.floor.PanelPo;
import com.enation.app.cms.floor.model.vo.CmsManageMsg;
import com.enation.app.cms.floor.model.vo.Panel;
import com.enation.app.cms.floor.producer.FloorProducer;
import com.enation.app.cms.floor.service.IFloorContentManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 楼层内容管理实现类
 * 
 * @author yanlin
 * @version v1.0
 * @since v1.0
 * @date 2017年8月11日 下午3:18:32
 */
@Service
public class FloorContentManager implements IFloorContentManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private FloorProducer floorProducer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorContentManager#deleteByFloorId(java.
	 * lang.Integer)
	 */
	@Override
	public void deleteByFloorId(Integer id) {
		String sql = "delete from es_cms_floor_panel where floor_id = ?";
		this.daoSupport.execute(sql, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorContentManager#design(com.enation.app
	 * .cms.floor.model.floor.FloorDesign)
	 */
	@Override
	public FloorDesign design(FloorDesign floorDesign) {
		int floorid = floorDesign.getId();
		List<PanelPo> panelList = floorDesign.getPanelList();

		for (PanelPo panel : panelList) {
			Integer panelid = panel.getId();
			if (panelid == null || panelid.equals(0)) {
				panel.setPanel_name("新版块");
				panel.setFloor_id(floorid);
				this.daoSupport.insert("es_cms_floor_panel", panel);
			} else {
				this.daoSupport.execute("update es_cms_floor_panel set  panel_data=? where id=?",
						 panel.getPanel_data(), panelid);
			}

		}
		/**
		 * 重点：下面注释的代码不要删
		 */
		// 根据不同的类型发送不同的消息
		if (ClientType.pc.name().equals(floorDesign.getClient_type())) {
			floorProducer.sendPcMessage(CmsManageMsgType.INDEX, null, CmsManageMsg.ADD_OPERATION);
		} else if (ClientType.mobile.name().equals(floorDesign.getClient_type())) {
			floorProducer.sendMobileMessage(CmsManageMsgType.INDEX, null, CmsManageMsg.ADD_OPERATION);
		}
		return floorDesign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorContentManager#deleteFromById(java.
	 * lang.Integer)
	 */
	@Override
	public void deleteFromById(Integer id) {
		String sql = "delete from es_cms_floor_panel where id = ?";
		this.daoSupport.execute(sql, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorContentManager#updatePanelName(com.
	 * enation.app.cms.floor.model.vo.Panel)
	 */
	@Override
	public void updatePanelName(Panel panel) {
		this.daoSupport.execute("update es_cms_floor_panel set panel_name = ? where id = ?", panel.getPanel_name(),
				panel.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.floor.service.IFloorContentManager#addPanel(com.enation.
	 * app.cms.floor.model.floor.PanelPo)
	 */
	@Override
	@Transactional
	public PanelPo addPanel(PanelPo panel) {
		if (StringUtil.isEmpty(panel.getPanel_name())) {
			panel.setPanel_name("新版块");
		}
		//查询该楼层的最大的排序
		String sql = "select * from es_cms_floor_panel where floor_id = ? order by sort desc limit 0,1";
		PanelPo maxpanel = this.daoSupport.queryForObject(sql, PanelPo.class, panel.getFloor_id());
		if(maxpanel==null){
			panel.setSort(1);
		}else{
			panel.setSort(maxpanel.getSort()+1);
		}
		this.daoSupport.insert("es_cms_floor_panel", panel);
		int id = this.daoSupport.getLastId("es_cms_floor_panel");
		panel.setId(id);
		return panel;
	}

	@Override
	public PanelPo updatePanelSort(Integer id, String sort) {
		
		String sql = "select * from es_cms_floor_panel where id = ?";
		
		PanelPo curPanel = this.daoSupport.queryForObject(sql, PanelPo.class, id);//要操作的板块
		
		if("up".equals(sort)){
			sql = "select * from es_cms_floor_panel where sort<? and floor_id=? order by sort desc limit 0,1";
		}else if("down".equals(sort)){
			sql = "select * from es_cms_floor_panel where sort>? and floor_id=? order by sort asc limit 0,1";
		}else{
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "排序参数错误");
		}
		PanelPo changePanel = this.daoSupport.queryForObject(sql, PanelPo.class,curPanel.getSort(),curPanel.getFloor_id());
		if(changePanel != null ){
			sql = "update es_cms_floor_panel set sort = ? where id = ?";
			this.daoSupport.execute(sql, changePanel.getSort(),curPanel.getId());
			this.daoSupport.execute(sql, curPanel.getSort(),changePanel.getId());
		}
		return curPanel;
	}

}
