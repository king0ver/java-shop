package com.enation.app.shop.aftersale.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.aftersale.model.vo.RefundDetail;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 退货、或退款单详细
 * @author zjp
 * @version v6.4 
 * 2017年8月10日17:09:14
 */
@Component
public class StoreRefundDetailTag extends BaseFreeMarkerTag{

	@Autowired
	private IAfterSaleManager afterSaleManager;
	@Autowired
	private IOrderQueryManager orderQueryManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sn = request.getParameter("sn");

		Map map = new HashMap();
		
		RefundDetail refundDetail = this.afterSaleManager.getDetail(sn);
		OrderDetail orderDetail = this.orderQueryManager.getOneBySn(refundDetail.getRefund().getOrder_sn());
		map.put("refundDetail", refundDetail);
		map.put("orderDetail", orderDetail);
		
		return map;
	}

}
