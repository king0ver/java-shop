var Order = {
	/**
	 * 确认收货
	 * @param order_sn
	 * @param success
	 * @param error
	 */
	rog: function (order_sn, success, error) {
		$.ajax({
			type: 'POST',
			url: ctx + '/order-opration/mine/order/rog/' + order_sn + '.do',
			dataType: 'json',
			success: success,
			error: error
		})
	},
	/**
	 * 取消订单
	 * @param params
	 * @param success
	 * @param error
	 */
	cancel: function (params, success, error) {
		$.ajax({
			type: 'POST',
			url: ctx + '/order-opration/mine/order/cancel/' + params.order_sn + '.do',
			data: { reson: params.reason },
			success: success,
			error: error
		})
	}
};