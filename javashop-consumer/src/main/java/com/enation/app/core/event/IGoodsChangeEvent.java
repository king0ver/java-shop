package com.enation.app.core.event;

import com.enation.framework.jms.support.goods.GoodsChangeMsg;

/**
 * 商品变化事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:45:08
 */
public interface IGoodsChangeEvent {

	/**
	 * 商品变化后需要执行的方法
	 * @param goodsChangeMsg
	 */
	public void goodsChange(GoodsChangeMsg goodsChangeMsg);
}
