package com.enation.app.shop.goodssearch.service;

import com.enation.eop.SystemSetting;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * 索引类型工厂
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月15日 下午7:10:41
 */
public class IndexTypeFactory {

	private IndexTypeFactory(){}
	
	/**
	 * 获取索引
	 * @return
	 */
	public static IGoodsIndexManager getIndexType(){
		
		int open = SystemSetting.getCluster();//0 未开启  1开启
		
		if(open==0){
			
			return SpringContextHolder.getBean("goodsIndexLuceneManager");
		}else{
			
			return SpringContextHolder.getBean("goodsIndexEsManager");
		}
	}
}
