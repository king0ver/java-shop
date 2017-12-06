package com.enation.app.shop.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.message.model.StoreNoticeLog;
import com.enation.app.shop.message.model.StoreSMSLog;
import com.enation.app.shop.message.model.enums.ShopNoticeType;
import com.enation.app.shop.message.service.IStoreMessageManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺消息管理类
 * @author Kanon
 * @since 6.4.0
 * @version 1.0 
 * 2017-8-16
 */
@Service
public class StoreMessageManager implements IStoreMessageManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IStoreMessageManager#getStoreMessageList(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getStoreMessageList(Integer pageNo, Integer pageSize,Integer store_id, String type) {

		String sql="SELECT * FROM es_store_notice_log WHERE store_id=? AND is_delete=0 ";
		
		if(!StringUtil.isEmpty(type)){
			sql+=" AND type = '" + type +"'";
		}
		sql+=" ORDER BY id DESC";
		return daoSupport.queryForPage(sql, pageNo, pageSize, store_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IStoreMessageManager#delete(java.lang.Integer)
	 */
	@Override
	public void delete(String ids) {

		this.daoSupport.execute("UPDATE es_store_notice_log  SET is_delete=1 WHERE id in ("+ids+")");
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.order.service.message.IStoreMessageManager#read(java.lang.Integer[])
	 */
	@Override
	public void read(String ids) {
		String sql = "UPDATE es_store_notice_log SET is_read =1 WHERE id IN ("+ids+")";		
		this.daoSupport.execute(sql);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreMessageManager#addStoreSmsContent(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void addStoreSmsContent(String smsContent, Integer storeId) {
		StoreSMSLog storeSMSLog=new StoreSMSLog();
		storeSMSLog.setStore_id(storeId);
		storeSMSLog.setSms_content(smsContent);
		storeSMSLog.setSend_time(DateUtil.getDateline());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStoreMessageManager#addStoreContent(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void addStoreContent(String content, Integer storeId,ShopNoticeType type) {
		StoreNoticeLog storeNoticeLog=new StoreNoticeLog();
		storeNoticeLog.setStore_id(storeId);
		storeNoticeLog.setNotice_content(content);
		storeNoticeLog.setSend_time(DateUtil.getDateline());
		storeNoticeLog.setType(type.name());
		daoSupport.insert("es_store_notice_log", storeNoticeLog);
		
	}

}
