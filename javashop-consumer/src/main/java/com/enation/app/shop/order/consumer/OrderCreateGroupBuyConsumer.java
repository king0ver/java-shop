package com.enation.app.shop.order.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.trade.model.po.OrderItem;
import com.enation.app.shop.trade.service.IOrderItemManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 团购消费 订单创建消息 
 * @author Chopper
 * @version v1.0
 * @since v6.4
 * 2017年9月11日 上午10:18:48 
 *
 */
@Component
public class OrderCreateGroupBuyConsumer {

	@Autowired
	private IDaoSupport daoSupport;
	 
	
	@Autowired
	private IOrderItemManager itemQueryManager;
	
	/**
	 * 执行的监听的方法
	 * @param order_sn
	 */
	public void receive(String order_sn) {
		itemQueryManager.queryByOrderSn(order_sn);
	}
	
	
	/**
	 * 订单创建之后，修改团购状态
	 * @param orderitemList
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void afterOrderCreate(List<OrderItem> orderitemList) {
		String sql = "update es_groupbuy_goods set buy_num=buy_num+?,goods_num=goods_num-? where goods_id=? and act_id=?";
//		for (OrderItem orderItem : orderitemList) {
//			if(orderItem.getPromotion_type().equals(PromotionTypeEnum.GROUPBUY.getType())) {
//				this.daoSupport.execute(sql, orderItem.getNum(),  orderItem.getNum(), orderItem.getGoods_id(),
//						orderItem.getPromotion_id());
//			}
//		}
	}
}
