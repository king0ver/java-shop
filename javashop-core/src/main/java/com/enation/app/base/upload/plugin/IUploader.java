package com.enation.app.base.upload.plugin;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enation.app.base.core.model.ConfigItem;

/**
 * 存储方案参数接口
 * 
 * @author mengyuanming
 * @version 1.0.0
 * @since v6.4.0
 * @date 2017年8月10日下午10:01:20
 */
public interface IUploader {

	/**
	 * 配置各个存储方案的参数
	 * 
	 * @return 参数列表
	 */
	public List<ConfigItem> definitionConfigItem();

	/**
	 * 上传文件
	 * @param stream	  MultipartFile文件
	 * @return url字符串
	 */
	public String upload(MultipartFile stream);
	/**
	 * 删除文件
	 * @param filePath	文件全路径
	 */
	public void deleteFile(String filePath);

	/**
	 * 获取插件ID
	 * @return 插件beanId
	 */
	public String getPluginId();
	
	/**
	 * 获取插件名称
	 * @return 插件名称
	 */
	public String getPluginName();
	
	/**
	 * 存储方案是否开启 0 不开启  1 开启
	 * @return 
	 */
	public Integer getIsOpen();
	
	/**
	 * 生成缩略图路径
	 * @param url	原图片全路径
	 * @param width	需要生成图片尺寸的宽
	 * @param height	需要生成图片尺寸的高
	 * @return	生成的缩略图路径
	 */
	public String getThumbnailUrl(String url,Integer width,Integer height);

}
