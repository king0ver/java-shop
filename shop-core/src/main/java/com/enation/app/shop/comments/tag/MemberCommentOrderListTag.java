package com.enation.app.shop.comments.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.comments.service.IMemberCommentManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.component.ComponentView;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * (获取买家待评价订单标签) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年1月5日 上午2:07:52
 */
@Component
public class MemberCommentOrderListTag extends BaseFreeMarkerTag{
	
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IMemberCommentManager memberCommentManager;
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = sellerManager.getSeller();
		if(member==null){
			throw new TemplateModelException("未登陆不能使用此标签[MemberOrderListTag]");
		}
		Map result = new HashMap();
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		int pageSize = 10;
		
		Page ordersPage = memberCommentManager.pageCommentOrders(Integer.valueOf(page), pageSize);

		Long totalCount = ordersPage.getTotalCount();
		
		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("ordersList", ordersPage);
		return result;
	}
	/**
	 * 获取组件列表
	 * @return 组件列表
	 */
	private List<ComponentView> getDbList() {
		String sql = "select * from es_component ";
		return this.daoSupport.queryForList(sql, ComponentView.class);
	}
	
}
