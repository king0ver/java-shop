package com.enation.app.cms.pagecreate.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.cms.pagecreate.service.IPageCreator;
import com.enation.app.cms.pagecreate.service.impl.PageCreateManager;
import com.enation.app.core.event.IPageCreateEvent;
import com.enation.framework.database.IDaoSupport;
/**
 * 静态页生成
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:41:52
 */
@Component
public class PageCreateConsumer implements IPageCreateEvent{

	@Autowired
	private IPageCreator redisPageCreator;
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IProgressManager progressManager;

	/**
	 *  生成静态页面
	 * @param choose_pages	页面
	 */
	@Override
	public void createPage(String[] choose_pages) {
		System.out.println("开始执行任务");
		/** 生成静态页面 */
		System.out.println("choose_pages size:" + choose_pages.length);
		int goodsCount = 0;
		int helpCount = 0;
		int indexCount = 0;
		/** 判断是否需要生成商品页 取其个数 */
		if (this.checkExists("goods",choose_pages)) {
			goodsCount = this.getGoodsCount();
		}
		/** 判断是否需要生成帮助中心页 取其个数 */
		if (this.checkExists("help",choose_pages)) {
			helpCount = this.getHelpCount();
		}
		/** 判断是否需要生成首页 取首页个数 */
		if (this.checkExists("index",choose_pages)) {
			indexCount = 2;
		}
		/** 需要生成页面总数 */
		int allcount = goodsCount + helpCount + indexCount;
		/** 生成任务进度 */
		TaskProgress taskProgress = new TaskProgress(allcount);
		progressManager.putProgress(PageCreateManager.PAGEID, taskProgress);
		if (this.checkExists("goods",choose_pages)) {
			/** 生成商品静态页 */
			this.redisPageCreator.createGoods();
		}
		/** 生成帮助中心页面 */
		if (this.checkExists("help",choose_pages)) {
			this.redisPageCreator.createHelp();
		}
		/** 生成首页 */
		if (this.checkExists("index",choose_pages)) {
			this.redisPageCreator.createIndex();
		}
		TaskProgress task=(TaskProgress)progressManager.getProgress(PageCreateManager.PAGEID);
		if(task!=null) {
			task.step("静态页生成完成");
			task.success();
			/** 更新进度 重新放入缓存 */
			progressManager.putProgress(PageCreateManager.PAGEID, task);
		}
	}

	/**
	 * 检测某个页面是否存在
	 * 
	 * @param page
	 * @param choose_pages
	 * @return
	 */
	private boolean checkExists(String page,String[] choose_pages) {
		if (choose_pages == null)
			return false;
		for (String choose : choose_pages) {
			if (choose.equals(page)) {
				return true;
			}
		}
		return false;
	}

	private int getGoodsCount() {
		return this.daoSupport.queryForInt("select count(0) from es_goods");
	}

	private int getHelpCount() {
		return this.daoSupport.queryForInt("select count(0) from  es_helpcenter ");
	}

}
