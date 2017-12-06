package com.enation.eop.sdk.config.amqp;


/**
 * AMQP配置文件 
 * @author kingapex
 * 2017年8月2日下午12:39:31
 * @version 1.0
 * @since 6.4
 */
public class AmqpProperties {

	private String host;
	private int port;
	private String username;
	private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	 
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
