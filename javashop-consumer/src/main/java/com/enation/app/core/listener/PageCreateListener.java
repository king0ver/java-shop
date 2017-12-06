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
import com.enation.app.core.receiver.PageCreateReceiver;

/**
 * 
 * 静态也生成消息消费者
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:27:29
 */
@Configuration
public class PageCreateListener {

	String queue = "page-change-queue";

	/**
	 * 消息监听
	 * 
	 * @param connectionFactory
	 * @param listenerAdapter
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer listenerContainerPageCreate(ConnectionFactory connectionFactory,
			ChannelAwareMessageListener listenerAdapterPageCreate) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue);
		container.setMessageListener(listenerAdapterPageCreate);
		return container;
	}

	/**
	 * 消息监听代理
	 * 
	 * @param receiver
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapterPageCreate(PageCreateReceiver receiver) {
		return new MessageListenerAdapter(receiver, "createPage");
	}

	@Bean
	Binding bindingPageCreate(Queue queuePageCreate, FanoutExchange exchangePageCreate) {
		return BindingBuilder.bind(queuePageCreate).to(exchangePageCreate);
	}

	@Bean
	Queue queuePageCreate() {
		return new Queue(queue, false);
	}

	@Bean
	FanoutExchange exchangePageCreate() {
		return new FanoutExchange(AmqpExchange.PAGE_CREATE.name());
	}
}
