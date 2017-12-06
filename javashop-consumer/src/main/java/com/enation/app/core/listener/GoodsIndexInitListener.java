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
import com.enation.app.core.receiver.GoodsIndexInitReceiver;

/**
 * 
 * 静态也生成消息消费者
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:27:29
 */
@Configuration
public class GoodsIndexInitListener {

	String queue = "goods-index-init-queue";

	/**
	 * 消息监听
	 * 
	 * @param connectionFactory
	 * @param listenerAdapter
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer listenerContainerGoodsIndex (ConnectionFactory connectionFactory,
			ChannelAwareMessageListener listenerAdapterGoodsIndex) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue); 
		container.setMessageListener(listenerAdapterGoodsIndex);
		return container;
	}

	/**
	 * 消息监听代理
	 * 
	 * @param receiver
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapterGoodsIndex(GoodsIndexInitReceiver receiver) {
		return new MessageListenerAdapter(receiver, "initGoodsIndex");
	}

	@Bean
	Binding bindingGoodsIndexCreate(Queue queueGoodsIndex, FanoutExchange exchangeGoodsIndex) {
		return BindingBuilder.bind(queueGoodsIndex).to(exchangeGoodsIndex);
	}

	@Bean
	Queue queueGoodsIndex() {
		return new Queue(queue, false);
	}

	@Bean
	FanoutExchange exchangeGoodsIndex() {
		return new FanoutExchange(AmqpExchange.INDEX_CREATE.name());
	}
}
