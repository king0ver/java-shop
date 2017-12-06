package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IGoodsChangeEvent;
import com.enation.framework.jms.support.goods.GoodsChangeMsg;

/**
 * 商品变化消费者
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:47:22
 */
@Component
public class GoodsChangeReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IGoodsChangeEvent> events;
	
	/**
	 * 商品变化
	 * @param goodsChangeMsg
	 */
	public void goodsChange(GoodsChangeMsg goodsChangeMsg){
		try {
			if(events!=null){
				for(IGoodsChangeEvent event : events){
					event.goodsChange(goodsChangeMsg);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理商品变化消息出错", e);
			e.printStackTrace();
		}
	}
}
