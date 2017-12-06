package com.enation.app.shop.orderbill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.core.event.IRefundStatusChangeEvent;
import com.enation.app.shop.aftersale.model.po.Refund;
import com.enation.app.shop.aftersale.support.RefundChangeMessage;
import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.app.shop.orderbill.service.IBillItemManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;

/**
 * 订单收货生成结算单
 * 
 * @author fk
 * @version v6.4
 * @since v6.4 pass 2017年9月29日 下午4:04:14
 */
@Service
public class OrderBillCreateConsumer implements IOrderStatusChangeEvent, IRefundStatusChangeEvent {

	@Autowired
	private IBillItemManager billItemManager;

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {

		if (orderMessage.getNewStatus().equals(OrderStatus.ROG)) {
			BillItem billItem = getBillItem(orderMessage.getOrder().getSn(), 0);
			this.billItemManager.add(billItem);
		}
	}

	@Override
	public void refund(RefundChangeMessage refundPartVo) {
		if (refundPartVo.getOperation_type().equals(RefundChangeMessage.AUTH)) {
			Refund refund=refundPartVo.getRefund();
			BillItem billItem = getBillItem(refund.getOrder_sn(), 1);
			billItem.setRefund_sn(refund.getSn());
			billItem.setRefund_id(refund.getId());
			billItem.setRefund_time(refund.getCreate_time());
			this.billItemManager.add(billItem);
		}

	}

	/**
	 * 构造一个结算单项
	 * 
	 * @param order_sn
	 * @param item_type
	 * @return
	 */
	private BillItem getBillItem(String order_sn, Integer item_type) {
		OrderDetail orderDetail = this.orderQueryManager.getOneBySn(order_sn);

		Assert.notNull(orderDetail, "找不到订单");
		Double order_price = orderDetail.getOrder_price();
		Double discount_price = orderDetail.getDiscount_price();

		BillItem item = new BillItem(item_type);
		item.setOrder_sn(order_sn);
		item.setOrder_price(order_price);
		item.setDiscount_price(discount_price);
		item.setSeller_id(orderDetail.getSeller_id());
		item.setMember_id(orderDetail.getMember_id());
		item.setMember_name(orderDetail.getMember_name());
		item.setOrder_time(orderDetail.getCreate_time());
		item.setPayment_type(orderDetail.getPayment_type());
		item.setShip_name(orderDetail.getShip_name());
		return item;
	}

}
