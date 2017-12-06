package com.enation.app.shop.order.manager;

/**
 * 交易入库业务类
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月23日下午5:16:52
 */
public interface ITradeIntoDBManager {
	
	/**
	 * 根据cacheKey入库
	 * @param tradeSn
	 */
	public void intoDB(String cacheKey);
	
	
	
}
