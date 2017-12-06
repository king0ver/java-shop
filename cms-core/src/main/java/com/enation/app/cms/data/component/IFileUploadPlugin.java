package com.enation.app.cms.data.component;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 * 
 * @author Chopper
 * @version v1.0
 * @since pangu1.0 2017年5月19日 下午2:23:03
 *
 */
public interface IFileUploadPlugin {

	/**
	 * 标识
	 * 
	 * @return
	 */
	public String plugin();

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @return
	 */
	public String upload(MultipartFile file);
}
