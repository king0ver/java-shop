package com.enation.app.shop.shop.rdesign.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.shop.shop.rdesign.model.StorePhotos;
import com.enation.app.shop.shop.rdesign.service.IStorePhotosManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
/**
 * 
 * (店铺相册管理类) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年4月10日 上午10:53:21
 */
@Service("storePhotosManager")
public class StorePhotosManager implements IStorePhotosManager {
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private UploadFactory uploadFactory;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreSildeManager#list(java.lang.Integer)
	 */
	@Override
	public List<StorePhotos> list(Integer store_id) {
		String sql="select * from es_store_photos where store_id=?";
		List<StorePhotos> list=this.daoSupport.queryForList(sql, StorePhotos.class, store_id);
		return list;
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStorePhotosManager#add(java.lang.Integer, java.lang.String)
	 */
	@Override
	public void add(Integer store_id, String img) {
		Map map = new HashMap();
		map.put("store_id", store_id);
		map.put("img", img);
		this.daoSupport.insert("es_store_photos", map);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStorePhotosManager#delete(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void delete(Integer store_id, Integer photo_id) {
		String sql = "select * from es_store_photos where store_id=? and photo_id=? ";
		StorePhotos storePhotos = this.daoSupport.queryForObject(sql, StorePhotos.class, store_id,photo_id);
		this.deletePohto(storePhotos.getImg());
		sql = "delete from es_store_photos where store_id=? and photo_id=?";
		this.daoSupport.execute(sql, store_id,photo_id);	
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.store.service.IStorePhotosManager#getStorePhotos(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public StorePhotos getStorePhotos(Integer store_id, Integer photo_id) {
		String sql = "select * from es_store_photos where store_id=? and photo_id=? ";
		return this.daoSupport.queryForObject(sql, StorePhotos.class, store_id,photo_id);
		
	}
	/**
	 * 删除指定的图片
	 * 
	 */
	private void deletePohto(String photoName) {
		if (photoName != null) {
			String static_server_path = null;
			photoName = photoName.replaceAll(EopSetting.FILE_STORE_PREFIX, static_server_path );
			IUploader uploader=uploadFactory.getUploader();
			uploader.deleteFile(photoName);
		}
	}
	
}
