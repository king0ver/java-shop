package com.enation.app.shop.orderbill.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.orderbill.model.po.Bill;
import com.enation.app.shop.orderbill.service.IBillManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import freemarker.template.TemplateModelException;

/**
 * 卖家周期账单列表
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月8日 下午5:08:15
 */
@Component
public class SellerBillListTag extends BaseFreeMarkerTag{

	@Autowired
	private IBillManager billManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		Seller seller = sellerManager.getSeller();
		
		if (seller == null||seller.getStore_id()==null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		
		Integer page = this.getPage();
		Integer pageSize = this.getPageSize();
		
		Page<Bill> wegpage = this.billManager.queryMyShopBill(page,pageSize);
		
		Map map=new HashMap();
		
		map.put("page",page);
		map.put("pageSize",pageSize);
		map.put("totalCount", wegpage.getTotalCount());
		map.put("billList", wegpage);
		
		return map;
	}

}
