package com.enation.app.shop.promotion.groupbuy.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 商家中心用的我的团购列表
 * @author kingapex
 *2015-1-8下午6:06:06
 */
@Component 
public class MyGroupBuyListTag extends BaseFreeMarkerTag {

	@Autowired
	IGroupBuyManager groupBuyManager;
	
	@Autowired
	private ISellerManager storeMemberManager;
	@Override
	protected Object exec(Map arg0) throws TemplateModelException {
		Seller storeMember  = storeMemberManager.getSeller();
		if(storeMember==null ) {
			 return null;
		}
		int page = this.getPage();
		int pageSize = this.getPageSize();
		
		HttpServletRequest request =this.getRequest();
		
		Map params = new HashMap();
		params.put("gb_name", request.getParameter("gb_name"));
		params.put("gb_status", request.getParameter("gb_status"));
		Page webpage = groupBuyManager.listByStoreId(page, pageSize, storeMember.getStore_id(), params);
		return webpage;
		
	}

}
