package com.enation.app.shop.trade.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.support.CheckoutParam;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 读取结算参数
 * <p>PC端前台结算页面读取用户的结算参数，从redis中读取。
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月17日15:37:34
 */
@Component
@Scope("prototype")
public class CheckoutParamTag extends BaseFreeMarkerTag {

	@Autowired
	private ICheckoutParamManager checkoutParamManager;
	
	@Autowired
	private ICartWriteManager cartWriteManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		CheckoutParam checkoutParam = this.checkoutParamManager.getParam();
		
		//设置用户已经进入过结算页面
		ThreadContextHolder.getSession().setAttribute("is_inCheckout", "1");
		//设置配送方式
		this.cartWriteManager.setShipping();
		
		return checkoutParam;
	}

}
