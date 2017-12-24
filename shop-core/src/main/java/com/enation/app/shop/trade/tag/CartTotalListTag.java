package com.enation.app.shop.trade.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.service.ICartReadManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 读取购物车列表
 * <p>PC端前台购物车页面读取(全部商品)购物车列表，从redis中读取。
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月17日15:37:34
 */
@Component
@Scope("prototype")
public class CartTotalListTag extends BaseFreeMarkerTag {
	
	@Autowired
	private ICartReadManager cartReadManager;
	
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List<CartVo> list = this.cartReadManager.getCartlist();
		return list;
	}

}
