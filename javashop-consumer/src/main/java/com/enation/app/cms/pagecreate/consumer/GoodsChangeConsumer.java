package com.enation.app.cms.pagecreate.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.progress.model.TaskProgress;
import com.enation.app.base.progress.service.IProgressManager;
import com.enation.app.cms.pagecreate.service.IPageCreator;
import com.enation.app.cms.pagecreate.service.impl.PageCreateManager;
import com.enation.app.core.event.IGoodsChangeEvent;
import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.framework.jms.support.goods.GoodsChangeMsg;

/**
 * 
 * 商品页面生成
 * 
 * @author zh
 * @version v1.0
 * @since v6.4.0 2017年8月29日 下午3:40:14
 */
@Component
public class GoodsChangeConsumer implements IGoodsChangeEvent {

	@Autowired
	private IPageCreator redisPageCreator;
	@Autowired
	private IProgressManager progressManager;

	/**
	 * 商品静态页面生成
	 * 
	 * @param goodsChangeMsg
	 *            商品变更消息vo
	 */
	@Override
	public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
		/** 生成任务进度 */
		TaskProgress taskProgress = new TaskProgress(2);
		progressManager.putProgress(PageCreateManager.PAGEID, taskProgress);

		Integer[] goodsIds = goodsChangeMsg.getGoods_ids();
		/** 为了防止生成的商品在首页存在 所有先生成首页一次 */
		redisPageCreator.createIndex();
		for (int i = 0; i < goodsIds.length; i++) {
			String pagename = ("/goods-" + goodsIds[i] + ".html");
			/** 生成静态页面 */
			redisPageCreator.createOne(pagename, ClientType.PC.name());
			redisPageCreator.createOne(pagename, ClientType.WAP.name());
		}
		progressManager.remove(PageCreateManager.PAGEID);

	}
}
