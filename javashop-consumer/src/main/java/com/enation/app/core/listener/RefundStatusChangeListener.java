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
import com.enation.app.core.receiver.RefundStatusChangeReceiver;

/**
 * 退款通过
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:44:42
 */
@Configuration
public class RefundStatusChangeListener {

	String queue = "refund-pass-queue";

	/**
	 * 消息监听∂
	 * 
	 * @param connectionFactory
	 * @param listenerAdapter
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer listenerContainerRefundBill(ConnectionFactory connectionFactory,
			ChannelAwareMessageListener listenerAdapterRefundBill) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue);
		container.setMessageListener(listenerAdapterRefundBill);
		return container;
	}

	/**
	 * 消息监听代理
	 * 
	 * @param receiver
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapterRefundBill(RefundStatusChangeReceiver receiver) {
		return new MessageListenerAdapter(receiver, "refund");
	}

	@Bean
	Binding bindingRefundBill(Queue queueRefundBill, FanoutExchange exchangeRefundBill) {
		return BindingBuilder.bind(queueRefundBill).to(exchangeRefundBill);
	}

	@Bean
	Queue queueRefundBill() {
		return new Queue(queue, false);
	}

	@Bean
	FanoutExchange exchangeRefundBill() {
		return new FanoutExchange(AmqpExchange.REFUND_STATUS_CHANGE.name());
	}
}
