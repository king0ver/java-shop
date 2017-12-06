package com.enation.app.shop.goods;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.framework.test.SpringTestSupport;


public class GoodsQueryManagerTest extends SpringTestSupport {

	@Autowired
	private IGoodsQueryManager goodsQueryManager; 
	
	
	@Test
	public void testAdd() throws Exception {
		String json ="{ xxx }";
		mockMvc.perform(  MockMvcRequestBuilders.get("/xxx.do").contentType(MediaType.APPLICATION_JSON_UTF8).content(json));
	}
	
	/*
	 * 
		
	 */
	
}
