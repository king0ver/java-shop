package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IRefundStatusChangeEvent;
import com.enation.app.shop.aftersale.support.RefundChangeMessage;
@Component
public class RefundStatusChangeReceiver {
	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired(required = false)
	private List<IRefundStatusChangeEvent> events;

	public void refund(RefundChangeMessage refundPartVo) {
		try {
			if (events != null) {
				for (IRefundStatusChangeEvent event : events) {
					event.refund(refundPartVo);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理退款通过消息出错", e);
			e.printStackTrace();
		}
	}
}
