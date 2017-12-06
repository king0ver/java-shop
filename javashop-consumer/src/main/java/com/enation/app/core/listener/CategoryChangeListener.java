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
import com.enation.app.core.receiver.CategoryChangeReceiver;

/**
 * 分类变更
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月21日 下午4:13:41
 */
@Configuration
public class CategoryChangeListener{
	
	String queue = "category-change-queue";
	
	 /**
     * 消息监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer containerCategoryChange(ConnectionFactory connectionFactory,
    		ChannelAwareMessageListener listenerAdapterCategoryChange) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue);
        container.setMessageListener(listenerAdapterCategoryChange);
        return container;  
    }
    
    /**
     * 消息监听代理
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapterCategoryChange(CategoryChangeReceiver receiver){
    	return new MessageListenerAdapter(receiver, "categoryChange");
    }
    
    @Bean 
    Binding bindingCategoryChange(Queue queueCategoryChange, FanoutExchange exchangeCategoryChange) {
        return BindingBuilder.bind(queueCategoryChange).to(exchangeCategoryChange);
    }
    
    @Bean
    Queue queueCategoryChange() {
        return new Queue(queue, false);
    }
    
    @Bean
	FanoutExchange exchangeCategoryChange() {
        return new FanoutExchange(AmqpExchange.GOODS_CATEGORY_CHANGE.name());
    }
}
