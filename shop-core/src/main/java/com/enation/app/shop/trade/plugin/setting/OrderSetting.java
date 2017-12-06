package com.enation.app.shop.trade.plugin.setting;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.ISettingService;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * 订单设置
 * 设置订单常用设置
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月8日 下午2:40:33
 */
@Component
public class OrderSetting {
	
	//系统设置中的分组
	public static final String setting_key = "order"; 
	public static Integer cancel_order_day; //取消订单天数 
	public static Integer rog_order_day;	//确定收货天数
	public static Integer comment_order_day; //自动评价订单天数
	public static Integer service_expired_day;//售后失效天数
	public static Integer complete_order_day;//订单完成天数
	
	
	/**
	 * 加载系统设置-订单设置
	 * 由数据库中加载
	 */
	public static void load(){
		
		ISettingService settingService= SpringContextHolder.getBean("settingService");
		Map<String,String> settings = settingService.getSetting(setting_key);
		if(settings==null){
			return ;
		}
		cancel_order_day = Integer.parseInt(settings.get("cancel_order_day").toString());
		
		rog_order_day = Integer.parseInt(settings.get("rog_order_day").toString());
		
		comment_order_day = Integer.parseInt(settings.get("comment_order_day").toString());
		
		service_expired_day = Integer.parseInt(settings.get("service_expired_day")==null?service_expired_day+"":settings.get("service_expired_day").toString());
		
		complete_order_day = Integer.parseInt(settings.get("complete_order_day")==null?complete_order_day+"":settings.get("complete_order_day").toString());
	}
	
}
