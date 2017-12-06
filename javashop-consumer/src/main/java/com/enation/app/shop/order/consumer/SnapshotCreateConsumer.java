package com.enation.app.shop.order.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goods.service.ICategoryParamsManager;
import com.enation.app.shop.goods.service.IGoodsGalleryManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.snapshot.service.ISnapshotManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;

@Component
public class SnapshotCreateConsumer implements IOrderStatusChangeEvent{
	
	@Autowired
	IGoodsManager goodsManager;
	
	@Autowired
	ICategoryParamsManager categoryParamsManager;
	
	@Autowired
	IGoodsGalleryManager goodsGalleryManager;
	
	@Autowired
	IOrderOperateManager orderOperateManager;
	
	@Autowired
	IBrandManager brandManager;
	
	@Autowired
	ICategoryManager categoryManager;
	
	@Autowired
	ISnapshotManager snapshotManager;
	
	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		if(orderMessage.getNewStatus().equals(OrderStatus.CONFIRM)) {
			
			//获取订单详情
			OrderPo order = orderMessage.getOrder();
			
			snapshotManager.add(order);
		}
	}
}	
