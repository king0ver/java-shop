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
import com.enation.app.core.receiver.OnlinePayReceiver;

/**
 * 在线支付
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月21日 下午4:13:41
 */
@Configuration
public class OnlinePayListener{
	
	String queue = "online-pay-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerOnlinePay(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterOnlinePay) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterOnlinePay);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterOnlinePay(OnlinePayReceiver receiver){
    	return new MessageListenerAdapter(receiver, "onlinePay");
    }
    
    @Bean 
    Binding bindingOnlinePay(Queue queueOnlinePay, FanoutExchange exchangeOnlinePay) {
        return BindingBuilder.bind(queueOnlinePay).to(exchangeOnlinePay);
    }
    
    @Bean
    Queue queueOnlinePay() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeOnlinePay() {
        return new FanoutExchange(AmqpExchange.ONLINE_PAY.name());
    }
}
