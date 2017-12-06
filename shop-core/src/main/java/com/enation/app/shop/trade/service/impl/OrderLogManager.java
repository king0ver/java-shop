package com.enation.app.shop.trade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.po.OrderLog;
import com.enation.app.shop.trade.service.IOrderLogManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 订单的操作日志
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 上午11:34:23
 */
@Service
public class OrderLogManager implements IOrderLogManager{

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public List<OrderLog> getOrderLogs(String order_sn) {
		
		List<OrderLog> list = this.daoSupport.queryForList("select * from es_order_log where order_sn = ?",OrderLog.class, order_sn);
		
		return list;
	}

}
