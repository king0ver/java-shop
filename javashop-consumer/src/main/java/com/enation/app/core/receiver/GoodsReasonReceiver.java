package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IGoodsReasonEvent;
import com.enation.framework.jms.support.goods.GoodsReasonMsg;

/**
 * 
 * 商品附带原因变化消费者
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月20日 下午2:09:12
 */
@Component
public class GoodsReasonReceiver {
	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired(required = false)
	private List<IGoodsReasonEvent> events;

	/**
	 * 商品变化
	 * 
	 * @param goodsReasonMsg
	 */
	public void goodsReason(GoodsReasonMsg goodsReasonMsg) {
		try {
			if (events != null) {
				for (IGoodsReasonEvent event : events) {
					event.goodsReason(goodsReasonMsg);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理商品变化消息出错", e);
			e.printStackTrace();
		}
	}
}
