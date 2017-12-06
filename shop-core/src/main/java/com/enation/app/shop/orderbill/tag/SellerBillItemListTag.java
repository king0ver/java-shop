package com.enation.app.shop.orderbill.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.app.shop.orderbill.service.IBillItemManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import freemarker.template.TemplateModelException;

/**
 * 某结算单详细列表项
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月11日 上午10:55:48
 */
@Component
public class SellerBillItemListTag extends BaseFreeMarkerTag{

	@Autowired
	private IBillItemManager billItemManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String bill_id = request.getParameter("bill_id");
		Integer item_type = (Integer) params.get("item_type");
		
		Seller seller = sellerManager.getSeller();
		
		if (seller == null||seller.getStore_id()==null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		
		Integer page = this.getPage();
		Integer pageSize = this.getPageSize();
		
		Page<BillItem> wegpage = billItemManager.getItemList(StringUtil.toInt(bill_id, 0),item_type,page,pageSize);
		
		Map map=new HashMap();
		
		map.put("page",page);
		map.put("pageSize",pageSize);
		map.put("totalCount", wegpage.getTotalCount());
		map.put("billItemList", wegpage);
		
		return map;
	}

}
