package com.enation.app.shop.trade.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 购物车消息提示
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月19日13:49:26
 */
@Component
@Scope("prototype")
public class CartMessageTag extends BaseFreeMarkerTag {

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map map = new HashMap();
		map.put("cart_message", "购物车由于商品价格或活动信息修改，导致有细微调整，带来的不便请谅解。");
		return map;
	}

}
