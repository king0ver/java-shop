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
import com.enation.app.core.receiver.GoodsChangeReceiver;

/**
 * 商品变化监听
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月21日 下午4:13:41
 */
@Configuration
public class GoodsChangeListener{
	
	String queue = "goods-change-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerGoodsChangeIndex(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterGoodsChangeIndex) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterGoodsChangeIndex);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterGoodsChangeIndex(GoodsChangeReceiver receiver){
    	return new MessageListenerAdapter(receiver, "goodsChange");
    }
    
    @Bean 
    Binding bindingGoodsChangeIndex(Queue queueGoodsChangeIndex, FanoutExchange exchangeGoodsChangeIndex) {
        return BindingBuilder.bind(queueGoodsChangeIndex).to(exchangeGoodsChangeIndex);
    }
    
    @Bean
    Queue queueGoodsChangeIndex() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeGoodsChangeIndex() {
        return new FanoutExchange(AmqpExchange.GOODS_CHANGE.name());
    }
}
