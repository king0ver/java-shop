package com.enation.app.shop.trade.support;

import java.io.Serializable;

import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.OrderPo;


/**
 * 订单变化消息
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月5日下午8:26:29
 */
public class OrderStatusChangeMessage implements Serializable {
 
	private static final long serialVersionUID = 8915428082431868648L;
	
	public  OrderStatusChangeMessage(){}
	
 
	
	/**
	 * 变化的订单
	 */
	private OrderPo order;
	
	/**
	 * 原状态
	 */
	private OrderStatus oldStatus;
	
	/**
	 * 新状态
	 */
	private OrderStatus newStatus;

 

	public OrderStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(OrderStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public OrderStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(OrderStatus newStatus) {
		this.newStatus = newStatus;
	}

	public OrderPo getOrder() {
		return order;
	}

	public void setOrder(OrderPo order) {
		this.order = order;
	}
	

}
