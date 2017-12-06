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
import com.enation.app.core.receiver.SendEmailReceiver;

/**
 * 邮件发送
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午6:29:18
 */
@Configuration
public class EmailSendMessageListener{
	
	String queue = "email-send-message-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerSendEmail(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterSendEmail) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterSendEmail);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterSendEmail(SendEmailReceiver receiver){
    	return new MessageListenerAdapter(receiver, "sendEmail");
    }
    
    @Bean 
    Binding bindingSendEmail(Queue queueSendEmail, FanoutExchange exchangeSendEmail) {
        return BindingBuilder.bind(queueSendEmail).to(exchangeSendEmail);
    }
    
    @Bean
    Queue queueSendEmail() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeSendEmail() {
        return new FanoutExchange(AmqpExchange.EMAIL_SEND_MESSAGE.name());
    }
}
