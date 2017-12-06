package com.enation.app.cms.pagecreate.consumer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.cms.pagecreate.service.IPageCreator;
import com.enation.app.cms.pagecreate.service.impl.PageCreateManager;
import com.enation.app.core.event.IHelpChangeEvent;
import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.framework.database.IDaoSupport;

/**
 * 
 * 帮助中心页面生成
 * 
 * @author zh
 * @version v1.0
 * @since v1.0 2017年8月29日 下午3:40:48
 */
@Component
public class HelpChangeConsumer implements IHelpChangeEvent {

	@Autowired
	private IPageCreator redisPageCreator;
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IProgressManager progressManager;

	/**
	 * 帮助中心页面的生成
	 * 
	 * @param articeids
	 *            帮助中心页面的ids
	 */
	@Override
	public void helpChange(List<Integer> articeids) {
		/** 生成任务进度 */
		TaskProgress taskProgress = new TaskProgress(articeids.size());
		progressManager.putProgress(PageCreateManager.PAGEID, taskProgress);
		for (int i = 0; i < articeids.size(); i++) {
			/** 获取catid */
			Map map = daoSupport.queryForMap("select * from es_helpcenter where id = ?", articeids.get(i));
			String pagename = ("/help-" + map.get("cat_id") + "-" + articeids.get(i) + ".html");
			/** 生成静态页面 */
			redisPageCreator.createOne(pagename, ClientType.PC.name());
		}
		/** 移除任务 */
		progressManager.remove(PageCreateManager.PAGEID);
	}
}
