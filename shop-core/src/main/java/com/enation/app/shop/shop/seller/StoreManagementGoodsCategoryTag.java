package com.enation.app.shop.shop.seller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取经营类目
 * @author liao
 * @version trunk
 * @since 
 * 2017年5月23日 下午3:12:56
 */
@Component
public class StoreManagementGoodsCategoryTag extends BaseFreeMarkerTag{

	@Autowired
	private IShopManager shopManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		//获取店铺ID
		Integer store_id=(Integer)params.get("store_id");
		
		//获取到此店铺选择经营类目的所有父
		List list = shopManager.getShopManagementGoodsType(store_id);
		return list;
	}

}
