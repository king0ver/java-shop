package com.enation.app.core.event;

import com.enation.app.shop.aftersale.support.RefundChangeMessage;

/**
 * 
 * 退款/退货申请
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年11月16日 下午5:59:32
 */
public interface IRefundStatusChangeEvent {
	public void refund(RefundChangeMessage refundPartVo);
}
