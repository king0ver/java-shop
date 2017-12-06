package com.enation.app.base.core.service;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 系统设置业务接口
 * @author kingapex
 *2012-5-11上午10:42:35
 */
public interface ISettingService {


	/**
	 * 向系统设置中添加一项
	 * @param groupname 系统项
	 * @param name	参数名
	 * @param value	参数值
	 */
	public void add(String groupname,String name,String value);
	
	
	/**
	 * 保存某项系统设置
	 * @param groupname 系统项
	 * @param name 参数名
	 * @param value  参数值
	 */
	public void save(String groupname,String name,String value);
	
	
	
	/**
	 * 删除一项系统设置
	 * @param groupname 系统项
	 */
	public void delete(String groupname);
	
	
	
	/**
	 * 更新所有系统设置
	 * 
	 * @param code
	 * @param value
	 * @throws SettingRuntimeException
	 */
	public abstract void save(Map<String,Map<String,String>> settings ) throws SettingRuntimeException;

	/**
	 * 更新某一项设置
	 * @param groupname 系统项
	 * @param settings 参数组
	 */
	public abstract void save(String groupname,Map<String,String> settings ) ;
	
	
	/**
	 * 读取全部设置
	 * @return
	 */
	public  Map<String,Map<String,String>>  getSetting();
	
	/**
	 * 读取某种设置
	 * @param group 系统项
	 * @return
	 */
	public   Map<String,String>  getSetting(String group);
	
	 
	
	/**
	 * 读取某项设置值
	 * @param name 参数名
	 * @return 参数值
	 */
	public abstract String getSetting(String group,String name);

}