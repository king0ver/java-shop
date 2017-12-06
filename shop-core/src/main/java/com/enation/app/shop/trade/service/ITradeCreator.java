package com.enation.app.shop.trade.service;

import com.enation.app.shop.trade.model.vo.Trade;

/**
 * 订单创建<br>
 * @since v6.4
 * @author kingapex
 * @version v1.0
 * @created 2017年08月16日15:39:19
 */
public interface ITradeCreator {

	/**
	 * 新建订单
	 * @param 会话id
	 * @return
	 * 
	 * @param sessionid
	 */
	public Trade createTrade( );

 
}