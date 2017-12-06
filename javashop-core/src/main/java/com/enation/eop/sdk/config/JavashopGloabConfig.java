package com.enation.eop.sdk.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.enation.framework.cache.ICache;
import com.enation.framework.cache.RedisCacheImpl;
import com.enation.framework.component.ComponentLoader;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.ISqlFileExecutor;
import com.enation.framework.database.impl.DefaultSqlFileExecutor;
import com.enation.framework.database.impl.JdbcDaoSupport;
import com.enation.framework.database.impl.LowerCaseJdbcTemplate;

/**
 * 全局bean配置
 * @author kingapex
 * @verison 1.0
 * @since 6.4
 * 2017年9月1日下午6:19:50
 */
@Configuration
public class JavashopGloabConfig {
	

	
	/**
	 * 声明jdbcTemplate
	 * @param dataSource 数据源（已经在xml中声明）
	 * @return LowerCaseJdbcTemplate（继承自JdbcTemplate,做了一些大小写的处理）
	 */
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource){
		JdbcTemplate jdbcTemplate = new  LowerCaseJdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}
	
	
	/**
	 * daoSupport的声明
	 * @param jdbcTemplate jdbcTemplate已经在上面声明
	 * @return JdbcDaoSupport
	 */
	@Bean
	public IDaoSupport daoSupport(JdbcTemplate jdbcTemplate ){
		IDaoSupport daoSupport  = new JdbcDaoSupport(jdbcTemplate);
		return daoSupport;
	}
	
	
	/**
	 * sql文件执行器
	 * @return
	 */
	public  ISqlFileExecutor sqlFileExecutor() {
		
		return new  DefaultSqlFileExecutor();
	}
	
	
	
	/**
	 *  声明 邮件发送 sender
	 *  使用Spring封装的Mail Sender 
	 */
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender =  new  JavaMailSenderImpl();
		Properties javaMailProperties =  new Properties();
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.timeout", "25000");
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
	
	
	/**
	 * 声明 SpringContextHolder 
	 * @return
	 */
	@Bean
	@Lazy(false)
	public SpringContextHolder springContextHolder() {
		
		return new SpringContextHolder();
	}
	
	
	
	
	//@Bean 
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() { 
		return new PropertySourcesPlaceholderConfigurer(); 
	} 
	
	
	
	
	@Bean
	ICache cache(RedisTemplate redisTemplate){
		
		return new RedisCacheImpl(redisTemplate);
	}
	
}
