package com.enation.app.shop.shop.seller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.receipt.service.IReceiptContentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 通过店铺ID，获得该店铺 下的商品个数
 * @author wanghongjun
 * 2015-04-20
 */

@Component
@Scope("prototype")
public class StoreHistoryReceiptTag extends BaseFreeMarkerTag{

	
	@Autowired
	private IReceiptContentManager receiptContentManager;
	@Autowired
	private ISellerManager storeMemberManager;

	
	@Override
	public Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		int pageSize = 10;
		Seller storeMember = storeMemberManager.getSeller();
		/**获取历史发票*/
		Page ordersPage = this.receiptContentManager.getCurrentHistoryReceipt(Integer.valueOf(page), pageSize,storeMember.getStore_id());
		Long totalCount = ordersPage.getTotalCount();
		/**将发票信息存入*/
		Map result = new HashMap();
		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("ordersList", ordersPage);
		return result;
	}

	
	
}
