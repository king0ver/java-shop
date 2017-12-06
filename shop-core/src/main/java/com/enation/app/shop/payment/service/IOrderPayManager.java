package com.enation.app.shop.payment.service;

import com.enation.app.shop.aftersale.model.vo.RefundVo;
import com.enation.app.shop.trade.model.enums.TradeType;

/**
 * 订单支付管理
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月3日下午11:17:35
 */
public interface IOrderPayManager {
	
	
	/**
	 * 对某订单支付
	 * @param ordersn 订单sn
	 * @param payment_method_id 支付方式id
	 * @param payMode 支付模式：
	 * normal:正常的网页跳转
	 * qr:二维码扫描
	 * 如果为空默认为normal模式
	 * @return 支付跳转HTML
	 */
	public String payOrder(String ordersn ,Integer payment_method_id,String payMode,String client_type);
	
	
	/**
	 * 对某交易进行支付
	 * @param tradesn 交易编号
	 * @param payment_method_id 支付方式id
	 * @param payMode 支付模式：
	 * normal:正常的网页跳转
	 * qr:二维码扫描
	 * 如果为空默认为normal模式
	 * @param client_type 调用客户端类型
	 * @return 支付跳转HTML
	 */
	public String payTrade(String tradesn,Integer payment_method_id,String payMode,String client_type);
	
	
	/**
	 * 支付同步回调
	 * @param pluginId
	 */
	public String payReturn(TradeType tradeType,String pluginId);
	
	
	/**
	 * 支付异步回调
	 * @param pluginId
	 * @return
	 */
	public String payCallback(TradeType tradeType,String pluginId);


	/**
	 * 订单退款，原路退回
	 * @param refund_sn 退款单号
	 * @param order_sn 订单号
	 * @param refundPrice 退款金额
	 */
	public boolean returnOrder(RefundVo refund);


	/**
	 * 查询退款进度情况
	 */
	public void queryRefundOrderStatus();

}
