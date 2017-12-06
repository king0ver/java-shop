package com.enation.app.shop.trade.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.shop.trade.model.enums.OrderOperate;
import com.enation.app.shop.trade.model.enums.OrderStatus;

/**
 * 订单流程
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月19日下午10:43:22
 */
public class OrderStep {
	 

	
	/**
	 * 订单状态
	 */
	private OrderStatus orderStatus;
	
	
	/**
	 * 允许的操作
	 */
	private List<OrderOperate> allowableOperate;
	
	public OrderStep(OrderStatus _orderStatus,OrderOperate... operates ){
		this.orderStatus = _orderStatus;
		this.allowableOperate = new ArrayList<OrderOperate>();
		for (OrderOperate orderOperate : operates) {
			allowableOperate.add(orderOperate);
		}
	}
	
	
	public boolean checkAllowable(OrderOperate operate){
		for (OrderOperate orderOperate : allowableOperate) {
			if(operate.compareTo(orderOperate)==0){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		Map<OrderStatus,OrderStep> codFlow = new HashMap<OrderStatus,OrderStep>();
		
		//新订单，可以确认，可以取消
		OrderStep newStep = new OrderStep( OrderStatus.NEW,  OrderOperate.confirm,OrderOperate.cancel);
		codFlow.put( OrderStatus.NEW, newStep);
		
		//确认的订单，可以发货
		OrderStep confirmStep = new OrderStep( OrderStatus.CONFIRM,  OrderOperate.ship);
		codFlow.put( OrderStatus.CONFIRM, confirmStep);
		
		OrderStep shipStep = new OrderStep( OrderStatus.SHIPPED,  OrderOperate.rog);
		codFlow.put( OrderStatus.SHIPPED, shipStep);
		
		
		//已经支付，只可以发货
		OrderStep payStep = new OrderStep( OrderStatus.PAID_OFF,  OrderOperate.ship);
		codFlow.put( OrderStatus.PAID_OFF, payStep);
		
		boolean result = codFlow.get(OrderStatus.CONFIRM).checkAllowable(OrderOperate.ship );
		System.out.println(result);
		
	}
	
}
