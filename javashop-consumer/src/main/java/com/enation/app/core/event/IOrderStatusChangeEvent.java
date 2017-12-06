package com.enation.app.core.event;

import com.enation.app.shop.trade.support.OrderStatusChangeMessage;

/**
 * 订单状态变化事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午3:49:58
 */
public interface IOrderStatusChangeEvent {

	/**
	 * 订单状态改变，需要执行的操作
	 * @param orderMessage
	 */
	public void orderChange(OrderStatusChangeMessage orderMessage);
}
