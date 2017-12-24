package com.enation.app.shop.trade.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.shop.trade.model.vo.*;
import com.enation.app.shop.trade.service.IOrderItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 会员我的订单标签
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月23日 下午5:09:34
 */
@Component
@Scope("prototype")
public class BuyerOrderListTag  extends BaseFreeMarkerTag{

	
	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private IOrderItemManager orderItemQueryManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		
		Member member = UserConext.getCurrentMember();
		
		if(member == null || member.getMember_id()==null){
			throw new TemplateModelException("未登陆不能使用此标签[MemberOrderListTag]");
		}
		
		String page = request.getParameter("page")==null?"1": request.getParameter("page");
		int page_no = StringUtil.toInt(page, 1);
		int pageSize = 10;
		
		String status = request.getParameter("status");
		String keyword = request.getParameter("keyword");
		
		OrderQueryParam queryParam =  new OrderQueryParam();
		queryParam.setPage_no(page_no);
		queryParam.setPage_size(pageSize);
		queryParam.setMember_id( member.getMember_id());
		queryParam.setTag(status);
		queryParam.setGoods_name(keyword);//关键字相当于查询商品名称
		
		Page<OrderLineSeller> orderPage = this.orderQueryManager.query(queryParam);
		List<OrderLineSeller>	orderList= (List<OrderLineSeller>	)orderPage.getResult();

 		for ( OrderLineSeller orderLine :orderList){

 			//货到付款的订单顾客是不能支付的
 			if (PaymentType.cod.value().equals( orderLine.getPayment_type())) {
				OperateAllowable allowable = orderLine.getOperateAllowable();
				allowable.setAllowPay(false);
			}

		}

		Map<String,Object> result = new HashMap<>();
		result.put("keyword", keyword);
		result.put("status", status);
		
		Long totalCount = orderPage.getTotalCount();
		
		result.put("page", page_no);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		result.put("orderList", orderPage);
		
		
		return result;
	}

	
	
}
