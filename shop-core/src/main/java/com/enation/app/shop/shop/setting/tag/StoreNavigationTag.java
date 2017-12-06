package com.enation.app.shop.shop.setting.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.rdesign.service.INavigationManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
public class StoreNavigationTag extends BaseFreeMarkerTag {
	@Autowired
	private INavigationManager navigationManager;
	@Autowired
	private ISellerManager sellerManager;
	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer storeid=(Integer) params.get("store_id");
		if(storeid==null){
			Seller storeMember = sellerManager.getSeller();
			storeid = storeMember.getStore_id();
		}
		List list = this.navigationManager.getNavicationList(storeid);
		return list;
	}
}
