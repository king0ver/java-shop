package com.enation.app.base.upload.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.model.vo.UploadPluginVo;
import com.enation.app.base.upload.service.IUploadManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 存储方案
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日上午11:50:24
 *
 */
@Api(description="存储方案controller")
@RestController
@RequestMapping("/shop-core/admin/uploader")
@Validated
public class UploadController extends GridController {

	@Autowired
	private IUploadManager uploaderManager;
	
	@Autowired
	private UploadFactory uploadFactory;


	@ApiOperation("后台获取所有存储方案插件")
	@GetMapping("/uploader-list-json")
	public GridJsonResult list_json() {
		return JsonResultUtil.getGridJson(uploaderManager.getAllPlugins());
	}

	@ApiOperation("后台开启存储方案")
	@PostMapping("/set-open")
	public JsonResult setOpen(@RequestBody String pluginId) {
		try {
			pluginId = pluginId.substring(0,pluginId.length() - 1);
			/** 修改启用状态 */
			this.uploaderManager.open(pluginId);
			/** 通过beanid 找到IUploader */
			UploadPluginVo vo = this.uploaderManager.getByPluginId(pluginId);
			/** 工厂重新赋值 */
			uploadFactory.setUpload(vo);
			return JsonResultUtil.getSuccessJson("设置成功");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("出现错误，请稍后重试");
		}
	}
	
	@ApiOperation("后台保存")
	@PostMapping
	public UploadPluginVo save(@RequestBody UploadPluginVo vo){
		uploaderManager.save(vo);
		return vo;
	}
}
