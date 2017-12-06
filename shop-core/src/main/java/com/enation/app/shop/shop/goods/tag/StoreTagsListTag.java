package com.enation.app.shop.shop.goods.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.goods.service.IStoreGoodsTagManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
/**
 * 店铺商品标签列表
 * @author LiFenLong
 *
 */
public class StoreTagsListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreGoodsTagManager storeGoodsTagManager;
	@Autowired
	private ISellerManager SellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List list=new ArrayList();
		if(params.get("type")==null){
			Seller member=SellerManager.getSeller();
			 list=storeGoodsTagManager.list(member.getStore_id());
		}else{
			 list=storeGoodsTagManager.list(Integer.parseInt(params.get("store_id").toString()));
		}
		return list;
	}
}
