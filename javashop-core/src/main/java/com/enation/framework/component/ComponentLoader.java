package com.enation.framework.component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.enation.framework.component.context.ComponentContext;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.plugin.AutoRegisterPluginsBundle;
import com.enation.framework.plugin.Bundle;
import com.enation.framework.plugin.IPlugin;

public class ComponentLoader implements BeanPostProcessor {

	/**
	 * 插件桩集合
	 */
	public static  Map<Object , Object> bundleMap = new HashMap<>();
	
	/**
	 * 插件集合
	 */
	public static List pluginList = new ArrayList<>();

	public Object postProcessAfterInitialization(Object bean, String arg1)
			throws BeansException {
		return bean;
	}


	@SuppressWarnings("unchecked")
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		
		/** 如果是插件桩将其收集 为后续注册插件使用 */
		if(bean instanceof AutoRegisterPluginsBundle){
			bundleMap.put(bean.getClass(), bean);
		}

		if(bean instanceof IPlugin){
			pluginList.add(bean);
		}
		if(bean instanceof IComponent){
			IComponent component = (IComponent)bean;
			ComponentView componentView = new ComponentView();
			componentView.setComponent(component);
			componentView.setComponentid( beanName );
			ComponentContext.registerComponent(componentView);
		}
		return bean;
	}
	
	/**
	 * 为插件桩注册插件
	 */
	public static void start(){
		
		for (Object bean : pluginList) {
			
			/** 将插件注册到插件桩中 获取注解*/
			AutoRegister annotion = bean.getClass().getAnnotation(AutoRegister.class);
			if(annotion != null) {
				/** 获取此插件实现的事件 */
				Class<?> interfaces[] = bean.getClass().getInterfaces();
				for (Class<?> inte : interfaces) {
					/** 获取此事件的插件桩 */
					Bundle bundleAnnotation = inte.getAnnotation(Bundle.class);
					if(bundleAnnotation != null){
						AutoRegisterPluginsBundle bundle = (AutoRegisterPluginsBundle)bundleMap.get(bundleAnnotation.value());
						/** 得到插件 */
						IPlugin plugin =  (IPlugin)bean;
						/** 注册此插件 */
						bundle.registerPlugin(plugin);
					}
				}
			}
			
		}
	}

}
