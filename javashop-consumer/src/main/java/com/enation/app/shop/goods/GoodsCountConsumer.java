package com.enation.app.shop.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.AmqpExchange;
import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.OrderItem;
import com.enation.app.shop.trade.service.IOrderItemManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.jms.support.goods.GoodsChangeMsg;

/**
 * 
 * 收货后添加商品购买数量
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月26日 下午4:58:51
 */
@Component
public class GoodsCountConsumer implements IOrderStatusChangeEvent {
	@Autowired
	private IOrderItemManager orderItemQueryManager;
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private AmqpTemplate amqpTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		if (orderMessage.getNewStatus().name().equals(OrderStatus.ROG.name())) {
			List<OrderItem> orderItemList = orderItemQueryManager
					.queryByOrderSn(orderMessage.getOrder().getSn().toString());
			Set<Integer> set = new  HashSet<Integer>(); 
			for (OrderItem orderItem : orderItemList) {
				String sql = "update es_goods set buy_count=buy_count+? where goods_id=?";
				this.daoSupport.execute(sql, orderItem.getNum(), orderItem.getGoods_id());
				set.add(orderItem.getGoods_id());
				/**
				 * 更新orderItem ship_num为统计商品下单量提供数据
				 */
				orderItem.setShip_num(orderItem.getNum());
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("item_id", orderItem.getItem_id());
				this.daoSupport.update("es_order_items", orderItem, paramMap);
			}
			// 发送修改商品消息
			GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(set.toArray(new Integer[set.size()]),
					GoodsChangeMsg.UPDATE_OPERATION);
			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey", goodsChangeMsg);
		}

	}

}
