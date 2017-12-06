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
import com.enation.app.core.receiver.MemberRegisterReceiver;

/**
 * 会员注册
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:04:12
 */
@Configuration
public class MemberRegisterListener {

	String queue = "member-register-queue";
	
	 /**
    * 消息监听
    * @param connectionFactory
    * @param listenerAdapter
    * @return
    */
   @Bean
   public SimpleMessageListenerContainer containerMemberRegister(ConnectionFactory connectionFactory,
   		ChannelAwareMessageListener listenerAdapterMemberRegister) {
       SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
       container.setConnectionFactory(connectionFactory);
       container.setQueueNames(queue);
       container.setMessageListener(listenerAdapterMemberRegister);
       return container;  
   }
   
   /**
    * 消息监听代理
    * @param receiver
    * @return
    */
   @Bean
   MessageListenerAdapter listenerAdapterMemberRegister(MemberRegisterReceiver receiver){
   	return new MessageListenerAdapter(receiver, "memberRegister");
   }
   
   @Bean 
   Binding bindingMemberRegister(Queue queueMemberRegister, FanoutExchange exchangeMemberRegister) {
       return BindingBuilder.bind(queueMemberRegister).to(exchangeMemberRegister);
   }
   
   @Bean
   Queue queueMemberRegister() {
       return new Queue(queue, false);
   }
   
   @Bean
	FanoutExchange exchangeMemberRegister() {
       return new FanoutExchange(AmqpExchange.MEMEBER_REGISTER.name());
   }
}
