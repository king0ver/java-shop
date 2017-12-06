package com.enation.app.shop.goodssearch.service;

import java.util.List;
import java.util.Map;

import org.apache.lucene.facet.FacetResult;

/**
 * 搜索选择器接口
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月15日 下午4:53:23
 */
public interface ISearchSelectorCreator {
	
	
	/**
	 * 生成选择器并压入指定的map
	 * @param map
	 */
	public void createAndPut(Map<String,Object>  map,List<FacetResult> results);
}
