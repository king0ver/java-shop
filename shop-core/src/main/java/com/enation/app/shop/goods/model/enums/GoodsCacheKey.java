package com.enation.app.shop.goods.model.enums;

/**
 * 商品缓存Key
 * @author kingapex
 * @verison 1.0
 * @since 6.4
 * 2017年9月15日上午11:50:22
 */
public enum GoodsCacheKey {
	
	GOODS("商品缓存KEY"),
	SKU("SKU缓存KEY");
	
	private String description;
 
	GoodsCacheKey(String _description){
		  this.description=_description;
		  
	}
	
	
}
