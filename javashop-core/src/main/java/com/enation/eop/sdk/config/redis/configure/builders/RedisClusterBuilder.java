package com.enation.eop.sdk.config.redis.configure.builders;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import com.enation.eop.sdk.config.redis.configure.IRedisBuilder;
import com.enation.eop.sdk.config.redis.configure.JedisSetting;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.tv.cachecloud.client.basic.heartbeat.ClientStatusEnum;
import com.sohu.tv.cachecloud.client.basic.heartbeat.HeartbeatInfo;
import com.sohu.tv.cachecloud.client.basic.util.HttpUtils;

/**
 * 自建云集群
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月27日 下午2:43:03
 */
@Service
public class RedisClusterBuilder implements IRedisBuilder{
	

	private static Logger logger = LoggerFactory.getLogger(RedisClusterBuilder.class);
	
	/**
	 * 集群配置项
	 */
	private RedisClusterConfiguration clusterConfiguration;
	
	@Override
	public JedisConnectionFactory buildConnectionFactory(String domain_url,Long appId,String client_version) {
		
		String redis_cluster_suffix= "/cache/client/redis/cluster/%s.json?clientVersion=";
		String REDIS_CLUSTER_URL = domain_url + redis_cluster_suffix+ client_version;
		
		String url = String.format(REDIS_CLUSTER_URL, String.valueOf(appId));
        String response = HttpUtils.doGet(url);
        ObjectMapper objectMapper = new ObjectMapper();
        HeartbeatInfo heartbeatInfo = null;
        try {
            heartbeatInfo = objectMapper.readValue(response, HeartbeatInfo.class);
        } catch (IOException e) {
            logger.error("remote build error, appId: {}", appId, e);
        }
        if (heartbeatInfo == null) {
        }

        /** 检查客户端版本 **/
        if (heartbeatInfo.getStatus() == ClientStatusEnum.ERROR.getStatus()) {
            throw new IllegalStateException(heartbeatInfo.getMessage());
        } else if (heartbeatInfo.getStatus() == ClientStatusEnum.WARN.getStatus()) {
            logger.warn(heartbeatInfo.getMessage());
        } else {
            logger.info(heartbeatInfo.getMessage());
        }

        Set<String> nodeList = new HashSet<String>();
        //形如 ip1:port1,ip2:port2,ip3:port3
        String nodeInfo = heartbeatInfo.getShardInfo();
        //为了兼容,如果允许直接nodeInfo.split(" ")
        nodeInfo = nodeInfo.replace(" ", ",");
        String[] nodeArray = nodeInfo.split(",");
        for (String node : nodeArray) {
            String[] ipAndPort = node.split(":");
            if (ipAndPort.length < 2) {
                continue;
            }
//            String ip = ipAndPort[0];
//            int port = Integer.parseInt(ipAndPort[1]);
            nodeList.add(node);
        }
        
        clusterConfiguration = new RedisClusterConfiguration(nodeList);
        
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(clusterConfiguration, JedisSetting.getPoolConfig());  
		
        return jedisConnectionFactory;
	}

	@Override
	public Integer getType() {
		
		return 3;
	}

}
