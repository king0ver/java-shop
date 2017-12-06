package com.enation.app.shop.promotion.exchange.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.exchange.service.IExchangeCategoryManager;
import com.enation.app.shop.promotion.exchange.model.vo.ExchangeGoodsCategoryVo;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;

/**
 * 
 * 积分商品分类列表标签
 * 
 * @author jianghongyan
 * @version 1.0.0,2016年6月20日
 * @since v6.1
 */
@Component
@Scope("prototype")
public class ExchangeCatListTag extends BaseFreeMarkerTag {

	@Autowired
	private IExchangeCategoryManager ExchangeCatManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer catid = (Integer) params.get("catid");
		if (catid == null) {
			catid = 0;
		}
		List<ExchangeGoodsCategoryVo> catList = ExchangeCatManager.listAllChildren(catid);
		return catList;
	}

}
