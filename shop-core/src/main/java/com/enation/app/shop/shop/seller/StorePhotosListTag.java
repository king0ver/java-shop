package com.enation.app.shop.shop.seller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.service.IStorePhotosManager;
import com.enation.app.shop.shop.setting.model.po.StoreLevel;
import com.enation.app.shop.shop.setting.service.IStoreLevelManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 
 * (相册列表) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年4月10日 上午10:55:13
 */
public class StorePhotosListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStorePhotosManager storePhotosManager;
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IStoreLevelManager storeLevelManager;
	@Autowired
	private IShopManager shopManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		//获取商家信息
		Seller member=sellerManager.getSeller();
		//获取店铺id
		Integer store_id = member.getStore_id();
		//获取店铺信息
		ShopDetail shopDetail = shopManager.getShopDetail(store_id);
		//获取店铺等级信息
		StoreLevel storeLevel = storeLevelManager.getStoreLevel(shopDetail.getShop_level());
		//获取店铺存储空间
		Integer space_capacity = storeLevel.getSpace_capacity();
		Map map = new HashMap();
		//将图片信息存入
		map.put("usedSpace", shopDetail.getStore_space_capacity());
		map.put("totalSpace", storeLevel.getSpace_capacity());
		map.put("storePhotosList", storePhotosManager.list(store_id));
		map.put("store_id", store_id);
		return map;
	}
}
