package com.enation.app.shop.goodssearch.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.lucene.facet.FacetResult;
import org.springframework.stereotype.Service;

import com.enation.app.shop.goodssearch.service.ISearchSelectorCreator;
import com.enation.app.shop.goodssearch.util.PriceUrlUtils;


/**
 * 价格搜索生成
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月15日 下午5:40:25
 */
@Service("priceSelectorCreator")
public class PriceSelectorCreator implements ISearchSelectorCreator {

	
	/* (non-Javadoc)
	 * @see com.enation.app.shop.component.goodsindex.service.ISearchSelectorCreator#createAndPut(java.util.Map, java.util.List)
	 */
	@Override
	public void createAndPut(Map<String, Object> map, List<FacetResult> results) {
		
		PriceUrlUtils.createAndPut(map);
		
	}
 
	
	
}
