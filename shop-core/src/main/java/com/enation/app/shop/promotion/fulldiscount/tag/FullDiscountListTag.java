package com.enation.app.shop.promotion.fulldiscount.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import freemarker.template.TemplateModelException;

/**
 * 
 * 满优惠促销列表标签
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月4日 下午6:01:06
 */
@Component
public class FullDiscountListTag extends BaseFreeMarkerTag{
	@Autowired
	private IFullDiscountManager fullDiscountManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int pageSize = 10;
		Integer pageNo = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page").toString());
		String keyword = request.getParameter("keyword");
		/** 获取当前登陆的店铺ID */
		Seller seller = sellerManager.getSeller();
		
		Map result = new HashMap();
		Page page = this.fullDiscountManager.getFullDiscountList(keyword, seller.getStore_id(), pageNo, pageSize);
		List<Map> list = (List<Map>) page.getResult();
		for (Map map : list) {
			if(Long.valueOf((map.get("end_time").toString()))<DateUtil.getDateline()) {
				map.put("is_end", 1);
			}
		}
		result.put("activityList",page);
		result.put("totalCount", page.getTotalCount());
		result.put("page", pageNo);
		result.put("pageSize", pageSize);
		result.put("keyword", keyword);
		return result;
	}
	
}
