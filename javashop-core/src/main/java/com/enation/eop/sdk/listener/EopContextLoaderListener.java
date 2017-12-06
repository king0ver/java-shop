package com.enation.eop.sdk.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.enation.app.base.core.model.FastDfsSetting;
import com.enation.eop.SystemSetting;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.utils.JavaReflection;
import com.enation.framework.component.ComponentLoader;
import com.enation.framework.component.IComponentManager;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * EopLinstener 负责初始化站点缓存
 * @author kingapex
 * 2010-7-18下午04:01:16
 */
public class EopContextLoaderListener implements ServletContextListener {

	
	public void contextDestroyed(ServletContextEvent event) {

	}
	
	public void contextInitialized(ServletContextEvent event) {
	 
		//因为首次安装时，还没有连接数据库，所以把组织关系的代码移到外面
		ComponentLoader.start();
		
		if( EopSetting.INSTALL_LOCK.toUpperCase().equals("YES") ){
			EopSite.reload(); //重新加载站点设置
			SystemSetting.load();
			
			//设置店铺设置
			Object objs = SpringContextHolder.getBean("storeSetting");
			try {
				JavaReflection.invokeMethod(objs, "load", new Object[0]);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			//订单设置
			Object obj = SpringContextHolder.getBean("orderSetting");
			try {
				JavaReflection.invokeMethod(obj, "load", new Object[0]);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			
			IComponentManager componentManager = SpringContextHolder.getBean("componentManager");
			componentManager.startComponents(); //启动组件
			
		}
		
	}

}
