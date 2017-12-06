package com.enation.app.shop.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

/**
 * 消息管理类
 * @author Kanon
 *
 */
@Service
public class MessageManager implements IMessageManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IMessageManger#listPage(java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = true)  
	public Page listPage(Integer pageNo,Integer pageSize,Integer type) {

		String sql="SELECT * FROM es_message_template WHERE type=?";
		return daoSupport.queryForPage(sql, pageNo, pageSize, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IMessageManger#editMessageTemplate(com.enation.app.base.core.model.message.MessageTemplate)
	 */
	@Override
	public void editMessageTemplate(MessageTemplate messageTemplate) {
		
		this.daoSupport.update("es_message_template", messageTemplate,"id="+messageTemplate.getId() );
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IMessageManger#getMessageTemplate(java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = true)
	public MessageTemplate getMessageTemplate(Integer id) {
		
		String sql="SELECT * FROM es_message_template WHERE id=?";
		return this.daoSupport.queryForObject(sql, MessageTemplate.class, id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IMessageManager#addMessageTemplate(com.enation.app.base.core.model.message.MessageTemplate)
	 */
	@Override
	public void addMessageTemplate(MessageTemplate messageTemplate) {

		this.daoSupport.insert("es_message_template", messageTemplate);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IMessageManager#getMessageTemplate(java.lang.String)
	 */
	@Override
	public MessageTemplate getMessageTemplate(String code) {
		
		String sql="SELECT * FROM es_message_template WHERE tpl_code=?";
		return this.daoSupport.queryForObject(sql, MessageTemplate.class, code);
	}


}
