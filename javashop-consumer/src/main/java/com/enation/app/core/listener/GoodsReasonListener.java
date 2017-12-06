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
import com.enation.app.core.receiver.GoodsReasonReceiver;
/**
 * 
 * 商品附带原因的变化监听
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月20日 下午2:00:54
 */
@Configuration
public class GoodsReasonListener {
	String queue = "goods-reason-queue";
	/**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerGoodsReasonIndex(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterGoodsReasonIndex) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterGoodsReasonIndex);
        return container;  
    }
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterGoodsReasonIndex(GoodsReasonReceiver receiver){
    	return new MessageListenerAdapter(receiver, "goodsReason");
    }
    
    @Bean 
    Binding bindingGoodsReasonIndex(Queue queueGoodsReasonIndex, FanoutExchange exchangeGoodsReasonIndex) {
        return BindingBuilder.bind(queueGoodsReasonIndex).to(exchangeGoodsReasonIndex);
    }
    
    @Bean
    Queue queueGoodsReasonIndex() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeGoodsReasonIndex() {
        return new FanoutExchange(AmqpExchange.GOODS_CHANGE_REASON.name());
    }
    
    
}
