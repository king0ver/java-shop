package com.enation.app.shop.trade.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BuyerApplyRefundTag extends BaseFreeMarkerTag {
	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ordersn = request.getParameter("ordersn");
		String skuid = request.getParameter("skuid");
		OrderDetail order = null;
		if (StringUtil.isEmpty(ordersn)) {
			throw new UrlNotFoundException();
		}
		if (StringUtil.isEmpty(skuid)) {
			throw new UrlNotFoundException();
		}
		order = this.orderQueryManager.getOneBySn(ordersn);

		if (order == null) {
			throw new UrlNotFoundException();
		}
		Map result = new HashMap<>();
		for (Product goods : order.getProductList()) {
			if (goods.getProduct_id() == Integer.valueOf(skuid).intValue()) {
				result.put("product", goods);
				break;
			}
		}
		result.put("order", order);
		return result;
	}

}
