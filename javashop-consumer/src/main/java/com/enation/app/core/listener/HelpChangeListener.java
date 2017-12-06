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
import com.enation.app.core.receiver.HelpChangeReceiver;

/**
 * 帮助中心页面变化消费者
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:25:48
 */
@Configuration
public class HelpChangeListener {
	
	String queue = "help-change-queue";

	/**
	 * 消息监听
	 * 
	 * @param connectionFactory
	 * @param listenerAdapter
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer listenerContainerHelpChange(ConnectionFactory connectionFactory,
			ChannelAwareMessageListener listenerAdapterHelpChange) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue);
		container.setMessageListener(listenerAdapterHelpChange);
		return container;
	}

	/**
	 * 消息监听代理
	 * 
	 * @param receiver
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapterHelpChange(HelpChangeReceiver receiver) {
		return new MessageListenerAdapter(receiver, "helpChange");
	}

	@Bean
	Binding bindingHelpChange(Queue queueHelpChange, FanoutExchange exchangeHelpChange) {
		return BindingBuilder.bind(queueHelpChange).to(exchangeHelpChange);
	}

	@Bean
	Queue queueHelpChange() {
		return new Queue(queue, false);
	}

	@Bean
	FanoutExchange exchangeHelpChange() {
		return new FanoutExchange(AmqpExchange.HELP_CHANGE.name());
	}

}
