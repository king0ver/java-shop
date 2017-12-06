package com.enation.app.cms.data.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.cms.data.model.DataField;
import com.enation.framework.cache.ICache;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description="购物车API")
@RequestMapping("/order-create")
@RestController
public class TestControllor {
	
	@Autowired
	private ICache cache;
	
	@Autowired
    private AmqpTemplate amqpTemplate;
	
	
	@ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")  
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long") 
	@GetMapping("/test")
	public DataField get(){
		
		cache.put("mytest", "abc");
		DataField field = new DataField();
		
		String  value = (String) cache.get("mytest");
		field.setChina_name(value);
	
		
		amqpTemplate.convertAndSend("kingapex-test","kingapex-routingkey", "ordersn000");
		
		System.out.println("消息已经发送");
		
		return field;
	}
	
	
	
	
}
