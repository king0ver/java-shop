package com.enation.eop.sdk.config.redis.configure.builders;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.enation.eop.sdk.config.redis.configure.IRedisBuilder;
import com.enation.eop.sdk.config.redis.configure.JedisSetting;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.tv.cachecloud.client.basic.heartbeat.ClientStatusEnum;
import com.sohu.tv.cachecloud.client.basic.util.HttpUtils;

/**
 * 自建云哨兵
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月27日 下午2:25:52
 */
@Service
public class RedisSentinelBuilder  implements IRedisBuilder{


	private static Logger logger = LoggerFactory.getLogger(RedisSentinelBuilder.class);
	
	/**
	 * 哨兵配置项
	 */
	private RedisSentinelConfiguration sentinelConfiguration;
	
	@Override
	public JedisConnectionFactory buildConnectionFactory(String domain_url,Long appId,String client_version) {
		
		String redis_sentinel_suffix= "/cache/client/redis/sentinel/%s.json?clientVersion=";
		
		String REDIS_SENTINEL_URL = domain_url +redis_sentinel_suffix + client_version;
		
		String response = HttpUtils.doGet(String.format(REDIS_SENTINEL_URL, appId));
        if (response == null || response.isEmpty()) {
            logger.warn("get response from remote server error, appId: {}, continue...", appId);
        }

        /**
         * http请求返回的结果是无效的；
         */
        ObjectMapper mapper = new ObjectMapper();
        JsonNode heartbeatInfo = null;
        try {
            heartbeatInfo = mapper.readTree(response);
        } catch (Exception e) {
//            logger.error("heartbeat error, appId: {}. continue...", appId, e);
        }
        if (heartbeatInfo == null) {
            logger.error("get sentinel info for appId: {} error. continue...", appId);
        }

        /** 检查客户端版本 **/
        if (heartbeatInfo.get("status").intValue() == ClientStatusEnum.ERROR.getStatus()) {
            throw new IllegalStateException(heartbeatInfo.get("message").textValue());
        } else if (heartbeatInfo.get("status").intValue() == ClientStatusEnum.WARN.getStatus()) {
            logger.warn(heartbeatInfo.get("message").textValue());
        } else {
            logger.info(heartbeatInfo.get("message").textValue());
        }

        /**
         * 有效的请求：取出masterName和sentinels，并创建JedisSentinelPool的实例；
         */
        String masterName = heartbeatInfo.get("masterName").asText();
        String sentinels = heartbeatInfo.get("sentinels").asText();
        Set<String> sentinelSet = new HashSet<String>();
        for (String sentinelStr : sentinels.split(" ")) {
            String[] sentinelArr = sentinelStr.split(":");
            if (sentinelArr.length == 2) {
                sentinelSet.add(sentinelStr);
            }
        }
        
        sentinelConfiguration = new RedisSentinelConfiguration(masterName, sentinelSet);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfiguration, JedisSetting.getPoolConfig());  
        
		return jedisConnectionFactory;
	}

	@Override
	public Integer getType() {
		
		return 2;
	}

}
