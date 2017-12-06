package com.enation.app.shop.shop.goods.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.goods.model.StoreCat;
import com.enation.app.shop.shop.goods.service.IStoreGoodsCatManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
public class StoreCatTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreGoodsCatManager storeGoodsCatManager;
	@Autowired
	private ISellerManager sellerManager;

	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer catid = (Integer) params.get("catid");
		Map map = new HashMap();
		StoreCat storeCat = new StoreCat();
		try {
			Seller Seller = sellerManager.getSeller();;
			map.put("storeid", Seller.getStore_id());
			map.put("store_catid", catid);
			storeCat = this.storeGoodsCatManager.getStoreCat(map);
			
		} catch (Exception e) {
			this.logger.error("商品分类查询错误", e);
		}
		return storeCat;
	}
}
