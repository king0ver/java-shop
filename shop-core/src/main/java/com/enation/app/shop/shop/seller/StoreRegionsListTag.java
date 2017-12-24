package com.enation.app.shop.shop.seller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.setting.service.IStoreRegionsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺地区标签
 * @author xulipeng
 * 2015年1月12日14:28:25
 */

@Component
public class StoreRegionsListTag extends BaseFreeMarkerTag {
	@Autowired
	private IStoreRegionsManager storeRegionsManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List list = this.storeRegionsManager.getRegionsToAreaList();
		return list;
	}
}
