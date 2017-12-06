package com.enation.app.shop.trade.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
/**
 * 获取店铺订单<br>
 * 是获取卖家的订单
 * @author LiFenLong
 *
 */
@Component
public class SellerOrderListTag extends BaseFreeMarkerTag{
 
	@Autowired
	private ISellerManager sellerManager;
	
	@Autowired
	private IOrderQueryManager orderQueryManager;
	
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		//session中获取会员信息,判断用户是否登陆
		Seller member=sellerManager.getSeller();
		if(member==null){
			HttpServletResponse response= ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect("login.html");
			} catch (IOException e) {
				throw new UrlNotFoundException();
			}
		}
		
		
		//获取订单列表参数
		int pageSize=10;
		String page = request.getParameter("page")==null?"1": request.getParameter("page");
		int page_no = StringUtil.toInt(page, 1);
		String goods=request.getParameter("goods");
		String order_state = request.getParameter("order_state");
		String keyword=request.getParameter("keyword");
		String buyerName=request.getParameter("buyerName");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
 
		OrderQueryParam queryParam =  new OrderQueryParam();
		queryParam.setMember_id(null);
		queryParam.setSeller_id( member.getStore_id());
		queryParam.setPage_no(page_no);
		queryParam.setPage_size(pageSize);
		queryParam.setStart_time(startTime);
		queryParam.setEnd_time(endTime);
		queryParam.setBuyer_name(buyerName);
		queryParam.setOrder_sn(keyword);
		queryParam.setGoods_name(goods);
		queryParam.setTag(order_state);
		
		Map result=new HashMap();
		result.put("keyword", keyword);
		result.put("order_state", order_state);
		result.put("buyerName", buyerName);
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		result.put("goods", goods);
		
		Page orderList=orderQueryManager.query(queryParam);
		//获取总记录数
		Long totalCount = orderList.getTotalCount();
		
		result.put("page", page_no);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		result.put("storeOrder", orderList);
		return result;
	}
	
}
