package com.enation.app.shop.shop.seller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.rdesign.service.IStoreSildeManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 幻灯片列表
 * @author LiFenLong
 *
 */
public class StoreSlideListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreSildeManager storeSildeManager;
	@Autowired
	private ISellerManager sellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Integer storeid = (Integer) params.get("storeid");
		if(storeid==null){
			Seller member=sellerManager.getSeller();;
			storeid = member.getStore_id();
		}
		return storeSildeManager.list(storeid);
	}
}
