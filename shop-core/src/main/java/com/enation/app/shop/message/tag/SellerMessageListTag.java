package com.enation.app.shop.message.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.message.service.IStoreMessageManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 店铺消息列表标签
 * @author Kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-16
 */
@Component
public class SellerMessageListTag extends BaseFreeMarkerTag{

	@Autowired
	private IStoreMessageManager storeMessageManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.framework.taglib.BaseFreeMarkerTag#exec(java.util.Map)
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String type = (String) params.get("type");
		if(StringUtil.isEmpty(type)){
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			type = request.getParameter("type");
		}
		
		Seller seller = sellerManager.getSeller();
		if(seller==null||sellerManager.getSeller().getStore_id()==null){
			return new UrlNotFoundException();
		}
		
		return storeMessageManager.getStoreMessageList(this.getPage(), this.getPageSize(), seller.getStore_id(), type);
	}

}
