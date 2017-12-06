package com.enation.app.shop.shop.apply.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 获取会员开店信息
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月4日 上午12:21:33
 */
@Component
public class CheckShopInfoTag extends BaseFreeMarkerTag{
	@Autowired
	IShopManager shopManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = UserConext.getCurrentMember();
		if(member!=null) {
			Shop shop = shopManager.getShopByMember(member.getMember_id());
			if(shop!=null) {
				return shop;
			}
		}
		
		return new Shop();
	}

}
