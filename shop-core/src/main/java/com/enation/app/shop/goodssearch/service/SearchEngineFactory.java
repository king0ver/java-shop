package com.enation.app.shop.goodssearch.service;

import com.enation.eop.SystemSetting;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * 搜索引擎工厂类
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月14日 下午4:13:39
 */
public abstract class SearchEngineFactory {

	private SearchEngineFactory(){}
	
	/**
	 * 获取搜索引擎
	 * @return
	 */
	public static IGoodsSearchManager getSearchEngine(){
		
		int open = SystemSetting.getCluster();//0 未开启  1开启
		
		if(open==0){
			
			return SpringContextHolder.getBean("goodsSearchLuceneManager");
		}else{
			
			return SpringContextHolder.getBean("goodsSearchEsManager");
		}
	}
}
