package com.enation.app.shop.order.manager;

import java.util.List;

import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.Product;

/**
 * 产品入库接口
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年09月28日11:44:25
 */
public interface IProductIntoDBManager {

	/**
	 *	订单入库，扣减库存
	 * @param order
	 * @param productList
	 */
	public void productIntoDB(Order order, List<Product> productList);
	
}
