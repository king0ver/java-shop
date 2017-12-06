package com.enation.app.shop.promotion.exchange.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.exchange.service.IExchangeGoodsManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 积分商品展示tag
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月26日 下午8:49:56
 */
@Component
public class ExchangeGoodsListTag extends BaseFreeMarkerTag {

	@Autowired
	private IExchangeGoodsManager exchangeGoodsManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int pageSize = 15;
		Integer pageNo = request.getParameter("page") == null ? 1
				: Integer.parseInt(request.getParameter("page").toString());
		Page page = exchangeGoodsManager.frontList(pageNo, pageSize, null);
		Map result = new HashMap();
		List<Map> list = (List<Map>) page.getResult();
		result.put("goodsList", list);
		result.put("totalCount", page.getTotalCount());
		result.put("page", pageNo);
		result.put("pageSize", pageSize);
		return result;
	}

}
