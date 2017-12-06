package com.enation.app.shop.shop.seller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 店铺列表标签
 * @author LiFenLong
 *
 */
public class StoreSearchTag extends BaseFreeMarkerTag{
	@Autowired
	private IShopManager shopManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Map map=new HashMap();
		map.put("shop_name",request.getParameter("keyword"));
		map.put("store_credit", request.getParameter("store_credit"));
		map.put("searchType", request.getParameter("searchType"));
		String pageNo = request.getParameter("page");
		int pageSize=10;
		pageNo = (pageNo == null || pageNo.equals("")) ? "1" : pageNo;
		Page page=shopManager.shop_list(map,"open", Integer.parseInt(pageNo), 10);
		map.put("list",page );
		map.put("totalCount", page.getTotalCount());
		map.put("pageSize", pageSize);
		map.put("page", pageNo);
		return map; 
	}
}
