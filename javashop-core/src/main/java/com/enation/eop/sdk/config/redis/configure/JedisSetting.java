package com.enation.eop.sdk.config.redis.configure;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisPoolConfig;

public class JedisSetting {
	
	private static JedisPoolConfig poolConfig;
	
	private static RedisConnectionConfig connectionConfig;

	public static void loadPoolConfig(RedisConnectionConfig config) {
		
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(config.getMaxTotal()==null?(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 3):config.getMaxTotal());
		poolConfig.setMaxIdle(config.getMaxIdle()==null?(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2):config.getMaxIdle());
		poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
		poolConfig.setMaxWaitMillis(config.getMaxWaitMillis()==null?1000L:config.getMaxWaitMillis());
		poolConfig.setJmxNamePrefix("javashop-redis-pool");
		poolConfig.setJmxEnabled(true);
		poolConfig.setTestOnBorrow(true);
		
		connectionConfig = config;
		
	}
	
	public static JedisPoolConfig getPoolConfig(){
		
		return poolConfig;
	}

	public static RedisConnectionConfig getConnectionConfig() {
		
		return connectionConfig;
	}
	
}
