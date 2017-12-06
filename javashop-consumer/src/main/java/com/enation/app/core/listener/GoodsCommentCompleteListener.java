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
import com.enation.app.core.receiver.GoodsCommentReceiver;

/**
 * 商品评论完成
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:04:12
 */
@Configuration
public class GoodsCommentCompleteListener {

	String queue = "goods-comment-complete-queue";
	
	 /**
    * 消息监听
    * @param connectionFactory
    * @param listenerAdapter
    * @return
    */
   @Bean
   public SimpleMessageListenerContainer containerGoodsCommentComplete(ConnectionFactory connectionFactory,
   		ChannelAwareMessageListener listenerAdapterGoodsCommentComplete) {
       SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
       container.setConnectionFactory(connectionFactory);
       container.setQueueNames(queue);
       container.setMessageListener(listenerAdapterGoodsCommentComplete);
       return container;  
   }
   
   /**
    * 消息监听代理
    * @param receiver
    * @return
    */
   @Bean
   MessageListenerAdapter listenerAdapterGoodsCommentComplete(GoodsCommentReceiver receiver){
   	return new MessageListenerAdapter(receiver, "commentComplete");
   }
   
   @Bean 
   Binding bindingGoodsCommentComplete(Queue queueGoodsCommentComplete, FanoutExchange exchangeGoodsCommentComplete) {
       return BindingBuilder.bind(queueGoodsCommentComplete).to(exchangeGoodsCommentComplete);
   }
   
   @Bean
   Queue queueGoodsCommentComplete() {
       return new Queue(queue, false);
   }
   
   @Bean
	FanoutExchange exchangeGoodsCommentComplete() {
       return new FanoutExchange(AmqpExchange.GOODS_COMMENT_COMPLETE.name());
   }
}
