package com.enation.app.shop.aftersale.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.aftersale.model.vo.RefundQueryParam;
import com.enation.app.shop.aftersale.model.vo.RefundVo;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import freemarker.template.TemplateModelException;

/**
 * 退款退货列表tag
 * @author fk
 * @version 6.4
 * 2017年8月3日16:21:24
 */
@Component
public class RefundListTag extends BaseFreeMarkerTag {

	@Autowired
	private IAfterSaleManager afterSaleManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Member member =  UserConext.getCurrentMember();

		if (member == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String page = request.getParameter("page");
		page=page==null?"1":page;
		int pageSize = 5;
		
		RefundQueryParam queryParam = new RefundQueryParam();
		
		queryParam.setPage_no(Integer.valueOf(page));
		queryParam.setPage_size(pageSize);		
		queryParam.setMember_id( member.getMember_id());
		
		Page<RefundVo> webPage = this.afterSaleManager.query(queryParam);
		
		Map result = new HashMap();
		List resultList = (List) webPage.getResult();
		resultList = resultList == null ? new ArrayList() : resultList;
		
		result.put("totalCount", webPage.getTotalCount());
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("resultList", resultList);
		
		return result;
	}

}
