package com.enation.app.shop.goodssearch.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.shop.goodssearch.model.SearchSelector;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;


/**
 * 属性url生成工具
 * @author fk
 *  2017年6月30日18:01:50
 */
public class ParamUrlUtils {
	
	
	/**
	 * 在原有的url基础上根据参数名和值生成新的属性url<br>
	 * 如原url为 search.html?cat=1&prop=p1_1，生成新的url:search.html?cat=1&prop=p1_1@name_value
	 * @param name
	 * @param value
	 * @return
	 */
	public static String createPropUrl(String name,String value){
		Map<String,String> params = ParamsUtils.getReqParams();
		String param = params.get("prop");
		if(!StringUtil.isEmpty(param)){
			param = param + Separator.separator_prop;
		}else{
			param = "";
		}
		param = param + name + Separator.separator_prop_vlaue + value;
		params.put("prop", param);
		return ParamsUtils.paramsToUrlString(params);
	}
	
	/**
	 * 获取已选择的属性维度
	 * @return
	 */
	public static  List<SearchSelector> getPropDimSelected(){
			
		List<SearchSelector> selectorList  = new ArrayList();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String servlet_path = request.getServletPath();
		String prop  = request.getParameter("param");
		String[]  prop_ar = ParamsUtils.getProps();
		for (String p : prop_ar) {
			 String[] onprop_ar=p.split(Separator.separator_prop_vlaue);
			 SearchSelector selector = new SearchSelector();
			 	String name = onprop_ar[0];
			 	String value = onprop_ar[1];
	    		selector.setName(name);
	    		selector.setValue(value);
	    		String url = servlet_path +"?"+createPropUrlWithoutSome(name, value);
	    		selector.setUrl(url);
	    		selectorList.add(selector);
		}
		return selectorList;
	}
	
	/**
	 * 排除某个属性的方式生成属性字串<br>
	 * search.html?cat=1&prop=p1_1@p2_2 传入p2和2则返回search.html?cat=1&prop=p1_1<br>
	 * 用于生成已经选择的selector的url
	 * @param name
	 * @param value
	 * @return
	 */
	private static String createPropUrlWithoutSome(String name,String value){
		Map<String,String> params= ParamsUtils.getReqParams();
		String prop  = params.get("prop");
		if(!StringUtil.isEmpty(prop)){
			prop = prop.replaceAll("("+Separator.separator_prop+"?)"+name+Separator.separator_prop_vlaue+value+"("+Separator.separator_prop+"?)", "");
		}else{
			prop="";
		}
		params.put("prop", prop);
		return ParamsUtils.paramsToUrlString(params);
	}
	
}
