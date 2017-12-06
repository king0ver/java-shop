package com.enation.app.base.upload;

import java.util.List;

import com.enation.framework.cache.ICache;
import com.enation.framework.validator.UnProccessableServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.upload.model.po.Upload;
import com.enation.app.base.upload.model.vo.UploadPluginVo;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.framework.database.IDaoSupport;

/**
 * 存储方案工厂
 * 
 * @author mengyuanming
 * @version  v1.1 增加beanid的缓存逻辑，因为如果管理后端分离的话，static为两个jvm，导致调无效
 * @since v6.4.0
 * @date 2017年8月14日上午11:57:04
 *
 */
@Component
public class UploadFactory {
	

	//所有的支付插件都会自动注入到这里
	@Autowired
	private List<IUploader> uploads;

	@Autowired
	private ICache cache;

	//缓存key，存储方案的beanid
	private  static  final  String CACHE_KEY ="uploader_bean_id";


	@Autowired
	private  IDaoSupport daoSupport;

	/**
	 * 获取插件
	 * 
	 * @return 实例化存储方案对象
	 */
	private IUploader findEnablePlugin() {
		String sql="SELECT * FROM es_uploader WHERE up_open = 1";
		Upload up= this.daoSupport.queryForObject(sql, Upload.class);
	 	if(up==null){
	 		throw  new UnProccessableServiceException("未找到可用的文件存储方案");
		}
		return this.findByBeanid(up.getUp_bean_id());
	}

	/**
	 * 获取存储方案对象
	 * 
	 * @return 实例化的存储方案对象
	 */
	public  IUploader getUploader() {

		//由缓存中读取中设置的存储方案beanid
		String beanid = this.getBeanIdFromCache();

		//如果为空则要到库中读取
		if (beanid == null) {

			//由数据库中查询存储方案
			IUploader uploader = this.findEnablePlugin();

			//找到存储方案，并将其id设置在缓存中
			cache.put(CACHE_KEY, uploader.getPluginId());
			return uploader;

		}else {
			IUploader uploader = this.findByBeanid(beanid);
			return uploader;
		}


	}


	/**
	 * 由缓存中读取中设置的存储方案beanid
	 * @return
	 */
	private String  getBeanIdFromCache(){
		return  (String)cache.get(CACHE_KEY);
	}


	/**
	 * 根据beanid获取出存储方案
	 * @param beanid
	 * @return
	 */
	private IUploader findByBeanid(String beanid){
		for (IUploader iUploader : uploads) {
			if (iUploader.getPluginId().equals( beanid)) {
				return iUploader;
			}
		}

		//如果走到这里，说明找不到可用的存储方案
		throw  new UnProccessableServiceException("未找到可用的文件存储方案");
	}

	/**
	 * 设置存储方案，这个方案的beanid会被缓存
	 * @param upload 要设置的存储方案
	 */
	public   void setUpload(UploadPluginVo vo) {
		cache.put(CACHE_KEY, vo.getUp_bean_id());

	}

}
