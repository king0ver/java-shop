package com.enation.app.shop.promotion.fulldiscount.tag;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * (这里用一句话描述这个类的作用) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 上午11:58:50
 */
@Component
public class FullDiscountConflictListTag  extends BaseFreeMarkerTag{
	@Autowired
	IFullDiscountManager fullDiscountManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int pageSize = 5;
		Integer pageNo = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page").toString());
		
		List actList = (List) ThreadContextHolder.getSession().getAttribute("actList");
		Integer size = actList.size();
		Integer fromIndex = pageSize*(pageNo-1);
		Integer toIndex = size<5+pageSize*(pageNo-1)?size:5+pageSize*(pageNo-1);
		List subList = actList.subList(fromIndex, toIndex);
		
		Map result = new HashMap();
		
		result.put("goodsList",subList);
		result.put("totalCount", size);
		result.put("page", pageNo);
		result.put("pageSize", pageSize);
		return result;
	}
}
