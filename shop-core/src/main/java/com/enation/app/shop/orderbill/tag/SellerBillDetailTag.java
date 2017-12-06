package com.enation.app.shop.orderbill.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.orderbill.model.po.Bill;
import com.enation.app.shop.orderbill.service.IBillManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;
import com.enation.framework.validator.ResourceNotFoundException;

import freemarker.template.TemplateModelException;

/**
 * 某结算单详细
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月11日 上午10:18:22
 */
@Component
public class SellerBillDetailTag extends BaseFreeMarkerTag{

	@Autowired
	private IBillManager billManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		String bill_id = request.getParameter("bill_id");
		
		Seller seller = sellerManager.getSeller();
		
		if (seller == null||seller.getStore_id()==null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		Bill bill = this.billManager.get(StringUtil.toInt(bill_id,0));
		
		if(bill == null || !seller.getStore_id().equals(bill.getSeller_id())){
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "你访问的账单不存在");
		}
		
		return bill;
	}

}
