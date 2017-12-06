package com.enation.app.base.upload.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;

/**
 * 存储方案插件父类<br>
 * 具有读取配置的能力
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月12日 下午2:10:18
 */
public abstract class AbstractUploadPlugin {
	
	@Autowired
	private  IDaoSupport daoSupport;
	
	
	/**
	 * 获取插件的配置方式
	 * @return
	 */
	protected Map<String, String> getConfig(){
		//获取当前支付插件的id
		String uploadPluginId = this.getPluginId();
		String config  = this.daoSupport.queryForString("select up_config from es_uploader where up_bean_id=?", uploadPluginId);
		if(StringUtil.isEmpty(config)){
			return new HashMap<>();
		}
		Gson gson = new Gson();
		List<ConfigItem> list = gson.fromJson(config,   new TypeToken<List<ConfigItem>>() {  
                }.getType());
		Map<String, String> result = new HashMap<>();
		if(list!=null){
			for(ConfigItem item : list){
				result.put(item.getName(), item.getValue());
			}
		}
		return result;
	}
	
	protected abstract String getPluginId(); 
	


}
