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
import com.enation.app.core.receiver.ShopChangeReceiver;

/**
 * 店铺变更
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:16:14
 */
@Configuration
public class ShopChangeListener{
	
	String queue = "shop-change-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerShopChange(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterShopChange) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterShopChange);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterShopChange(ShopChangeReceiver receiver){
    	return new MessageListenerAdapter(receiver, "shopChange");
    }
    
    @Bean 
    Binding bindingShopChange(Queue queueShopChange, FanoutExchange exchangeShopChange) {
        return BindingBuilder.bind(queueShopChange).to(exchangeShopChange);
    }
    
    @Bean
    Queue queueShopChange() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeShopChange() {
        return new FanoutExchange(AmqpExchange.SHOP_CHANGE_REGISTER.name());
    }
}
