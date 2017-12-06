package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IGoodsIndexInitEvent;


/**
 * 消费者
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年4月12日 下午4:33:14
 */
@Component
public class GoodsIndexInitReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IGoodsIndexInitEvent> events;
	
	/**
	 * 初始化商品索引
	 * @param str
	 */
	public void initGoodsIndex(String str){
		try {
			if(events!=null){
				for(IGoodsIndexInitEvent event : events){
					event.createGoodsIndex();
				}
			}
		} catch (Exception e) {
			this.logger.error("处理商品索引初始化消息出错",e);
			e.printStackTrace();
		}
	}
	
}
