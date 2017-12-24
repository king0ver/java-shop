package com.enation.app.shop.aftersale.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.aftersale.model.vo.RefundQueryParam;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import freemarker.template.TemplateModelException;

/**
 * 退货列表、或退款单列表
 * @author fk
 * @version v6.4 
 * 2017年8月3日17:09:14
 */
@Component
public class StoreRefundTag extends BaseFreeMarkerTag{

	@Autowired
	private IAfterSaleManager afterSaleManager;
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		Seller Seller = sellerManager.getSeller();

		Map map=new HashMap();
		
		Integer page = this.getPage();
		Integer pageSize = this.getPageSize();
		RefundQueryParam queryParam  = new RefundQueryParam();
		String refuseType = request.getParameter("type");
		if(refuseType!=null){
			queryParam.setRefuse_type(refuseType);
		}
		if (Seller == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		//申请时间开始
		String start_time = request.getParameter("start_time")!=null?request.getParameter("start_time").toString():null;
		if(start_time!=null){
			map.put("start_time",DateUtil.getDateline(start_time+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			queryParam.setStart_time(start_time);
		}
		
		//申请时间结束
		String end_time = request.getParameter("end_time")!=null?request.getParameter("end_time").toString():null;
		if(end_time!=null){
			map.put("end_time",DateUtil.getDateline(end_time+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
			queryParam.setEnd_time(end_time);
		}
		//订单号
		String ordersn = request.getParameter("order_sn")!=null?request.getParameter("order_sn").toString():null;
		if(ordersn!=null){
			map.put("order_sn",ordersn);
			queryParam.setOrder_sn(ordersn);
		}
		//退款单好
		String tradeno=request.getParameter("tradeno")!=null?request.getParameter("tradeno").toString():null;
		if(tradeno!=null){
			map.put("tradeno",tradeno);
			queryParam.setSn(tradeno);
		}
		//状态
		String tradestatus=request.getParameter("tradestatus")!=null?request.getParameter("tradestatus").toString():null;
		if(tradestatus!=null){
			map.put("tradestatus",tradestatus);
			queryParam.setRefund_status(tradestatus);
		}
		
		
		queryParam.setPage_no(page);
		queryParam.setPage_size(pageSize);
		queryParam.setSeller_id(Seller.getStore_id());
		Page wegpage = this.afterSaleManager.query(queryParam);

		map.put("page",page);
		map.put("pageSize",pageSize);
		map.put("totalCount", wegpage.getTotalCount());
		map.put("refundList", wegpage);
		
		return map;
	}

}
