package com.enation.app.shop.trade.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.po.OrderItem;
import com.enation.app.shop.trade.service.IOrderItemManager;
import com.enation.framework.database.IDaoSupport;


/**
 * 订单产品查询业务类
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月8日下午6:17:34
 */
@Service
public class OrderItemManager implements IOrderItemManager {

	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public List<OrderItem> queryByOrderSn(String ordersn) {
		
		List<OrderItem> list  = daoSupport.queryForList("select * from es_order_items where order_sn=?", OrderItem.class, ordersn);
		
		return list;
	}
	
	
	
	@Override
	public List<OrderItem> queryByTradeSn(String tradesn) {
		List<OrderItem> list  = daoSupport.queryForList("select * from es_order_items where trade_sn=?", OrderItem.class, tradesn);
		return list;
	}



	@Override
	public void updateItemSnapshotId(String ordersn, Integer proudct_id, Integer snapshot_id) {
		String sql = "update es_order_items set snapshot_id = ? where order_sn = ? and product_id = ? ";
		this.daoSupport.execute(sql, snapshot_id,ordersn,proudct_id);
	}



	@Override
	public OrderItem queryOrderItemBySnapshot(Integer snapshot_id) {
		String sql = "select * from es_order_items where snapshot_id = ? ";
		return this.daoSupport.queryForObject(sql, OrderItem.class, snapshot_id);
	}

}
