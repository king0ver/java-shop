package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详细和流程详细
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月24日 上午10:58:02
 */
public class OrderFlowDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7229502057033012784L;

	private OrderDetail orderDetail;//订单详细 
	
	private List<OrderFlow>  orderFlowList;//流程详细

	public OrderDetail getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public List<OrderFlow> getOrderFlowList() {
		return orderFlowList;
	}

	public void setOrderFlowList(List<OrderFlow> orderFlowList) {
		this.orderFlowList = orderFlowList;
	}

}
