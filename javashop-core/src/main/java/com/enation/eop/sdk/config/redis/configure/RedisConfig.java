package com.enation.eop.sdk.config.redis.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.enation.eop.sdk.config.redis.builder.RedisTemplateBuilder;
import com.enation.eop.sdk.config.redis.builder.StringRedisTemplateBuilder;

@Configuration
public class RedisConfig {
	
	@Autowired
	private RedisTemplateBuilder redisTemplateBuilder;
	
	@Autowired
	private StringRedisTemplateBuilder stringRedisTemplateBuilder;
	
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
    	
    	RedisTemplate<String,Object> redisTemplate = null;
    	
    	redisTemplate = redisTemplateBuilder.build();
    	
    	return redisTemplate;
    }
    
	@Bean
    public StringRedisTemplate stringRedisTemplate() {
		
    	StringRedisTemplate redisTemplate = null;
    	
    	redisTemplate = stringRedisTemplateBuilder.build();
    	
    	return redisTemplate;
    }
    
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
    	 return (JedisConnectionFactory) redisTemplate().getConnectionFactory();
    }
    
}
