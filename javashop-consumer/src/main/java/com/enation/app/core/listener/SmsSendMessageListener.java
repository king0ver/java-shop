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
import com.enation.app.core.receiver.SmsSendMessageReceiver;

/**
 * 发送手机短信
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午6:03:54
 */
@Configuration
public class SmsSendMessageListener{
	
	String queue = "sms-send-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapterSmsSenddMessage
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerSmsSenddMessage(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterSmsSenddMessage) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterSmsSenddMessage);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterSmsSenddMessage(SmsSendMessageReceiver receiver){
    	return new MessageListenerAdapter(receiver, "sendMessage");
    }
    
    @Bean 
    Binding bindingSmsSenddMessage(Queue queueSmsSenddMessage, FanoutExchange exchangeSmsSenddMessage) {
        return BindingBuilder.bind(queueSmsSenddMessage).to(exchangeSmsSenddMessage);
    }
    
    @Bean
    Queue queueSmsSenddMessage() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeSmsSenddMessage() {
        return new FanoutExchange(AmqpExchange.SMS_SEND_MESSAGE.name());
    }
}
