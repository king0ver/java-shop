/**
 * 
 */
package com.enation.app.shop.goodssearch.service;

import java.util.Map;

/**
 * 商品索引管理接口
 * @author kingapex
 *2015-4-16
 */
public interface IGoodsIndexManager {
	
	/**
	 * 将某个商品加入索引<br>
	 * @param goods
	 */
	public void addIndex(Map goods);
	
	/**
	 * 更新某个商品的索引
	 * @param goods
	 */
	public void updateIndex(Map goods);
	
	
	/**
	 * 更新
	 * @param goods
	 */
	public void deleteIndex(Map goods);
	
	
}
