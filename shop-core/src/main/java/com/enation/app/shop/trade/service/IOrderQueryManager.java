package com.enation.app.shop.trade.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.OrderLineSeller;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.framework.database.Page;

/**
 * 订单查询接口
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月28日上午10:00:57
 */
public interface IOrderQueryManager {
	/**
	 * seller查询订单
	 * 
	 * @param queryParam
	 *            查询参数
	 * @return 订单对象Page
	 */
	public Page<OrderLineSeller> query(OrderQueryParam queryParam);


	/**
	 * 根据交易编号查询订单vo
	 * 
	 * @param tradeSn
	 * @return
	 */
	public List<Order> queryByTradeSnGetOrder(String tradeSn);


	/**
	 * 根据编号获取一个订单
	 * 
	 * @param ordersn
	 * @return 订单明细
	 */
	public OrderDetail getOneBySn(String ordersn);

	/**
	 * 查询各种状态的订单数
	 * 在调用的时候key值为订单状态，订单状态详见OrderStatus
	 * @param queryParam 查询参数
	 * @return Map key为订单状态 value为对应订单状态的数量
	 *
	 */
	public Map<Object,Integer> getOrderNum(OrderQueryParam queryParam);


}
