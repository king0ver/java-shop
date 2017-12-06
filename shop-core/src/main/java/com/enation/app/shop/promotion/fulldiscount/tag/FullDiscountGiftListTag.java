package com.enation.app.shop.promotion.fulldiscount.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 满优惠活动赠品分页列表
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 下午4:59:17
 */
@Component
public class FullDiscountGiftListTag  extends BaseFreeMarkerTag{
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IFullDiscountGiftManager fullDiscountGiftManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		int pageSize = 10;
		Integer pageNo = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page").toString());
		String keyword = request.getParameter("keyword");
		Seller Seller = this.sellerManager.getSeller();
		
		Map result = new HashMap();
		Page page = this.fullDiscountGiftManager.list(keyword, Seller.getStore_id(), pageNo, pageSize);
		
		result.put("giftList",page);
		result.put("totalCount", page.getTotalCount());
		result.put("page", pageNo);
		result.put("pageSize", pageSize);
		result.put("keyword", keyword);
		return result;
	}

}
