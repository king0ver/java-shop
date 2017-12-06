package com.enation.app.shop.goods.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 草稿箱商品列表标签 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 下午3:44:25
 */
@Component
public class DraftGoodsListTag extends BaseFreeMarkerTag {
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IDraftGoodsManager draftGoodsManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		// session中获取会员信息,判断用户是否登陆
		Seller member = sellerManager.getSeller();
		if (member == null) {
			HttpServletResponse response = ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect("login.html");
			} catch (IOException e) {
				throw new UrlNotFoundException();
			}
		}
		Map result = new HashMap();
		int pageSize = 10;
		String disable = request.getParameter("disable") == null ? "0" : request.getParameter("disable");
		String page = request.getParameter("page") == null ? "1" : request.getParameter("page");
		String store_cat = request.getParameter("store_cat") == null ? "0" : request.getParameter("store_cat");
		String goodsName = request.getParameter("goodsName");

		result.put("store_id", member.getStore_id());
		result.put("disable", Integer.parseInt(disable));
		result.put("store_cat", store_cat);
		result.put("goodsName", goodsName);
		Page storegoods = draftGoodsManager.draftGoodsList(Integer.parseInt(page), pageSize, result);

		// 获取总记录数
		Long totalCount = storegoods.getTotalCount();

		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		result.put("storegoods", storegoods);
		return result;
	}

}
