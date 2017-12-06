
package com.enation.app.base.upload.controller.font;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.JsonResultUtil;

/**
 * 附件上传
 * 
 * @author kingapex 2010-3-10下午04:24:47
 * @author Kanon 2015-12-16 version 1.1 添加注释
 * @author Kanon 2016-3-1;version 6.0版本
 */
@Controller
@RequestMapping("/core")
public class UploadApiController {
	
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
	public String uploadFile(MultipartFile file){
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
			return path;
		}else{
			throw new IllegalArgumentException("没有文件");
		}
	}
	
	/**
	 * 删除图片
	 * 根据图片路径删除图片
	 * @param picname  图片路径
	 * @return 删除状态
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonResult delete(String picname) {
		IUploader uploader= uploadFactory.getUploader();
		
		try {
			uploader.deleteFile(picname);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("删除失败");
		}
		return JsonResultUtil.getErrorJson("删除成功");
	}


}
