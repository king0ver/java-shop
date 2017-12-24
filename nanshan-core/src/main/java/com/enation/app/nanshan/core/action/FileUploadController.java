package com.enation.app.nanshan.core.action;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.framework.action.GridController;
import com.enation.framework.util.FileUtil;

/**
 * 上传文件
 * @author jianjianming
 * @version $Id: FileUploadController.java,v 0.1 2017年12月24日 下午4:18:02$
 */
@Controller 
@Scope("prototype")
@RequestMapping("/core/admin/nanshan")
public class FileUploadController extends GridController{

	@Autowired
	private UploadFactory uploadFactory;

	/**
	 * 上传附件
	 * @param file 附件
	 * @param fileFileName 附件名称
	 * @param subFolder 附件存放文件夹
	 * @param path 上传后的图片路径
	 * @param ajax 是否为异步提交
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upload")
	public JSONObject uploadFile(MultipartFile file){
		JSONObject json = new JSONObject();
		String path = null;
		if (file != null && file.getOriginalFilename() != null) {
			try{
				if(!FileUtil.isAllowUpImg(file.getOriginalFilename())){
					throw new IllegalArgumentException("不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
				}
				IUploader uploader= uploadFactory.getUploader();
				path = uploader.upload(file);
			}catch(IllegalArgumentException e){
				throw new IllegalArgumentException(e.toString());
			}
			json.put("url", path);
		}else{
			throw new IllegalArgumentException("没有文件");
		}
		return json;
	}
	
	
}
