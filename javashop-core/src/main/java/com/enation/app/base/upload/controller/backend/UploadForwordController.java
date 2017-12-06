package com.enation.app.base.upload.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.upload.service.IUploadManager;

import io.swagger.annotations.Api;

@Api(description="后台存储方案跳转api")
@Controller
@RequestMapping("/shop/admin/uploader")
@Validated
public class UploadForwordController {
	
	@Autowired
	private IUploadManager uploadManager;
	
	/**
	 * 跳转至存储方案列表
	 * @return 存储方案列表
	 */
	@RequestMapping("/uploader-list")
	public String list(){
		return "/upload/upload-list";
	}
	
	/**
	 * 跳到修改界面
	 * @param paymentId
	 * @return
	 */
	@RequestMapping("/{plugin_id}")
	public ModelAndView  edit(@PathVariable String plugin_id){
		ModelAndView view= new ModelAndView();
		view.setViewName("/upload/upload-edit");
		view.addObject("uploadPluginVo", uploadManager.getByPluginId(plugin_id));
		return view;
	}
}
