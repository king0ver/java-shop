package com.enation.app.shop.shop.setting.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.setting.service.IStoreLogiCompanyManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 快递公司列表标签
 * @author fenlongli
 *
 */
@Component
public class LogiCompanyListTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreLogiCompanyManager storeLogiCompanyManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		return storeLogiCompanyManager.list();
	}
	
}
