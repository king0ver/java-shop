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
import com.enation.app.core.receiver.MemberLoginReceiver;

/**
 * 会员登陆监听
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:04:12
 */
@Configuration
public class MemberLoginListener {

	String queue = "member-login-queue";
	
	 /**
    * 消息监听
    * @param connectionFactory
    * @param listenerAdapter
    * @return
    */
   @Bean
   public SimpleMessageListenerContainer containerMemberLogin(ConnectionFactory connectionFactory,
   		ChannelAwareMessageListener listenerAdapterMemberLogin) {
       SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
       container.setConnectionFactory(connectionFactory);
       container.setQueueNames(queue);
       container.setMessageListener(listenerAdapterMemberLogin);
       return container;  
   }
   
   /**
    * 消息监听代理
    * @param receiver
    * @return
    */
   @Bean
   MessageListenerAdapter listenerAdapterMemberLogin(MemberLoginReceiver receiver){
   	return new MessageListenerAdapter(receiver, "memberLogin");
   }
   
   @Bean 
   Binding bindingMemberLogin(Queue queueMemberLogin, FanoutExchange exchangeMemberLogin) {
       return BindingBuilder.bind(queueMemberLogin).to(exchangeMemberLogin);
   }
   
   @Bean
   Queue queueMemberLogin() {
       return new Queue(queue, false);
   }
   
   @Bean
	FanoutExchange exchangeMemberLogin() {
       return new FanoutExchange(AmqpExchange.MEMEBER_LOGIN.name());
   }
}
