package com.enation.eop.sdk.config.amqp;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Rabbit配置
 * @author kingapex
 * 2017年8月2日上午11:52:50
 * @version 1.0
 * @since 6.4
 */
@Configuration
public class RabbitConfiguration {
	
	@Autowired
	private AmqpProperties amqpProperties;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		String host =amqpProperties.getHost();
		int port  = amqpProperties.getPort();
		String username = amqpProperties.getUsername();
		String password =amqpProperties.getPassword();
//		System.out.println("host is "+ host);
		CachingConnectionFactory connectionFactory =new CachingConnectionFactory(host,port);
	    connectionFactory.setUsername(username);
	    connectionFactory.setPassword(password);
	    return connectionFactory;
	}
 
	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory()) ;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(connectionFactory());
	}

	
}
