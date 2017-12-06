/**
 * 
 */
package com.enation.app.shop.goodssearch.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.LabelAndValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.goods.model.vo.CategoryVo;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goodssearch.model.SearchSelector;
import com.enation.app.shop.goodssearch.service.ISearchSelectorCreator;
import com.enation.app.shop.goodssearch.util.CatUrlUtils;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

/**
 * 分类选择器生成
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月15日 下午4:54:34
 */
@Service("catSelectorCreator")
public class CatSelectorCreator implements ISearchSelectorCreator {
	
	@Autowired
	private ICategoryManager categoryManager;
	
	/* (non-Javadoc)
	 * @see com.enation.app.shop.component.goodsindex.service.ISearchSelectorCreator#createAndPut(java.util.Map)
	 */
	@Override
	public void createAndPut(Map<String, Object> map,List<FacetResult> results) {
		map.put("cat", new ArrayList());
		List<CategoryVo> allCatList = this.categoryManager.listAllChildren(0);
		for (FacetResult tmp : results) {  
	    	String dim = tmp.dim;//维度
	    	//对分类维度的特殊处理 
	    	if(dim.equals("category")){
	    		if(tmp.labelValues.length>1){
	    			List<SearchSelector> catDim  = createCatSelector(tmp.labelValues);
	    			map.put("cat", catDim);
	    		}
	    		break;
	    	}
		 }
		 List<SearchSelector> selectedCat = CatUrlUtils.getCatDimSelected(allCatList);
		 map.put("selected_cat", selectedCat); //已经选择的分类
	}
	
	
	/**
	 * 生成分类选择器<br>
	 * lucene中索引的是catid，需要由缓存中取出catname，并生成正确的url
	 * @param lvs
	 * @return
	 */
	private List createCatSelector(LabelAndValue[] lvs){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String servlet_path = request.getServletPath();
		List<SearchSelector> selectorList  = new ArrayList();
		//由缓存中加载所有的分类
		List<CategoryVo> allCatList = this.categoryManager.listAllChildren(0);
		
		for (int i = 0; i < lvs.length; i++) {
			
    	
			LabelAndValue labelAndValue = lvs[i];
			String catid= labelAndValue.label;
			String catname ="";
			CategoryVo findcat = CatUrlUtils.findCat(allCatList,StringUtil.toInt(catid,0));
			if(findcat!=null){
				catname = findcat.getName();
			}
			if(StringUtil.isEmpty(catname)){
	 
				continue;
			}
			SearchSelector selector = new SearchSelector();
			selector.setName(catname);
			String url =servlet_path +"?"+ CatUrlUtils.createCatUrl(findcat,false);
			selector.setUrl(url);
			selectorList.add(selector);
		}
		
		return selectorList;
	}
	
	
	
	
	
}
