package com.enation.app.shop.shop.seller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.setting.service.IStoreLogiCompanyManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺物流公司列表
 * @author fenlongli
 *
 */
@Component
public class StoreLogiCompanyListTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IStoreLogiCompanyManager storeLogiCompanyManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		return storeLogiCompanyManager.listByStore();
	}
}
