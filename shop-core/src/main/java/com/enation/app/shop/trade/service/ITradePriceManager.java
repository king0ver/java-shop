package com.enation.app.shop.trade.service;

import com.enation.app.shop.trade.model.vo.PriceDetail;
 



/**
 * 订单价格业务类
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月23日上午10:01:30
 */
public interface ITradePriceManager {

	
	/**
	 * 获取当前要结算的订单的付款单
	 * @return
	 */
	public PriceDetail getTradePrice();
	
	
	/**
	 * 将订单价格压入缓存
	 * @return
	 */
	public void pushPrice(PriceDetail detail);
	
	
	/**
	 * 清除缓存中的价格
	 */
	public void cleanPrice();

 

}