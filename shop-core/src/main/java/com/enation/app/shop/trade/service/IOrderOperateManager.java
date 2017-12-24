package com.enation.app.shop.trade.service;


import com.enation.app.shop.payment.model.vo.OrderPayReturnParam;
import com.enation.app.shop.trade.model.enums.CommentStatus;
import com.enation.app.shop.trade.model.enums.OrderPermission;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.OrderConsigneeVo;
import com.enation.app.shop.trade.model.vo.operator.Cancel;
import com.enation.app.shop.trade.model.vo.operator.Complete;
import com.enation.app.shop.trade.model.vo.operator.Confirm;
import com.enation.app.shop.trade.model.vo.operator.Delivery;
import com.enation.app.shop.trade.model.vo.operator.Rog;

/**
 * 订单操作接口
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月3日下午7:55:45
 */
public interface IOrderOperateManager {
	
	/**
	 * 确认订单
	 * @param confirm 订单确认vo
	 * @param permission 需要检测的订单权限
	 */
	public void confirm(Confirm confirm, OrderPermission permission);
	
	/**
	 * 为某订单付款<br/>
	 * @param pay_price 本次付款金额
	 * @param permission 需要检测的订单权限
	 * @param order_sn 订单号
	 * @throws IllegalArgumentException 下列情形之一抛出此异常:
	 * <li>order_sn(订单id)为null</li>
	 * @throws IllegalStateException 如果订单支付状态为已支付
	 */
	public OrderPo payOrder(String order_sn, Double pay_price, OrderPermission permission) ;


	/**
	 * 为某交易付款
	 * @param param 订单支付参数
	 * @param permission 需要检测的订单权限
	 */
	public void payTrade(OrderPayReturnParam param, OrderPermission permission) ;

	/**
	 * 对充值付款
	 * @param payReturnParam
	 * @param permission
     */
	void payRecharge(OrderPayReturnParam payReturnParam, OrderPermission permission);
	
	/**
	 * 发货
	 * @param delivery 运单</br>
	 * @param permission 需要检测的订单权限
	 */
	public void ship(Delivery delivery, OrderPermission permission);
	
	
	/**
	 * 订单收货
	 * @param rog  收货vo
	 * @param permission 需要检测的订单权限
	 */
	public void rog(Rog rog, OrderPermission permission);
	
	
	/**
	 * 订单取消
	 * @param cancel 取消vo
	 * @param permission 需要检测的订单权限
	 */
	public void cancel(Cancel cancel, OrderPermission permission);
	
	
	/**
	 * 订单完成
	 * @param complete 订单完成vo
	 * @param permission 需要检测的订单权限
	 */
	public void complete(Complete complete, OrderPermission permission);

	/**
	 * 更新订单的售后状态
	 * @param order_sn
	 */
	public void updateServiceStatus(String order_sn,ServiceStatus status);

	/**
	 * 为某订单付款
	 * @param param 订单支付参数
	 * @param permission 需要检测的订单权限
	 */
	public void payOrder(OrderPayReturnParam param, OrderPermission permission) ;


	/**
	 * 修改收货人信息
	 * @param consignee
	 * @return
	 */
	public OrderConsigneeVo updateOrderConsignee(OrderConsigneeVo orderConsignee);

	/**
	 * 修改订单价格
	 * @param order_sn 
	 * @param order_price
	 */
	public void updateOrderPrice(String order_sn, Double order_price);

	/**
	 * 更新订单的评论状态
	 * @param order_id
	 */
	public void updateCommentStatus(Integer order_id,CommentStatus status);
	/**
	 * 更新订单项快照ID
	 * @param items_json
	 * @param sn
	 * @return
	 */
	public void updateItemJson(String items_json,String sn);
}
