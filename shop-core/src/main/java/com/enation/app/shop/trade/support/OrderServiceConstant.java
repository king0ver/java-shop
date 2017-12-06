package com.enation.app.shop.trade.support;

/**
 * 购物车缓存常量
 * @author kingapex
 * @version v1.0
 * @since v6.2
 * 2016年11月17日下午6:16:40
 */
public  class OrderServiceConstant {
	
	/**
	 * 私有构造器，不允许new 方式使用
	 */
	private OrderServiceConstant(){}
	
	/**
	 * 会话key前缀 
	 */
	public static final String CART_SESSION_ID_PREFIX="cart_sid_";
	
	
	
	/**
	 * 会员id key前缀
	 */
	public static final String CART_MEMBER_ID_PREFIX="cart_mid_";
	
	
	/**
	 * 交易的价格存储前缀 
	 */
	public static final String PRICE_SESSION_ID_PREFIX="trade_price_sid_";
	
	/**
	 * 交易格存储前缀 
	 */
	public static final String TRADE_SESSION_ID_PREFIX="trade_sid_"; 
	
	public static final String ORDER_CREATE_QUEUE ="order.create.queue";
	
	public static final String ORDER_STATUS_TOPIC ="order.status.topic";
	
	 
	

}
