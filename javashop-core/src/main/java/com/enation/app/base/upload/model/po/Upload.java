package com.enation.app.base.upload.model.po;

import com.enation.app.base.upload.model.vo.UploadPluginVo;
import com.enation.framework.database.PrimaryKeyField;
import com.google.gson.Gson;

/**
 * 储存方案
 * 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0 2017年11月10日 下午12:03:37
 */
public class Upload {

	/** id 存储方案id */
	private Integer id;
	/** up_name 存储方案名称 */
	private String up_name;
	/** up_open 存储方案是否开启 */
	private Integer up_open;
	/** 存储方案参数 */
	private String up_config;
	/** 存储方案插件beanid */
	private String up_bean_id;

	public Upload(UploadPluginVo plugin) {
		this.id = plugin.getId();
		this.up_name = plugin.getUp_name();
		this.up_open = plugin.getUp_open();
		this.up_bean_id = plugin.getUp_bean_id();

		Gson gson = new Gson();
		this.up_config = gson.toJson(plugin.getConfigItems());
	}

	public Upload() {

	}

	@PrimaryKeyField
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

	public String getUp_config() {
		return up_config;
	}

	public void setUp_config(String up_config) {
		this.up_config = up_config;
	}

	public String getUp_bean_id() {
		return up_bean_id;
	}

	public void setUp_bean_id(String up_bean_id) {
		this.up_bean_id = up_bean_id;
	}

}
