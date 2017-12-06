package com.enation.app.shop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.order.manager.ITradeIntoDBManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;

@Component
public class OrderConsumer {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private ICache cache;
	
	@Autowired
	private ITradeIntoDBManager tradeIntoDBManager;

	public void receiveQueue(String tradeSn) {
		//System.out.println(tradeSn);
		//Trade trade = (Trade) cache.get(tradeSn);
		//System.out.println("tradeSn="+tradeSn+"__________value->" + trade);
		System.out.println("处理="+tradeSn+"入库");
		tradeIntoDBManager.intoDB(tradeSn);
		
	}

}
