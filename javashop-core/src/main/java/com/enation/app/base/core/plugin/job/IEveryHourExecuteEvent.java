package com.enation.app.base.core.plugin.job;

import com.enation.framework.plugin.Bundle;

/**
 * 任务每小时执行事件
 * @author kingapex
 *
 */
@Bundle(JobExecutePluginsBundle.class)
public interface IEveryHourExecuteEvent {
	
	/**
	 * 每隔一小时会激发此事件 
	 */
	public void everyHour();
	
	
}
