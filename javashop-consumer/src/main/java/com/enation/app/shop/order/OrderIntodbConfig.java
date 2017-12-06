package com.enation.app.shop.order;

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

@Configuration
public class OrderIntodbConfig {
	
	private String queueName ="order-create-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer listenerContainerOrderIntodb(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterOrderIntodb) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapterOrderIntodb);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterOrderIntodb(OrderConsumer receiver){
    	return new MessageListenerAdapter(receiver, "receiveQueue");
    }
    
    @Bean 
    Binding bindingOrderIntodb(Queue queueOrderIntodb, FanoutExchange exchangeOrderIntodb) {
        return BindingBuilder.bind(queueOrderIntodb).to(exchangeOrderIntodb);
    }
    
    @Bean
    Queue queueOrderIntodb() {
        return new Queue(queueName, false);
    }
    
    @Bean
	FanoutExchange exchangeOrderIntodb() {
        return new FanoutExchange(AmqpExchange.ORDER_CREATE.name());
    }
	

}
