package com.enation.app.shop.order.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.core.event.IRefundStatusChangeEvent;
import com.enation.app.shop.aftersale.model.po.RefundGoods;
import com.enation.app.shop.aftersale.support.RefundChangeMessage;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.database.IDaoSupport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 订单中赠品库存的改变
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月26日 下午8:49:01
 */
@Component
public class OrderGiftRepertoryChangeConsumer implements IOrderStatusChangeEvent, IRefundStatusChangeEvent {
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {

		// 发货减少赠品库存
		if (orderMessage.getNewStatus().name().equals(OrderStatus.SHIPPED.name())) {
			Gson gson = new Gson();
			String sql = "select meta_value from es_order_meta where order_sn=? and meta_key='gift'";
			String fullDiscountGift = daoSupport.queryForString(sql, orderMessage.getOrder().getSn());
			List<FullDiscountGift> giftlist = gson.fromJson(fullDiscountGift, new TypeToken<List<FullDiscountGift>>() {
			}.getType());
			if (giftlist != null && giftlist.size() > 0) {
				for (FullDiscountGift gift : giftlist) {
					// 无论这个订单的赠品有几个，每循环一次giftlist就把这个赠品-1，因为赠品有几个的话giftlist长度就是几
					String giftsql = "update es_full_discount_gift set actual_store=actual_store-1 ,enable_store=enable_store-1 where gift_id=?";
					daoSupport.execute(giftsql, gift.getGift_id());
				}

			}
		}

	}

	@Override
	public void refund(RefundChangeMessage refundPartVo) {
		// 退货入库增加赠品库存
		if (refundPartVo.getOperation_type().equals(RefundChangeMessage.SELLER_IN_STOCK)) {
			String sql = "select * from es_refund_goods where refund_sn=?";
			RefundGoods refundGoods = this.daoSupport.queryForObject(sql, RefundGoods.class,
					refundPartVo.getRefund().getSn());
			if (refundGoods.getRefund_gift() != null) {
				Gson gson = new Gson();
				List<FullDiscountGift> giftlist = gson.fromJson(refundGoods.getRefund_gift(),
						new TypeToken<List<FullDiscountGift>>() {
						}.getType());
				if (giftlist != null && giftlist.size() > 0) {
					for (FullDiscountGift gift : giftlist) {
						// 无论这个订单的赠品有几个，每循环一次giftlist就把这个赠品的可用库存和实际库存+1，因为赠品有几个的话giftlist长度就是几
						String giftsql = "update es_full_discount_gift set enable_store=enable_store+1 ,actual_store=actual_store+1 where gift_id=?";
						daoSupport.execute(giftsql, gift.getGift_id());
					}
				}
			}
		}
	}
}
