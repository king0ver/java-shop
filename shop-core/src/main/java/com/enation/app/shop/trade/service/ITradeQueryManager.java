package com.enation.app.shop.trade.service;


import com.enation.app.shop.trade.model.po.TradePo;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.app.shop.trade.model.vo.TradeLine;
import com.enation.framework.database.Page;

/**
 * 交易查询业务类
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月6日下午8:11:52
 */
public interface ITradeQueryManager {

	/**
	 * 根据编号获取一个交易
	 * @param tradesn 交易编号
	 * @return
	 */
	public TradePo getOneBySn(String tradesn);
	
	/**
	 * 查询我的交易
	 * @param queryParam
	 * @return
	 */
	public Page<TradeLine> queryMyTrade(OrderQueryParam queryParam);
}
