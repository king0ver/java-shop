package com.enation.app.base.core.plugin.job;

import com.enation.framework.plugin.Bundle;

/**
 * 每月执行任务事件
 * @author kingapex
 *
 */
@Bundle(JobExecutePluginsBundle.class)
public interface IEveryMonthExecuteEvent {
	
	/**
	 * 每月调用一些此方法 
	 */
	public void everyMonth();
}
