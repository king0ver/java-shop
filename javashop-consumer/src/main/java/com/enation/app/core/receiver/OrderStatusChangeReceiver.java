package com.enation.app.core.receiver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;

/**
 * 订单状态改变消费者
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午3:57:34
 */
@Component
public class OrderStatusChangeReceiver {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Autowired(required=false)
	private List<IOrderStatusChangeEvent> events;
	
	public void orderChange(OrderStatusChangeMessage orderMessage){

		try {
			if (events != null) {
				for (IOrderStatusChangeEvent event : events) {
					event.orderChange(orderMessage);
				}
			}
		} catch (Exception e) {
			logger.error("处理订单状态变化消息出错",e);
			e.printStackTrace();
		}

		
	}
	
}
