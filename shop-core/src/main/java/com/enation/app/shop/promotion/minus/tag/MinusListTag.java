package com.enation.app.shop.promotion.minus.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.minus.service.IMinusManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 单品立减列表标签
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月24日下午2:33:01
 *
 */
@Component
public class MinusListTag extends BaseFreeMarkerTag {

	@Autowired
	private IMinusManager minusManager;

	@Autowired
	private ISellerManager sellerManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.framework.taglib.BaseFreeMarkerTag#exec(java.util.Map)
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		HttpServletRequest request = this.getRequest();
		// 获取参数
		String keyword = request.getParameter("keyword");
		Seller seller = this.sellerManager.getSeller();
		Integer shop_id = seller.getStore_id();

		Page page = this.minusManager.listJson(keyword, shop_id, this.getPage(), this.getPageSize());

		// 填充返回值
		Map result = new HashMap();
		result.put("minusList", page);
		result.put("totalCount", page.getTotalCount());
		result.put("pageNo", this.getPage());
		result.put("pageSize", this.getPageSize());
		result.put("keyword", keyword);
		return result;
	}

}
