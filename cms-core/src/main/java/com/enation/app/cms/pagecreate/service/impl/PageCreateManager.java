package com.enation.app.cms.pagecreate.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.progress.controller.ProgressController;
import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.base.progress.service.impl.ProgressManager;
import com.enation.app.cms.pagecreate.service.IPageCreateManager;
/**
 * 
 * 静态页生成实现
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月1日 上午11:51:09
 */
@Component
public class PageCreateManager implements IPageCreateManager{

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private IProgressManager progressManager;

	public final static String PAGEID = "page_create";

	@Override
	public boolean startCreate(String[] choose_pages) {

		if (progressManager.getProgress(PAGEID) != null) {
			return false;
		} 
		this.sendPageCreateMessage(choose_pages);
		return true;
	}


	/**
	 * 发送页面生成消息
	 * 
	 * @param type 是交换器名称
	 * @param id 路由键 
	 * @param choose_pages 要发送的对象 要生成的页面
	 */
	public void sendPageCreateMessage(String[] choose_pages) {
		try {
			this.amqpTemplate.convertAndSend(AmqpExchange.PAGE_CREATE.name(), "consumerPageMsg",choose_pages);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}



}
