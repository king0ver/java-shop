package com.enation.app.base.upload.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.base.upload.model.po.Upload;
import com.enation.app.base.upload.plugin.IUploader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiModelProperty;

/**
 * 存储方案插件vo
 * 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0 2017年11月10日 下午2:04:21
 */
public class UploadPluginVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "存储方案id")
	private Integer id;

	@ApiModelProperty(value = "存储方案名称")
	private String up_name;

	@ApiModelProperty(value = "存储方案是否开启")
	private Integer up_open;

	@ApiModelProperty(value = "存储方案配置项")
	private List<ConfigItem> configItems;

	@ApiModelProperty(value = "存储方案插件beanid")
	private String up_bean_id;

	

	public UploadPluginVo(Upload upload) {
		this.id = upload.getId();
		this.up_name = upload.getUp_name();
		this.up_open = upload.getUp_open();
		this.up_bean_id = upload.getUp_bean_id();
		Gson gson = new Gson();
		this.configItems = gson.fromJson(upload.getUp_config(),  new TypeToken< List<ConfigItem> >() {  }.getType());
	}

	public UploadPluginVo(IUploader upload) {
		this.id = 0;
		this.up_name = upload.getPluginName();
		this.up_open = upload.getIsOpen();
		this.up_bean_id = upload.getPluginId();
		this.configItems = upload.definitionConfigItem();
	}
	public UploadPluginVo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUp_name() {
		return up_name;
	}

	public void setUp_name(String up_name) {
		this.up_name = up_name;
	}

	public Integer getUp_open() {
		return up_open;
	}

	public void setUp_open(Integer up_open) {
		this.up_open = up_open;
	}

	public List<ConfigItem> getConfigItems() {
		return configItems;
	}

	public void setConfigItems(List<ConfigItem> configItems) {
		this.configItems = configItems;
	}

	public String getUp_bean_id() {
		return up_bean_id;
	}

	public void setUp_bean_id(String up_bean_id) {
		this.up_bean_id = up_bean_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
