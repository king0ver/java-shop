package com.enation.eop.sdk.config.redis.configure.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.enation.eop.sdk.config.redis.configure.IRedisBuilder;
import com.enation.eop.sdk.config.redis.configure.JedisSetting;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.tv.cachecloud.client.basic.util.HttpUtils;

/**
 * 自建云普通
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月27日 下午2:37:04
 */
@Service
public class RedisStandaloneBuilder  implements IRedisBuilder{
	

	private static Logger logger = LoggerFactory.getLogger(RedisStandaloneBuilder.class);
	
	@Override
	public JedisConnectionFactory buildConnectionFactory(String domain_url,Long appId,String client_version) {
		
		String redis_standalone_suffix = "/cache/client/redis/standalone/%s.json?clientVersion=";
		
		String REDIS_STANDALONE_URL = domain_url + redis_standalone_suffix+ client_version;
		 /**
         * 心跳返回的请求为空；
         */
        String response = HttpUtils.doGet(String.format(REDIS_STANDALONE_URL, appId));
        if (response == null || response.isEmpty()) {
            logger.warn("cannot get response from server, appId={}. continue...", appId);
        }

        /**
         * 心跳返回的请求无效；
         */
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = null;
        try {
            responseJson = mapper.readTree(response);
        } catch (Exception e) {
            logger.error("read json from response error, appId: {}.", appId, e);
        }

        if (responseJson == null) {
            logger.warn("invalid response, appId: {}. continue...", appId);
        }

        /**
         * 从心跳中提取HostAndPort，构造JedisPool实例；
         */
        String instance = responseJson.get("standalone").asText();
        String[] instanceArr = instance.split(":");
        if (instanceArr.length != 2) {
            logger.warn("instance info is invalid, instance: {}, appId: {}, continue...", instance, appId);
        }
        
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();  
        jedisConnectionFactory.setHostName(instanceArr[0]);  
        jedisConnectionFactory.setPort(Integer.valueOf(instanceArr[1]));  
        jedisConnectionFactory.setPoolConfig(JedisSetting.getPoolConfig());  
        
		return jedisConnectionFactory;
	}

	@Override
	public Integer getType() {
		
		return 1;
	}

}
