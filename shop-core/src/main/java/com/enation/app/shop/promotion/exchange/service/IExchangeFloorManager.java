package com.enation.app.shop.promotion.exchange.service;

import java.util.List;
import java.util.Map;
/**
 * 
 * 积分商品楼层管理接口
 * @author    jianghongyan
 * @version   1.0.0,2016年6月20日
 * @since     v6.1
 */
public interface IExchangeFloorManager {

	/**
	 * 获取全部楼层商品
	 * @return
	 */
	List listAll();

	/**
	 * 根据类型获取积分商品
	 * @param type
	 * @return
	 */
	Map getExchangeGoodsByType(Integer type);

}
