package com.enation.eop.sdk.config.redis.configure;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * redis构建接口
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月27日 下午2:05:40
 */
public interface IRedisBuilder {

	/**
	 * 构建连接
	 * @param domain_url
	 * @return
	 */
	public  JedisConnectionFactory buildConnectionFactory(String domain_url,Long appId,String client_version);
	
	/**
	 * 类型
	 * @return
	 */
	public Integer getType();
}
