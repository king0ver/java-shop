package com.enation.eop.sdk.config.redis.configure.builders;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.enation.eop.sdk.config.redis.configure.IRedisBuilder;
import com.enation.eop.sdk.config.redis.configure.JedisSetting;
import com.enation.eop.sdk.config.redis.configure.RedisConnectionConfig;
import com.sohu.tv.cachecloud.client.basic.util.StringUtil;

/**
 * 阿里云连接
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月27日 下午4:31:56
 */
@Service
public class RedisSelfBuilder implements IRedisBuilder{

	
	@Override
	public JedisConnectionFactory buildConnectionFactory(String domain_url,Long appId, String client_version) {
		
		JedisConnectionFactory jedis = new JedisConnectionFactory();
		RedisConnectionConfig config = JedisSetting.getConnectionConfig();
		jedis.setHostName(config.getHost());  
        jedis.setPort(config.getPort());  
        if (!StringUtil.isBlank(config.getPassword())) {  
            jedis.setPassword(config.getPassword());  
        }
        // 初始化连接pool  
        jedis.afterPropertiesSet();
        jedis.setPoolConfig(JedisSetting.getPoolConfig());
		
		return jedis;
	}

	@Override
	public Integer getType() {
		
		return 4;
	}

}
