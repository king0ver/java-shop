package com.enation.app.cms.focuspic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.enumeration.ClientType;
import com.enation.app.cms.floor.model.enumeration.CmsManageMsgType;
import com.enation.app.cms.floor.model.vo.CmsManageMsg;
import com.enation.app.cms.focuspic.model.CmsEntityUtil;
import com.enation.app.cms.focuspic.model.po.CmsFocusPicture;
import com.enation.app.cms.focuspic.model.vo.CmsFrontFocusPicture;
import com.enation.app.cms.focuspic.producer.FocusProducer;
import com.enation.app.cms.focuspic.service.IFocusPictureManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 焦点图功能实现
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日 上午11:24:47
 */
@Service
public class FocusPictureManager implements IFocusPictureManager {
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private FocusProducer focusProducer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.cms.focuspic.service.IFocusPictureManager#addFocus(com.
	 * enation.app.cms.focuspic.model.po.CmsFocusPicture)
	 */
	@Override
	public CmsFocusPicture addFocus(CmsFocusPicture cmsFocusPicture) {
		// 增加逻辑
		String sql = "select count(0) from es_cms_focuspic where client_type=?";
		Integer count = this.daoSupport.queryForInt(sql,cmsFocusPicture.getClient_type());
		if (count >= 5) {
			throw new UnProccessableServiceException(ErrorCode.FOCUS_OVER_LIMIT, "焦点图数量不能超过五张");
		}
		cmsFocusPicture.setOperation_url(CmsEntityUtil.createOperationUrl(cmsFocusPicture.getOperation_type(),
				cmsFocusPicture.getOperation_param(),cmsFocusPicture.getClient_type()));
		this.daoSupport.insert("es_cms_focuspic", cmsFocusPicture);
		cmsFocusPicture.setId(this.daoSupport.getLastId("es_cms_focuspic"));
		/**
		 * 重点：下面注释的代码不要删
		 */
		// 根据不同的类型发送不同的消息
		// if (ClientType.pc.name().equals(cmsFocusPicture.getClient_type())) {
		// focusProducer.sendPcMessage(CmsManageMsgType.NAV, null,
		// CmsManageMsg.ADD_OPERATION);
		// }else if (ClientType.mobile.name().equals(cmsFocusPicture.getClient_type()))
		// {
		// focusProducer.sendMobileMessage(CmsManageMsgType.NAV, null,
		// CmsManageMsg.ADD_OPERATION);
		// }
		return cmsFocusPicture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.focuspic.service.IFocusPictureManager#deleteFocus(java.
	 * lang.Integer)
	 */
	@Override
	public void deleteFocus(Integer id) {
		// 删除逻辑
		String sql = "delete from es_cms_focuspic where id = ?";
		this.daoSupport.execute(sql, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.focuspic.service.IFocusPictureManager#updateFocus(com.
	 * enation.app.cms.focuspic.model.po.CmsFocusPicture)
	 */
	@Override
	public CmsFocusPicture updateFocus(CmsFocusPicture cmsFocusPicture) {
		// 修改逻辑
		cmsFocusPicture.setOperation_url(CmsEntityUtil.createOperationUrl(cmsFocusPicture.getOperation_type(),
				cmsFocusPicture.getOperation_param(),cmsFocusPicture.getClient_type()));
		this.daoSupport.update("es_cms_focuspic", cmsFocusPicture, "id = " + cmsFocusPicture.getId());
		return cmsFocusPicture;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.cms.focuspic.service.IFocusPictureManager#getFocus(java.lang.
	 * Integer)
	 */
	@Override
	public CmsFocusPicture getFocus(Integer id) {
		// 查询逻辑
		String sql = "select * from es_cms_focuspic where id = ?";
		return this.daoSupport.queryForObject(sql, CmsFocusPicture.class, id);
	}

	@Override
	public List<CmsFocusPicture> getAllFocusList(String client_type) {
		// 批量查询逻辑 不需要分页
		String sql = "select * from es_cms_focuspic where client_type=? order by id asc";
		return this.daoSupport.queryForList(sql, CmsFocusPicture.class, client_type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.cms.focuspic.service.IFocusPictureManager#
	 * getCmsFrontFocusPicture()
	 */
	@Override
	public List<CmsFrontFocusPicture> getCmsFrontFocusPicture(String client_type) {
		// 前台获取焦点图vo
		String sql = "select * from es_cms_focuspic where client_type=? order by id asc";
		return this.daoSupport.queryForList(sql, CmsFrontFocusPicture.class,client_type);
	}

}
