package com.enation.app.shop.trade.service;


import java.util.List;

import com.enation.app.shop.trade.model.po.OrderItem;

/**
 * 订单项查询
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月8日下午6:03:25
 */
public interface IOrderItemManager {
	
	
	/**
	 * 根据订单编号查询订单产品列表
	 * @param ordersn 订单编号
	 * @return 产品列表
	 */
	List<OrderItem> queryByOrderSn(String ordersn);
	
	/**
	 * 根据交易编号查询订单产品列表
	 * @param ordersn 交易编号
	 * @return 产品列表
	 */
	List<OrderItem> queryByTradeSn(String tradesn);
	/**
	 * 更新快照ID
	 * @param orderItemId
	 * @param snapshot_id
	 * @return
	 */
	public void updateItemSnapshotId(String ordersn,Integer proudct_id,Integer snapshot_id);
	/**
	 * 根据快照id获取购物项
	 * @return
	 */
	public OrderItem queryOrderItemBySnapshot(Integer snapshot_id);
}
