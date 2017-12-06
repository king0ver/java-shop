package com.enation.app.shop.promotion.exchange.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.exchange.service.IExchangeFloorManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 
 * 积分商城前台页面楼层展示标签
 * @author    jianghongyan
 * @version   1.0.0,2016年6月20日
 * @since     v6.1
 */
@Component
@Scope("prototype")
public class ExchangeFloorTag extends BaseFreeMarkerTag{
	@Autowired
	private IExchangeFloorManager exchangeFloorManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
			List exchangeFloorList=exchangeFloorManager.listAll();
			return exchangeFloorList;
	}

}
