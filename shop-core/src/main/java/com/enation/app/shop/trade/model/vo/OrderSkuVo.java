package com.enation.app.shop.trade.model.vo;

import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;

/**
 * 
 * 会员，订单列表标识是否可售后
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年11月16日 下午12:55:10
 */
public class OrderSkuVo extends Product {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8472836126812686551L;
	private boolean allowApplyService;
	private String orderStatus;
	private String payStatus;
	private String shipStatus;
	private String payment_type;

	public OrderSkuVo(String orderStatus, String payStatus, String shipStatus, String payment_type) {
		super();
		this.orderStatus = orderStatus;
		this.payStatus = payStatus;
		this.shipStatus = shipStatus;
		this.payment_type = payment_type;
	}

	public boolean isAllowApplyService() {

		boolean service_status = ServiceStatus.NOT_APPLY.value().equals(super.getService_status());
		// 货到付款
		if (PaymentType.cod.name().compareTo(this.payment_type) == 0) {

			allowApplyService = ShipStatus.SHIP_ROG.value().equals(this.shipStatus) && service_status;

		} else {

			allowApplyService = PayStatus.PAY_YES.value().equals(this.payStatus) && service_status
					&& !OrderStatus.CANCELLED.equals(this.orderStatus);
		}

		return allowApplyService;

	}

	public void setAllowApplyService(boolean allowApplyService) {
		this.allowApplyService = allowApplyService;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}

}
