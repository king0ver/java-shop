package com.enation.app.shop.shop.seller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.setting.service.IStoreTemplateManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺模板标签
 * @author xulipeng
 *
 */
@Component
public class StoreTemplateTag extends BaseFreeMarkerTag {
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IStoreTemplateManager storeTemplateManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller member=sellerManager.getSeller();
		List list = this.storeTemplateManager.getTemplateList(member.getStore_id());
		return list;
	}
}
