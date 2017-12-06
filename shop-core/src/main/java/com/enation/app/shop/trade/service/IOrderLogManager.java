package com.enation.app.shop.trade.service;

import java.util.List;

import com.enation.app.shop.trade.model.po.OrderLog;

/**
 * 订单日志manager
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 上午11:31:14
 */
public interface IOrderLogManager {

	/**
	 * 查询订单的操作日志
	 * @param order_sn
	 * @return
	 */
	public List<OrderLog> getOrderLogs(String order_sn);
}
