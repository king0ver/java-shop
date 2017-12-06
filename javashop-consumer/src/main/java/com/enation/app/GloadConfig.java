package com.enation.app;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enation.eop.sdk.config.redis.configure.RedisConnectionConfig;


/**
 * 全局配置
 * @author kingapex
 *
 */
@Configuration
public class GloadConfig {
	

	@SuppressWarnings("unchecked")
    protected <T> T createDataSource(DataSourceProperties properties,Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }
	
	@Bean
	public RedisConnectionConfig redisConnectionConfig(JavashopRedisConfig javashopRedisConfig){
		RedisConnectionConfig redisConnectionConfig = new RedisConnectionConfig();
		redisConnectionConfig.setAppId( javashopRedisConfig.getAppId());
		redisConnectionConfig.setHost(javashopRedisConfig.getHost());
		redisConnectionConfig.setMaxIdle(javashopRedisConfig.getMaxIdle() );
		redisConnectionConfig.setMaxTotal( javashopRedisConfig.getMaxTotal());
		redisConnectionConfig.setMaxWaitMillis(javashopRedisConfig.getMaxWaitMillis() );
		redisConnectionConfig.setPassword(javashopRedisConfig.getPassword() );
		redisConnectionConfig.setPort(javashopRedisConfig.getPort() );
		redisConnectionConfig.setTestOnBorrow( javashopRedisConfig.isTestOnBorrow());
		redisConnectionConfig.setType( javashopRedisConfig.getType());
		return redisConnectionConfig;
	}
	
	
	/**
     * @see org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration.Tomcat 仿写的你可以去了解
     * @param properties 读入的配置
     * @return DruidDataSource
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public com.alibaba.druid.pool.DruidDataSource dataSource(DataSourceProperties properties) {
    	
        com.alibaba.druid.pool.DruidDataSource dataSource = createDataSource(properties, com.alibaba.druid.pool.DruidDataSource.class);

        
        DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());

        String validationQuery = databaseDriver.getValidationQuery();
        if (validationQuery != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }

        return dataSource;
    }
 
	
}
