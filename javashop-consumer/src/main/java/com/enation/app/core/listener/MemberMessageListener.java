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
import com.enation.app.core.receiver.MemberMessageReceiver;

/**
 * 站内消息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:58:51
 */
@Configuration
public class MemberMessageListener{
	
	String queue = "member-message-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerMemberMessage(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterMemberMessage) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterMemberMessage);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterMemberMessage(MemberMessageReceiver receiver){
    	return new MessageListenerAdapter(receiver, "memberMessage");
    }
    
    @Bean 
    Binding bindingMemberMessage(Queue queueMemberMessage, FanoutExchange exchangeMemberMessage) {
        return BindingBuilder.bind(queueMemberMessage).to(exchangeMemberMessage);
    }
    
    @Bean
    Queue queueMemberMessage() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeMemberMessage() {
        return new FanoutExchange(AmqpExchange.MEMBER_MESSAGE.name());
    }
}
