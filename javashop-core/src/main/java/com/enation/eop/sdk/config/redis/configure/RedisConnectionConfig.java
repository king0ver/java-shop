package com.enation.eop.sdk.config.redis.configure;

//@Configuration
//@PropertySource("classpath:redis.properties")
//@Component
public class RedisConnectionConfig {
//
//	@Bean 
//	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() { 
//		System.out.println("走了..");
//		return new PropertySourcesPlaceholderConfigurer(); 
//	} 
//	
	
   // @Value("${redis.appId}")  
	private Long appId;
    
    //@Value("${redis.type}")
	private  Integer type;
    
    //@Value("${redis.host}")
	private String host;
    
   // @Value("${redis.port}")
    private  Integer port;
    
    //@Value("${redis.password}")
    private String password;
    
   // @Value("${redis.maxIdle}")
    private  Integer maxIdle;
    
    //@Value("${redis.maxTotal}")
    private  Integer maxTotal;
    
   // @Value("${redis.maxWaitMillis}")
    private Long maxWaitMillis;
    
    //@Value("${redis.testOnBorrow}")
    private  boolean testOnBorrow;
    
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Long getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(Long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
    
    
}
