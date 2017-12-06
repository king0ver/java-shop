package com.enation.app.base.core.plugin.job;

import com.enation.framework.plugin.Bundle;

/**
 * 任务每天执行事件
 * @author kingapex
 *
 */
@Bundle(JobExecutePluginsBundle.class)
public interface IEveryDayExecuteEvent {
	
	/**
	 * 每晚23:30执行 
	 */
	public void everyDay();
	
}
