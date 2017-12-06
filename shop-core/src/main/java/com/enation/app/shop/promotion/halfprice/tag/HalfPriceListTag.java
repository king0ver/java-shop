package com.enation.app.shop.promotion.halfprice.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
public class HalfPriceListTag extends BaseFreeMarkerTag{

	@Autowired
	private IHalfPriceManager halfPriceManager;
	@Autowired
	private ISellerManager storeMemberManager;
	

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int pageSize = 10;
		Integer pageNo = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page").toString());
		String keyword = request.getParameter("keyword");
		/** 获取当前登陆的店铺ID */
		Seller storeMember = storeMemberManager.getSeller();
		
		Map result = new HashMap();
		Page page = this.halfPriceManager.getHalfPriceList(keyword, storeMember.getStore_id(), pageNo, pageSize);
		
		result.put("activityList",page);
		result.put("totalCount", page.getTotalCount());
		result.put("page", pageNo);
		result.put("pageSize", pageSize);
		result.put("keyword", keyword);
		return result;
	}
}
