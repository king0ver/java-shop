package com.enation.app.shop.promotion.exchange.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.exchange.service.IExchangeManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 
 * 获取积分兑换商品设置标签 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月27日 下午5:34:07
 */
@Component
@Scope("prototype")
public class ExchangeSettingTag extends BaseFreeMarkerTag {
	@Autowired
	private IExchangeManager exchangeManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Object goodsId=params.get("goods_id");
		if(goodsId==null){
			return null;
		}
		Integer active_id=(Integer) params.get("active_id");
		if(active_id==null){
			return null;
		}
		
		return exchangeManager.getSettingToShow(StringUtil.toInt(goodsId.toString(),true),active_id);
	}
}
