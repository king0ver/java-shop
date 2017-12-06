package com.enation.app.shop.goodssearch.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * (商品品牌标签)
 * 
 * @author zjp
 * @version v1.0
 * @since v6.2 2017年5月31日 下午6:52:44
 */
@Component
@Scope("prototype")
public class SearchSelectorBrandTag extends BaseFreeMarkerTag {
	@Autowired
	private ICategoryManager goodsCatManager;

	@Autowired
	private IBrandManager brandMSManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {

		Integer cat_id = (Integer) params.get("cat_id");

		Category goodscat = this.goodsCatManager.get(cat_id);
		Map selectorMap = new HashMap();
		this.brandMSManager.createSelectorList(selectorMap, goodscat);

		return selectorMap;

	}

}
