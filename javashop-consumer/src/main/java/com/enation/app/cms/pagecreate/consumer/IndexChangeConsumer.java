package com.enation.app.cms.pagecreate.consumer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.cms.floor.model.vo.CmsManageMsg;
import com.enation.app.cms.pagecreate.service.IPageCreator;
import com.enation.app.cms.pagecreate.service.impl.PageCreateManager;
import com.enation.app.core.event.ICategoryChangeEvent;
import com.enation.app.core.event.IIndexChangeEvent;
import com.enation.app.core.event.IMobileIndexChangeEvent;
import com.enation.framework.jms.support.goods.CategoryChangeMsg;
/**
 * 
 * 首页生成
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:41:18
 */
@Component
public class IndexChangeConsumer implements IIndexChangeEvent,ICategoryChangeEvent,IMobileIndexChangeEvent{

	@Autowired
	private IPageCreator redisPageCreator;
	@Autowired
	private IProgressManager progressManager;
	/**
	 * 生成首页
	 */
	@Override
	public void createIndexPage(CmsManageMsg operation) {
		this.createIndex();
	}
	
	@Override
	public void categoryChange(CategoryChangeMsg categoryChangeMsg) {
		this.createIndex();
	}
	
	private void createIndex() {
		/** 生成任务进度 */
		TaskProgress taskProgress = new TaskProgress(1);
		progressManager.putProgress(PageCreateManager.PAGEID, taskProgress);
		/** 生成静态页面 */
		redisPageCreator.createIndex();
		progressManager.remove(PageCreateManager.PAGEID);
	}

	@Override
	public void mobileIndexChange(CmsManageMsg operation) {
		this.createIndex();
	}

}
