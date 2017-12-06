package com.enation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;


/**
 * Javashop消费者工程
 * @author kingapex
 * @verison 1.0
 * @since 6.4
 * 2017年9月1日下午7:31:49
 */
@SpringBootApplication
//@ComponentScan({"com.enation.javashop.consumer","com.enation.eop.sdk.config.redis","com.enation.app.cms.pagecreate"})
@ComponentScan(  excludeFilters = {  @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.enation.eop.sdk.config.amqp.*") ,
		@ComponentScan.Filter(type = FilterType.ANNOTATION,  classes ={Controller.class, RestController.class} )} )
public class JavashopConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavashopConsumerApplication.class, args);
	}
}
