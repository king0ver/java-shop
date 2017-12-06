package com.enation.app.base.upload.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.framework.cache.ICache;
import com.enation.framework.context.spring.SpringContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.upload.model.po.Upload;
import com.enation.app.base.upload.model.vo.UploadPluginVo;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.base.upload.service.IUploadManager;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.log.LogType;

/**
 * 存储方案Manager
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日下午12:01:09
 *
 */
@Service("upLoaderManager")
public class UploadManager implements IUploadManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private List<IUploader> uploaders;
	@Autowired
	private ICache cache;
	// 缓存前缀
	private static final String CACHE_PREFIX = "uploader_param_";

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Log(type = LogType.SETTING, detail = "ID为${id}存储方案设置为默认启用的存储方案")
	public void open(String pluginId) {
		List<UploadPluginVo> vos = this.getAllPlugins();
		for (UploadPluginVo vo : vos) {
			Upload upload = new Upload(vo);
			this.save(vo);
		}
		this.daoSupport.execute("UPDATE es_uploader SET up_open=0");
		this.daoSupport.execute("UPDATE es_uploader SET up_open=1 WHERE up_bean_id = ?", pluginId);

	}

	@Override
	public Upload getUploadByBeanId(String beanId) {
		if (beanId == null) {
			return null;
		}
		return this.daoSupport.queryForObject("SELECT * FROM es_uploader WHERE up_bean_id = ?", Upload.class, beanId);
	}

	@Override
	public List<UploadPluginVo> getAllPlugins() {
		List<UploadPluginVo> resultList = new ArrayList<>();
		// 查询数据库中的支付方式
		String sql = "select * from es_uploader ";
		List<Upload> list = this.daoSupport.queryForList(sql, Upload.class);
		Map<String, Upload> map = new HashMap<>();

		for (Upload upload : list) {
			map.put(upload.getUp_bean_id(), upload);
		}
		for (IUploader plugin : uploaders) {
			Upload upload = map.get(plugin.getPluginId());
			UploadPluginVo result = null;

			if (upload != null) {// 数据库中已经有支付方式
				result = new UploadPluginVo(upload);
			} else {
				result = new UploadPluginVo(plugin);
			}

			resultList.add(result);
		}

		return resultList;
	}

	@Override
	public UploadPluginVo getByPluginId(String plugin_id) {
		Upload upload = this.getUploadByBeanId(plugin_id);
		if (upload == null) {
			IUploader plugin = SpringContextHolder.getBean(plugin_id);

			return new UploadPluginVo(plugin);
		}
		return new UploadPluginVo(upload);
	}

	@Override
	public UploadPluginVo save(UploadPluginVo vo) {
		Upload upload = new Upload(vo);
		if (vo.getId() == 0) {
			this.daoSupport.insert("es_uploader", upload);
		} else {
			this.daoSupport.update("es_uploader", upload, "id=" + vo.getId());
		}
		// 更新缓存
		cache.put(CACHE_PREFIX + upload.getUp_bean_id(), "");
		return vo;
	}

}
