package com.enation.app.shop.trade.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.app.shop.trade.service.ITradePriceManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 交易价格
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月19日13:39:02
 */
@Component
@Scope("prototype")
public class TradePriceTag extends BaseFreeMarkerTag {

	@Autowired
	private ITradePriceManager tradePriceManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		PriceDetail price  = tradePriceManager.getTradePrice();
		return price;
	}

}
