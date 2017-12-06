package com.enation.javashop.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enation.framework.cache.ICache;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
	
	@Autowired
	private ICache cache;
	
	
	@Test
	public void test(){
//		cache.put("abc", "1111");

		String  value = (String) cache.get("mytest");
		System.out.println("value is "+ value);
	}
}
