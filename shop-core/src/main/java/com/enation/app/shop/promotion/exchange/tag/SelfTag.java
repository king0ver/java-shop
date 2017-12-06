package com.enation.app.shop.promotion.exchange.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.shop.seller.impl.SellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.JsonResultUtil;

import freemarker.template.TemplateModelException;
/**
 * 
 * 此类判断是否为自营 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月31日 上午9:53:21
 */
@Component
@Scope("prototype")
public class SelfTag extends BaseFreeMarkerTag {
@Autowired
private SellerManager sellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		Integer self = ShopApp.self_storeid;
		Seller member = this.sellerManager.getSeller();
		if(member ==null){
			return JsonResultUtil.getErrorJson("未登录，不能使用此api");
		}
		if (self.equals(member.getStore_id())) {
			self = 0;
		}
		return self;
	}

}
