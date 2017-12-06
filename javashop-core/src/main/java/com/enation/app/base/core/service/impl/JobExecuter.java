package com.enation.app.base.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.base.core.plugin.job.JobExecutePluginsBundle;
import com.enation.app.base.core.service.IJobExecuter;
import com.enation.eop.SystemSetting;
import com.enation.framework.component.ComponentView;
import com.enation.framework.component.IComponentManager;

/**
 * 任务执行器
 * @author kingapex
 *
 */ 
public class JobExecuter implements IJobExecuter {

	@Autowired
	private JobExecutePluginsBundle jobExecutePluginsBundle;

	@Autowired
	private IComponentManager componentManager;


	@Override
	public void everyHour() {
		/** 判断集群是否开启  如果开启使用集群任务调度 */
		if(SystemSetting.getCluster() == 0){
			jobExecutePluginsBundle.everyHourExcecute();
		}
	}

	@Override 
	public void everyDay() {
		try{
			/** 判断集群是否开启  如果开启使用集群任务调度 */
			if(SystemSetting.getCluster() == 0){
				this.jobExecutePluginsBundle.everyDayExcecute();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void everyMonth() {
		/** 判断集群是否开启  如果开启使用集群任务调度 */
		if(SystemSetting.getCluster() == 0){
			this.jobExecutePluginsBundle.everyMonthExcecute();
		}

	}
}
