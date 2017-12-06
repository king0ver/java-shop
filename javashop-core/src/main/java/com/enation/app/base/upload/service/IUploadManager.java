package com.enation.app.base.upload.service;

import java.util.List;

import com.enation.app.base.upload.model.po.Upload;
import com.enation.app.base.upload.model.vo.UploadPluginVo;

/**
 * 存储方案接口
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日上午11:57:43
 *
 */
public interface IUploadManager {

	/**
	 * 设置存储方案是否启用
	 * 
	 * @param 存储方案id
	 */
	public void open(String pluginId);

	/**
	 * 根据beanId获取存储方案
	 * 
	 * @param beanId
	 *            存储方案beanId
	 * @return 存储方案
	 */
	public Upload getUploadByBeanId(String beanId);

	/**
	 * 后台获取存储插件列表
	 * 
	 * @return
	 */
	public List<UploadPluginVo> getAllPlugins();

	/**
	 * 根据插件id返回存储方案vo
	 * 
	 * @param plugin_id
	 *            插件id
	 * @return
	 */
	public UploadPluginVo getByPluginId(String plugin_id);

	/**
	 * 保存
	 * 
	 * @param vo
	 * @return
	 */
	public UploadPluginVo save(UploadPluginVo vo);

}
