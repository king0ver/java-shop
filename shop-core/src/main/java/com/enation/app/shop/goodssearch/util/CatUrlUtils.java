/**
 * 
 */
package com.enation.app.shop.goodssearch.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.shop.goods.model.vo.CategoryVo;
import com.enation.app.shop.goodssearch.model.SearchSelector;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

/**
 * 分类url生成工具
 * @author kingapex
 *2015-4-28
 */
public class CatUrlUtils {
	
	

	/**
	 * 获取已经选择的分类维度
	 * @return
	 */
	public static List<SearchSelector> getCatDimSelected(List<CategoryVo> allCatList){
		List<SearchSelector> selectorList  = new ArrayList();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String servlet_path = request.getServletPath();
		String cat  = request.getParameter("cat");
		if(!StringUtil.isEmpty(cat)){
			String[] cat_ar = cat.split(Separator.separator_prop_vlaue);
			String catstr  ="";
			for (String catid : cat_ar) {
				String catname ="";
				CategoryVo findcat  = findCat(allCatList,StringUtil.toInt(catid,0));
				if(findcat!=null){
					catname = findcat.getName();
				}
				 
				if(StringUtil.isEmpty(catname)){
					continue;
				}
				
				if(!StringUtil.isEmpty(catstr)){
					catstr=catstr+Separator.separator_prop_vlaue;
				}
				catstr=catstr+catid;
				
		 
				SearchSelector selector = new SearchSelector();
				selector.setName(catname);
				String url = servlet_path +"?"+createCatUrl(findcat,true);
				selector.setUrl(url);
				
				selector.setOtherOptions(createBrothersCat(allCatList, findcat));
				selectorList.add(selector);
				
			}
		}
		return  selectorList;
		
	}
	


	/**
	 * 生成此分类的同级别的selector
	 * @param allCatList
	 * @param cat
	 * @return
	 */
	private static List<SearchSelector> createBrothersCat(List<CategoryVo> allCatList,CategoryVo cat ){
		List<SearchSelector> selectorList  = new ArrayList();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String servlet_path = request.getServletPath();
		
		int parentid =  cat.getParent_id();
		List<CategoryVo> children  = new ArrayList();
		if(parentid==0){
			children= allCatList;
		}else{
			CategoryVo parentCat = findCat(allCatList,parentid);
			if(parentCat==null) return selectorList;
			
			if( parentCat.getHasChildren() ){
				 children =parentCat.getChildren();
			}
		}
		
		
			
			for (CategoryVo child : children) {
				SearchSelector selector = new SearchSelector();
				selector.setName(child.getName());
				String url =servlet_path +"?"+ createCatUrl(child,true);
				selector.setUrl(url);
				selectorList.add(selector);
			}
	
		return selectorList;
	}

	
	
	/**
	 * 根据树型结构的分类取出某个分类的名称 
	 * @param allCatList
	 * @param catid
	 * @return
	 */
	public static CategoryVo findCat(List<CategoryVo> allCatList  ,int catid){
		for (CategoryVo cat : allCatList) {
			if(cat.getCategory_id().intValue()==catid){
				return cat;
			}
			
			if(cat.getHasChildren()){
				CategoryVo findCat = findCat( cat.getChildren() ,catid);
				if(findCat!=null){
					return findCat;
				}
				
			}
			
		}
		return null;
	} 
	
	/**
	 * 生成加入某个分类的url<br>
	 * @param catid
	 * @return
	 */
	public static  String createCatUrl(CategoryVo goodscat,boolean only_cat){
		Map<String,String> params=null;
		
		if(only_cat ){
			params=new HashMap();
		}else{
			params=ParamsUtils.getReqParams();
		}
		
		String catpath = goodscat.getCategory_path();
		catpath=catpath.substring(2,catpath.length());
		if(catpath.endsWith("|")){catpath = catpath.substring(0, catpath.length()-1);}
		catpath = catpath.replace('|', Separator.separator_prop_vlaue.charAt(0));
		 
		params.put("cat",catpath);
		
		return ParamsUtils.paramsToUrlString(params);
	}
	

	 
	
}
