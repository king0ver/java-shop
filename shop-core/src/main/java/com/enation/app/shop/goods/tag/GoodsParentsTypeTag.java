package com.enation.app.shop.goods.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
public class GoodsParentsTypeTag extends BaseFreeMarkerTag{

	@Autowired
	private ICategoryManager categoryManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List list = categoryManager.list(0, null); 
		return list;
	}

}
