package com.enation.app.shop.promotion.groupbuy.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyActiveManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 团购活动列表标签
 * @author fenlongli
 *
 */
@Component
public class GroupBuyActListTag extends BaseFreeMarkerTag{
	
	@Autowired
	private IGroupBuyActiveManager groupBuyActiveManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		return groupBuyActiveManager.listJoinEnable();
	}
}
