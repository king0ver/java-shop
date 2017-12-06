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
import com.enation.app.core.receiver.MobileIndexChangeReceiver;

/**
 * 
 * 首页变化监听
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:26:33
 */
@Configuration
public class MobileIndexChangeListener {
	
	String queue = "mobile-index-change-queue";

	/**
	 * 消息监听
	 * 
	 * @param connectionFactory
	 * @param listenerAdapter
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer listenerContainerMobileIndexChange(ConnectionFactory connectionFactory,
			ChannelAwareMessageListener listenerAdapterMobileIndexChange) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue);
		container.setMessageListener(listenerAdapterMobileIndexChange);
		return container;
	}

	/**
	 * 消息监听代理
	 * 
	 * @param receiver
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapterMobileIndexChange(MobileIndexChangeReceiver receiver) {
		return new MessageListenerAdapter(receiver, "mobileIndexChange");
	}

	@Bean
	Binding bindingMobileIndexChange(Queue queueMobileIndexChange, FanoutExchange exchangeMobileIndexChange) {
		return BindingBuilder.bind(queueMobileIndexChange).to(exchangeMobileIndexChange);
	}

	@Bean
	Queue queueMobileIndexChange() {
		return new Queue(queue, false);
	}

	@Bean
	FanoutExchange exchangeMobileIndexChange() {
		return new FanoutExchange(AmqpExchange.MOBILE_INDEX_CHANGE.name());
	}


}
