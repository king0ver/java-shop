package com.enation.app.shop.shop.goods.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.goods.service.IStoreGoodsCatManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 店铺分类列表标签
 * @author xulipeng
 * 2014年10月8日20:24:57
 * 
 */

@Component
public class StoreGoodsCatListTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreGoodsCatManager storeGoodsCatManager;
	@Autowired
	private ISellerManager SellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer storeid = (Integer) params.get("storeid");
		if(storeid==null || storeid==0){
			Seller Seller = SellerManager.getSeller();;
			storeid = Seller.getStore_id();
		}
		Integer type = (Integer) params.get("type");
		Integer catid = (Integer) params.get("catid");
		List list =new ArrayList();
		if(type!=null && type==1){
			list = this.storeGoodsCatManager.getStoreCatList(catid,storeid);
		}else{
			list = this.storeGoodsCatManager.storeCatList(storeid);
		}
		return list;
	}
}
