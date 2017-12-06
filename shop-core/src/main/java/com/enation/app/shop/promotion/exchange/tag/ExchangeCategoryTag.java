package com.enation.app.shop.promotion.exchange.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.exchange.service.IExchangeCategoryManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 积分商品分类标签
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月26日 下午9:02:33
 */
@Component
public class ExchangeCategoryTag extends BaseFreeMarkerTag {
	@Autowired
	private IExchangeCategoryManager exchangeCatManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List cat_tree = exchangeCatManager.getListChildren(0);
		return cat_tree;
	}
}
