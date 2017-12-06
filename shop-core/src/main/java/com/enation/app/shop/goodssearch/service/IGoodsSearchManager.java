package com.enation.app.shop.goodssearch.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.goodssearch.model.GoodsWords;
import com.enation.framework.database.Page;

/**
 * 商品搜索
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月14日 上午10:52:20
 */
public interface IGoodsSearchManager {
	
	/**
	 * 搜索
	 * @param pageNo 分页
	 * @param pageSize 每页显示数量
	 * @return 商品分页
	 */
	public Page search(int pageNo,int pageSize) ;
	
	/**
	 * 获取筛选器

	 * @return Map
	 */
	public Map<String,Object> getSelector();
	
	/**
	 * 通过关键字获取商品分词索引
	 * @param keyword
	 * @return
	 */
	public List<GoodsWords> getGoodsWords(String keyword);
	
	  
	
}
