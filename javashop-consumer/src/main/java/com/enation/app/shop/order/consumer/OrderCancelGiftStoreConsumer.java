package com.enation.app.shop.order.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.database.IDaoSupport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 订单取消时增加订单赠品的可用库存
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月26日 下午8:25:10
 */
@Component
public class OrderCancelGiftStoreConsumer implements IOrderStatusChangeEvent{
	@Autowired
	private IDaoSupport daoSupport;
	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		if (orderMessage.getNewStatus().name().equals(OrderStatus.CANCELLED.name())) {
			Gson gson = new Gson();
			String sql="select meta_value from es_order_meta where order_sn=? and meta_key='gift'";
			String fullDiscountGift= daoSupport.queryForString(sql,orderMessage.getOrder().getSn());
			List<FullDiscountGift> giftlist = gson.fromJson(fullDiscountGift, new TypeToken<List<FullDiscountGift>>() {}.getType());
			if(giftlist!=null&&giftlist.size()>0) {
				for (FullDiscountGift gift:giftlist) {
					//当前取消的订单有赠品
					String giftsql="update es_activity_gift set enable_store=enable_store+1 ,actual_store=actual_store+1 where gift_id=?";
					daoSupport.execute(giftsql, gift.getGift_id());
				}
			}
		}
	}
}
