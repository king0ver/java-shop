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
import com.enation.app.core.receiver.MemberInfoCompleteReceiver;

/**
 * 会员完善个人信息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:04:12
 */
@Configuration
public class MemberInfoCompleteListener {

	String queue = "member-info-complete-queue";
	
	 /**
    * 消息监听
    * @param connectionFactory
    * @param listenerAdapter
    * @return
    */
   @Bean
   public SimpleMessageListenerContainer containerMemberInfoComplete(ConnectionFactory connectionFactory,
   		ChannelAwareMessageListener listenerAdapterMemberInfoComplete) {
       SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
       container.setConnectionFactory(connectionFactory);
       container.setQueueNames(queue);
       container.setMessageListener(listenerAdapterMemberInfoComplete);
       return container;  
   }
   
   /**
    * 消息监听代理
    * @param receiver
    * @return
    */
   @Bean
   MessageListenerAdapter listenerAdapterMemberInfoComplete(MemberInfoCompleteReceiver receiver){
   	return new MessageListenerAdapter(receiver, "memberInfoComplete");
   }
   
   @Bean 
   Binding bindingMemberInfoComplete(Queue queueMemberInfoComplete, FanoutExchange exchangeMemberInfoComplete) {
       return BindingBuilder.bind(queueMemberInfoComplete).to(exchangeMemberInfoComplete);
   }
   
   @Bean
   Queue queueMemberInfoComplete() {
       return new Queue(queue, false);
   }
   
   @Bean
	FanoutExchange exchangeMemberInfoComplete() {
       return new FanoutExchange(AmqpExchange.MEMBER_INFO_COMPLETE.name());
   }
}
