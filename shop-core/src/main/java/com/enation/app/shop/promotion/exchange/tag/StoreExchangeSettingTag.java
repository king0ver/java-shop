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
 * 商家中心积分换购具体信息标签
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月25日 下午2:15:58
 */
@Component
@Scope("prototype")
public class StoreExchangeSettingTag extends BaseFreeMarkerTag{

	@Autowired
	private IExchangeManager exchangeManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Object goodsId=params.get("goods_id");
		if(goodsId==null){
			return null;
		}
		return exchangeManager.getSetting(StringUtil.toInt(goodsId.toString(),true));
	}

}
