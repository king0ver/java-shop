package com.enation.app.base.progress.service;

import com.enation.app.base.progress.model.TaskProgress;
/**
 * 
 * 进度管理接口
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月1日 下午5:41:26
 */
public interface IProgressManager {
	/**
	 * 获取进度信息
	 * @param id		唯一标识
	 * @return	进度
	 */
	public TaskProgress getProgress(String id);
	/**
	 *  写入进度
	 * @param id
	 * @param progress
	 */
	public void putProgress(String id,TaskProgress progress);
	/**
	 * 移除任务
	 * @param id	唯一标识
	 */
	public void remove(String id);




}
