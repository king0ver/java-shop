package com.enation.app.shop.trade.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.shop.trade.model.enums.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;
import com.enation.app.shop.goods.service.IGoodsQuantityManager;
import com.enation.app.shop.payment.model.po.PaymentBill;
import com.enation.app.shop.payment.model.vo.OrderPayReturnParam;
import com.enation.app.shop.payment.service.IPaymentBillManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.support.OrderOperateChecker;
import com.enation.app.shop.trade.model.po.OrderLog;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.OperateAllowable;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.OrderConsigneeVo;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.model.vo.operator.Cancel;
import com.enation.app.shop.trade.model.vo.operator.Complete;
import com.enation.app.shop.trade.model.vo.operator.Confirm;
import com.enation.app.shop.trade.model.vo.operator.Delivery;
import com.enation.app.shop.trade.model.vo.operator.Rog;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;
import com.enation.framework.validator.ResourceNotFoundException;
import com.enation.framework.validator.UnProccessableServiceException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 订单操作业务类
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月3日下午8:02:52
 */
@Service
public class OrderOperateManager implements IOrderOperateManager {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private ISellerManager sellerManager;
	
	@Autowired
	private IPaymentBillManager paymentBillManager;

	@Autowired
    private AmqpTemplate amqpTemplate;
	
	@Autowired
	private IGoodsQuantityManager goodsQuantityManager;

	/**
	 * 确认订单
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void confirm(Confirm confirm, OrderPermission permission) {

		String ordersn = confirm.getOrder_sn();

		// 获取此订单
		OrderPo order =  orderQueryManager.getOneBySn(ordersn);

		if (order == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "找不到订单[" + ordersn + "]");
		}

		// 进行权限校验
		this.checkPermission(permission, order);

		// 进行操作校验
		this.checkAllowable(order, OrderOperate.confirm);
		
		//先扣减库存，失败则不修改状态
		Gson gson = new Gson();
		List<Product> list = gson.fromJson(order.getItems_json(),  new TypeToken< List<Product> >() {  }.getType());
		
		//判断库存扣减的失败
		boolean flag = true;
		/**
		 * 此List记录已经成功扣减库存的产品
		 * 当如果productList中的产品有一个扣减库存失败，那么要将库存已经已经扣减成功的产品, 回滚库存。
		 * 因redis不支持事务，不能回滚。
		 */
		List<Product> productIntoDBList = new ArrayList<Product>();
		
		//减库存
		for(Product sku : list){
			GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
			goodsQuantity.setEnable_quantity(0);
			goodsQuantity.setGoods_id(sku.getGoods_id());
			goodsQuantity.setQuantity(sku.getNum());
			goodsQuantity.setSku_id(sku.getProduct_id());
			
			if(this.goodsQuantityManager.reduceGoodsQuantity(goodsQuantity)){
				productIntoDBList.add(sku);
			}else{
				flag = false;
				break;
			}
		}
		
		//如果扣减库存出现失败，则回滚库存
		if(!flag){
			
			for(Product product : productIntoDBList){
				//库存回滚（可用库存）
				GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
				goodsQuantity.setEnable_quantity(0);
				goodsQuantity.setGoods_id(product.getGoods_id());
				goodsQuantity.setQuantity(product.getNum());
				goodsQuantity.setSku_id(product.getProduct_id());
				this.goodsQuantityManager.addGoodsQuantity(goodsQuantity);
			}
			
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "实际库存不足");
		}

		this.daoSupport.execute("update es_order set order_status=?  where sn=? ", OrderStatus.CONFIRM.value(),
				ordersn);
		OrderStatusChangeMessage message = new OrderStatusChangeMessage();
		message.setOrder(order);
		message.setOldStatus(OrderStatus.valueOf(order.getOrder_status()));
		message.setNewStatus(OrderStatus.CONFIRM);

		//TODO 发送订单状态变更消息
		this.amqpTemplate.convertAndSend(AmqpExchange.ORDER_STATUS_CHANGE.name(),"order-change-queue", message);
		order.setOrder_status(OrderStatus.CONFIRM.value());

		// 记录日志
		this.log(ordersn, "确认订单", confirm.getOperator());
		// 将当前当订单信息以OrderLine格式写入到trade表中到order_json字段中
//		this.orderToWriTrade(order.getTrade_sn());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OrderPo payOrder(String ordersn, Double payprice, OrderPermission permission) {

		// 获取此订单
		OrderDetail order = this.orderQueryManager.getOneBySn(ordersn);

		OperateAllowable operateAllowable = new OperateAllowable(PaymentType.valueOf(order.getPayment_type()),
				OrderStatus.valueOf(order.getOrder_status()), CommentStatus.valueOf(order.getComment_status()),
				ShipStatus.valueOf(order.getShip_status()),ServiceStatus.valueOf(order.getService_status()),
				PayStatus.valueOf(order.getPay_status()));
		order.setOperateAllowable(operateAllowable);
		if (order == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "找不到订单[" + ordersn + "]");
		}

		this.payOrder(order, payprice, permission);

		return order;
	}

	private void payOrder(OrderPo order, Double payprice, OrderPermission permission) {

		// 进行权限校验
		this.checkPermission(permission, order);

		// 进行操作校验
		this.checkAllowable(order, OrderOperate.pay);

		// 付款金额和订单金额不相等
		if (payprice.compareTo(order.getOrder_price()) != 0) {
			throw new UnProccessableServiceException(ErrorCode.ORDER_PAY_VALID_ERROR, "付款金额和应付金额不一致");
		}

		this.daoSupport.execute("update es_order set order_status=? ,pay_status=? ,paymoney=? ,payment_time = ? where sn=? ",
				OrderStatus.PAID_OFF.value(), PayStatus.PAY_YES.value(),payprice,DateUtil.getDateline(), order.getSn());
		OrderStatusChangeMessage message = new OrderStatusChangeMessage();
		message.setOrder(order);
		message.setOldStatus(OrderStatus.CONFIRM);
		message.setNewStatus(OrderStatus.PAID_OFF);
		this.amqpTemplate.convertAndSend(AmqpExchange.ORDER_STATUS_CHANGE.name(),"order-change-queue", message);
		order.setOrder_status(OrderStatus.PAID_OFF.value());
		order.setPay_status(PayStatus.PAY_YES.value());

		// 记录日志
		this.log(order.getSn(), "支付订单", "支付确认系统");
//		this.orderToWriTrade(order.getTrade_sn());

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void payTrade(OrderPayReturnParam param, OrderPermission permission) {
		
		PaymentBill bill = paymentBillManager.getByPayKey(param.getPay_key());
		List<Order> orderList = this.orderQueryManager.queryByTradeSnGetOrder(bill.getSn());
		Double money = 0.00;
		for(Order o : orderList){
			money += o.getOrder_price();
		}
		
		if(money-param.getPayprice()==0){
			
			this.changeOrderPayStatus(param,"trade_sn",bill);
		}
	}

	@Override
	public void payRecharge(OrderPayReturnParam payReturnParam, OrderPermission permission) {



	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void payOrder(OrderPayReturnParam param, OrderPermission permission) {
		
		PaymentBill bill = paymentBillManager.getByPayKey(param.getPay_key());
		
		OrderDetail order = this.orderQueryManager.getOneBySn(bill.getSn());
		
		if(order.getOrder_price()-param.getPayprice()==0){
			
			this.changeOrderPayStatus(param,"sn",bill);
		}
		
	}
	
	/**
	 * 改变订单的支付状态
	 * @param param
	 * @param orderStr
	 */
	private void changeOrderPayStatus(OrderPayReturnParam param,String orderStr,PaymentBill bill){
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("payment_method_id", param.getPayment_method_id());
		map.put("payment_plugin_id", param.getPayment_plugin_id()); //支付插件id
		map.put("payment_method_name", param.getPayment_method_name()); //支付方式名称
		map.put("order_status", OrderStatus.PAID_OFF.name()); //交易状态
		map.put("payment_time", DateUtil.getDateline()); //支付时间
		map.put("pay_status", PayStatus.PAY_YES.value()); //支付状态
		map.put("pay_order_no", param.getPay_order_no());
		map.put("paymoney", param.getPayprice());
		
		//更新流水中的动态
		bill.setPay_order_no(param.getPay_order_no());
		bill.setIs_pay(1);
		paymentBillManager.update(bill);
		// 更改订单的交易方式
		this.daoSupport.update("es_order", map, orderStr+" = " + bill.getSn());
		String trade = "";
		if(orderStr.equals("trade_sn")){
			String sql = "update es_order set paymoney = need_pay_money where trade_sn = ? ";
			this.daoSupport.execute(sql, bill.getSn());
			trade = "交易";
		}
		this.log(trade+bill.getSn(), "在线支付,"+param.getPayment_method_name()+" "+param.getPayprice()+"元成功", "会员");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void ship(Delivery delivery, OrderPermission permission) {

		String ordersn = delivery.getOrder_sn();

		// 获取此订单
		OrderPo order = orderQueryManager.getOneBySn(ordersn);

		if (order == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "找不到订单[" + ordersn + "]");
		}

		// 进行权限校验
		this.checkPermission(permission, order);

		// 进行操作校验
		this.checkAllowable(order, OrderOperate.ship);

		this.daoSupport.execute("update es_order set order_status=? ,ship_status=? ,ship_no=? ,ship_time = ?,logi_id=?,logi_name=? where sn=? ",
				OrderStatus.SHIPPED.value(), ShipStatus.SHIP_YES.value(), delivery.getDelivery_no(),DateUtil.getDateline(),
				delivery.getLogi_id(),delivery.getLogi_name(), order.getSn());
		OrderStatusChangeMessage message = new OrderStatusChangeMessage();
		//获取更新后的订单数据
		OrderPo orders = orderQueryManager.getOneBySn(ordersn);
		message.setOrder(orders);
		message.setOldStatus(OrderStatus.valueOf(order.getOrder_status()));
		message.setNewStatus(OrderStatus.SHIPPED);

		//TODO 发送订单状态变更消息
		this.amqpTemplate.convertAndSend(AmqpExchange.ORDER_STATUS_CHANGE.name(),"order-change-queue", message);
		order.setOrder_status(OrderStatus.SHIPPED.value());
		order.setShip_status(OrderStatus.SHIPPED.value());

		// 记录日志
		this.log(ordersn, "发货,单号[" + delivery.getDelivery_no() + "]", delivery.getOperator());

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void rog(Rog rog, OrderPermission permission) {
		String ordersn = rog.getOrder_sn();

		// 获取此订单
		OrderPo order = orderQueryManager.getOneBySn(ordersn);

		if (order == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "找不到订单[" + ordersn + "]");
		}

		// 进行权限校验
		this.checkPermission(permission, order);

		// 进行操作校验
		this.checkAllowable(order, OrderOperate.rog);

		this.daoSupport.execute("update es_order set order_status=? ,ship_status=? ,signing_time = ?   where sn=? ",
				OrderStatus.ROG.value(), ShipStatus.SHIP_ROG.value(),DateUtil.getDateline(), order.getSn());
		OrderStatusChangeMessage message = new OrderStatusChangeMessage();
		message.setOrder(order);
		message.setOldStatus(OrderStatus.valueOf(order.getOrder_status()));
		message.setNewStatus(OrderStatus.ROG);

		//发送订单状态变更消息
		this.amqpTemplate.convertAndSend(AmqpExchange.ORDER_STATUS_CHANGE.name(),"order-change-queue", message);
		order.setOrder_status(OrderStatus.ROG.value());
		order.setShip_status(ShipStatus.SHIP_ROG.value());

		// 记录日志
		this.log(ordersn, "确认收货", rog.getOperator());

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void cancel(Cancel cancel, OrderPermission permission) {

		String ordersn = cancel.getOrder_sn();
		// 获取此订单
		OrderPo order =orderQueryManager.getOneBySn(ordersn);

		if (order == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "找不到订单[" + ordersn + "]");
		}

		// 进行权限校验
		this.checkPermission(permission, order);

		// 进行操作校验
		this.checkAllowable(order, OrderOperate.cancel);

		this.daoSupport.execute("update es_order set order_status=? , cancel_reason=? where sn=? ", OrderStatus.CANCELLED.value(),cancel.getReson(),
				order.getSn());
		OrderStatusChangeMessage message = new OrderStatusChangeMessage();
		message.setOrder(order);
		message.setOldStatus(OrderStatus.valueOf(order.getOrder_status()));
		message.setNewStatus(OrderStatus.CANCELLED);

		//TODO 发送订单状态变更消息
		this.amqpTemplate.convertAndSend(AmqpExchange.ORDER_STATUS_CHANGE.name(),"order-change-queue", message);
		order.setOrder_status(OrderStatus.CANCELLED.value());

		// 记录日志
		this.log(ordersn, "取消订单", cancel.getOperator());
//		this.orderToWriTrade(order.getTrade_sn());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void complete(Complete complete, OrderPermission permission) {

		String ordersn = complete.getOrder_sn();

		// 获取此订单
		OrderPo order = orderQueryManager.getOneBySn(ordersn);

		if (order == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "找不到订单[" + ordersn + "]");
		}

		// 进行权限校验
		this.checkPermission(permission, order);

		// 进行操作校验
		this.checkAllowable(order, OrderOperate.complete);

		this.daoSupport.execute("update es_order set order_status=?,complete_time=?  where sn=? ", OrderStatus.COMPLETE.value(),
				DateUtil.getDateline(),order.getSn());
		OrderStatusChangeMessage message = new OrderStatusChangeMessage();
		message.setOrder(order);
		message.setOldStatus(OrderStatus.valueOf(order.getOrder_status()));
		message.setNewStatus(OrderStatus.COMPLETE);

		//TODO 发送订单状态变更消息
		this.amqpTemplate.convertAndSend(AmqpExchange.ORDER_STATUS_CHANGE.name(),"order-change-queue", message);
		order.setOrder_status(OrderStatus.COMPLETE.value());

		// 记录日志
		this.log(ordersn, "订单完成", complete.getOperator());
	}

	/**
	 * 记录订单日志
	 * 
	 * @param ordersn
	 *            订单编号
	 * @param message
	 *            操作信息
	 * @param op_name
	 *            操作者
	 */
	private void log(String ordersn, String message, String op_name) {
		OrderLog orderLog = new OrderLog();
		orderLog.setMessage(message);
		orderLog.setOp_name(op_name);
		orderLog.setOrder_sn(ordersn);
		orderLog.setOp_time(DateUtil.getDateline());
		this.daoSupport.insert("es_order_log", orderLog);
	}

	/**
	 * 对要操作的订单进行权限检查
	 * 
	 * @param permission
	 *            需要的权限
	 * @param order
	 *            相应的订单
	 */
	private void checkPermission(OrderPermission permission, OrderPo order) {

		if (permission != null) {

			// 校验卖家权限
			if (permission.equals(OrderPermission.seller)) {
				Seller seller = sellerManager.getSeller();

				if (seller == null || seller.getStore_id() != order.getSeller_id().intValue()) {
					throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权操作此订单");
				}

			}

			// 校验买家权限
			if (permission.equals(OrderPermission.buyer)) {
				Member member  = UserConext.getCurrentMember();

				if (member == null || member.getMember_id() == null
						|| member.getMember_id().intValue() != order.getMember_id().intValue()) {
					throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权操作此订单");
				}

			}

			// 校验管理权限
			if (permission.equals(OrderPermission.admin)) {

				AdminUser adminUser  = UserConext.getCurrentAdminUser();

				if (adminUser == null) {
					throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权操作此订单");
				}

			}

			// 目前客户端不用校栓任何权限
			if (permission.equals(OrderPermission.client)) {

			}

		}
	}

	/**
	 * 进行操作校验 看此状态下是否允许此操作
	 * 
	 * @param order
	 * @param orderOperate
	 */
	private void checkAllowable(OrderPo order, OrderOperate orderOperate) {

		OrderStatus status = OrderStatus.valueOf(order.getOrder_status());

		PaymentType paymentType = PaymentType.valueOf(order.getPayment_type());
		boolean allowble = OrderOperateChecker.checkAllowable(paymentType, status, orderOperate);

		if (!allowble) {
			throw new NoPermissionException("",
					"订单" + status.description() + "状态不能进行" + orderOperate.description() + "操作");
		}

	}

	@Override
	public void updateServiceStatus(String order_sn,ServiceStatus status) {

		//如果是进行申请，则订单状态更改为售后中
		if (status.value().equals( ServiceStatus.APPLY.value() )){
			String order_status = OrderStatus.AFTE_SERVICE.value();
			String sql = "update es_order set service_status = ?,order_status=? where sn = ? ";
			this.daoSupport.execute(sql, status.value(),order_status,order_sn);
		}else{//否则只更新售后状态
			String sql = "update es_order set service_status = ?  where sn = ? ";
			this.daoSupport.execute(sql, status.value(),order_sn);
		}


	}

//	private void orderToWriTrade(String trade_sn) {
//		Gson gson = new Gson();
//
//		// 在交易表找到发货的订单
//		List<Order> order_json = orderQueryManager.queryByTradeSnGetOrder(trade_sn);
//		List<OrderLine> orderLineList = new ArrayList<OrderLine>();
//		for (Order order : order_json) {
//			String item_json = order.getItems_json();
//			order.setProductList(gson.fromJson(item_json, new TypeToken<List<Product>>() {
//			}.getType()));  
//			OrderLine orderLine = new OrderLine(order);
//			orderLineList.add(orderLine);
//		}
//		String tardeSql = "update es_trade set order_json=? where trade_sn=?";
//		this.daoSupport.execute(tardeSql, gson.toJson(orderLineList), trade_sn);
//	}

	@Override
	public OrderConsigneeVo updateOrderConsignee(OrderConsigneeVo orderConsignee) {
		
		OrderPo order = this.daoSupport.queryForObject("select * from es_order where sn = ?", OrderPo.class, orderConsignee.getSn());
		order.setShip_province(orderConsignee.getProvince());
		order.setShip_provinceid(orderConsignee.getProvince_id());
		order.setShip_city(orderConsignee.getCity());
		order.setShip_cityid(orderConsignee.getCity_id());
		order.setShip_region(orderConsignee.getRegion());
		order.setShip_regionid(orderConsignee.getRegion_id());
		order.setShip_town(orderConsignee.getTown());
		order.setShip_townid(orderConsignee.getTown_id());
		
		order.setShip_addr(orderConsignee.getShip_addr());
		order.setShip_mobile(orderConsignee.getShip_mobile());
		order.setShip_tel(orderConsignee.getShip_tel());
		order.setReceive_time(orderConsignee.getReceive_time());
		order.setShip_name(orderConsignee.getShip_name());
		order.setRemark(orderConsignee.getRemark());
		this.daoSupport.update("es_order", order, "sn="+orderConsignee.getSn());
		
		return orderConsignee;
	}

	@Override
	public void updateOrderPrice(String order_sn,Double order_price) {
		this.daoSupport.execute("update es_order set order_price = ? where sn = ?", order_price,order_sn);
		this.log(order_sn, "商家修改订单价格","店铺"+sellerManager.getSeller().getName());
	}

	@Override
	public void updateCommentStatus(Integer order_id, CommentStatus status) {
		String sql = "update es_order set comment_status = ? where order_id = ? ";
		this.daoSupport.execute(sql, status.name(),order_id);
	}

	@Override
	public void updateItemJson(String items_json,String sn) {
		String sql = "update es_order set items_json = ? where  sn = ? ";
		this.daoSupport.execute(sql, items_json,sn);
	} 


}
