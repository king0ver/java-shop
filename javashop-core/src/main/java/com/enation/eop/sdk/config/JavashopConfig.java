package com.enation.eop.sdk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavashopConfig {
	
	
	@Value("${javashop.url.default_gateway:''}")
	private String default_gateway_url;

	public String getDefault_gateway_url() {
		return default_gateway_url;
	}
	
	@Value("${javashop.url.goodsdetail:''}")
	private String goodsdetail_url;
	
	public String getGoodsdetail_url() {
		return goodsdetail_url;
	}
	
	@Value("${javashop.url.search:''}")
	private String search_url;
	
	public String getSearch_url() {
		return search_url;
	}
	
	@Value("${javashop.url.staticpath:''}")
	private String staticpath_url;
	
	public String getStaticpath_url() {
		return staticpath_url;
	}
	
	
	
	
}
