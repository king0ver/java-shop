package com.enation.app.shop.goodssearch.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.shop.goodssearch.service.IGoodsIndexSendManager;
/**
 * 
 * 索引任务实现 
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月22日 上午10:10:48
 */
@Component
public class GoodsIndexSendManager implements IGoodsIndexSendManager {
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private IProgressManager progressManager;
	
	public final static String INDEXID = "index_create";



	@Override
	public boolean startCreate() {
		if (progressManager.getProgress(INDEXID) != null) {
			return false;
		} 
		/** 发送索引生成消息 */
		this.amqpTemplate.convertAndSend(AmqpExchange.INDEX_CREATE.name(), "GoodsIndexMsg","");
		return true;
	}


}
