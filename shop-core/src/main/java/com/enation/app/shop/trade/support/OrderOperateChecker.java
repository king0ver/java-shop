package com.enation.app.shop.trade.support;

import java.util.HashMap;
import java.util.Map;

import com.enation.app.shop.trade.model.enums.OrderOperate;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.vo.PaymentType;


/**
 * 订单操作校验器
 * 校验订单在不同状态下的可允许的操作状态
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月20日下午1:06:18
 */
public class OrderOperateChecker {
	
	//货到付款流程
	private static final Map<OrderStatus,OrderStep> codFlow = new HashMap<OrderStatus,OrderStep>();
	
	//款到发化流程
	private static final Map<OrderStatus,OrderStep> payFirstFlow = new HashMap<OrderStatus,OrderStep>();
	
	//定义流程
	static{
		initCodFlow();
		initPayfirstFlow();
	}
	
	
	/**
	 * 校验操作是否被允许
	 * @param paymentType
	 * @param status
	 * @param operate
	 * @return
	 */
	public static boolean checkAllowable(PaymentType paymentType,OrderStatus status,OrderOperate operate ){
		
		Map<OrderStatus,OrderStep> flow = null;
		if(PaymentType.cod.compareTo( paymentType)==0 ){
			flow = codFlow;
		}else{
			flow = payFirstFlow;
		}
		
		if(flow ==null){
			return false;
		}
		
		OrderStep step =  flow.get(status);
		return step.checkAllowable(operate);
		
	}
	
	
	
	
	
	/**
	 * 定义款到发货流程
	 */
	private static void initPayfirstFlow(){
		
		//新订单，可以确认，可以取消
		OrderStep newStep = new OrderStep( OrderStatus.NEW,  OrderOperate.confirm,OrderOperate.cancel);
		payFirstFlow.put( OrderStatus.NEW, newStep);
		
		//确认的订单，可以支付，可以取消
		OrderStep confirmStep = new OrderStep( OrderStatus.CONFIRM,  OrderOperate.pay,  OrderOperate.cancel);
		payFirstFlow.put( OrderStatus.CONFIRM, confirmStep);
		
		//已经支付，可以发货
		OrderStep payStep = new OrderStep( OrderStatus.PAID_OFF,  OrderOperate.ship);
		payFirstFlow.put( OrderStatus.PAID_OFF, payStep);
		
		//发货的订单，可以确认收货
		OrderStep shipStep = new OrderStep( OrderStatus.SHIPPED,  OrderOperate.rog);
		payFirstFlow.put( OrderStatus.SHIPPED, shipStep);
		
		//收货的订单，可以完成
		OrderStep rogStep = new OrderStep( OrderStatus.ROG,  OrderOperate.complete);
		payFirstFlow.put( OrderStatus.ROG, rogStep);

		//售后的订单可以完成
		OrderStep refundStep = new OrderStep( OrderStatus.AFTE_SERVICE,  OrderOperate.complete);
		payFirstFlow.put( OrderStatus.AFTE_SERVICE, refundStep);


		//取消的的订单不能有任何操作
		OrderStep cancelStep = new OrderStep( OrderStatus.CANCELLED);
		payFirstFlow.put( OrderStatus.CANCELLED, cancelStep);

		//异常的订单不能有任何操作
		OrderStep errorStep = new OrderStep( OrderStatus.INTODB_ERROR);
		payFirstFlow.put( OrderStatus.INTODB_ERROR, errorStep);
		
		//完成的订单不能有任何操作
		OrderStep completeStep = new OrderStep( OrderStatus.COMPLETE);
		payFirstFlow.put( OrderStatus.COMPLETE, completeStep);
	}
	
	/**
	 * 定义货到付款流程
	 */
	private static void initCodFlow(){
		//新订单，可以确认，可以取消
		OrderStep newStep = new OrderStep( OrderStatus.NEW,  OrderOperate.confirm,OrderOperate.cancel);
		codFlow.put( OrderStatus.NEW, newStep);
		
		//确认的订单，可以发货,可取消
		OrderStep confirmStep = new OrderStep( OrderStatus.CONFIRM, OrderOperate.ship,OrderOperate.cancel);
		codFlow.put( OrderStatus.CONFIRM, confirmStep);
		
		//发货的订单，可以确认收货
		OrderStep shipStep = new OrderStep( OrderStatus.SHIPPED,  OrderOperate.rog);
		codFlow.put( OrderStatus.SHIPPED, shipStep);
		
		//收货的订单，可以付款
		OrderStep rogStep = new OrderStep( OrderStatus.ROG,  OrderOperate.pay);
		codFlow.put( OrderStatus.ROG, rogStep);
		
		//已经支付，可以完成
		OrderStep payStep = new OrderStep( OrderStatus.PAID_OFF, OrderOperate.complete);
		codFlow.put( OrderStatus.PAID_OFF, payStep);


		//售后的订单可以完成
		OrderStep refundStep = new OrderStep( OrderStatus.AFTE_SERVICE,  OrderOperate.complete);
		codFlow.put( OrderStatus.AFTE_SERVICE, refundStep);


		//取消的的订单不能有任何操作
		OrderStep cancelStep = new OrderStep( OrderStatus.CANCELLED);
		codFlow.put( OrderStatus.CANCELLED, cancelStep);
		
		//完成的订单不能有任何操作
		OrderStep completeStep = new OrderStep( OrderStatus.COMPLETE);
		codFlow.put( OrderStatus.COMPLETE, completeStep);
	}
	
}
