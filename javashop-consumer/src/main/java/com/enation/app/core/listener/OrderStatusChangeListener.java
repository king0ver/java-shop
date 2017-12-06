package com.enation.app.core.listener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enation.app.base.AmqpExchange;
import com.enation.app.core.receiver.OrderStatusChangeReceiver;

/**
 * 订单状态改变监听
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午3:54:34
 */
@Configuration
public class OrderStatusChangeListener {

	String queue = "order-status-change-queue";

	/**
	 * 消息监听
	 * 
	 * @param connectionFactory
	 * @param listenerAdapter
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer listenerContainerOrderStatusChange(ConnectionFactory connectionFactory,
			ChannelAwareMessageListener listenerAdapterOrderStatusChange) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue);
		container.setMessageListener(listenerAdapterOrderStatusChange);
		return container;
	}

	/**
	 * 消息监听代理
	 * 
	 * @param receiver
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapterOrderStatusChange(OrderStatusChangeReceiver receiver) {
		return new MessageListenerAdapter(receiver, "orderChange");
	}

	@Bean
	Binding bindingOrderStatusChange(Queue queueOrderStatusChange, FanoutExchange exchangeOrderStatusChange) {
		return BindingBuilder.bind(queueOrderStatusChange).to(exchangeOrderStatusChange);
	}

	@Bean
	Queue queueOrderStatusChange() {
		return new Queue(queue, false);
	}

	@Bean
	FanoutExchange exchangeOrderStatusChange() {
		return new FanoutExchange(AmqpExchange.ORDER_STATUS_CHANGE.name());
	}
}
