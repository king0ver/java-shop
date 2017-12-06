package com.enation.app.shop.shop.setting.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.rdesign.model.Navigation;
import com.enation.app.shop.shop.rdesign.service.INavigationManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 
 * 获取导航详细信息 
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年10月31日 下午10:14:54
 */
@Component
public class NavigationTag extends BaseFreeMarkerTag {

	@Autowired
	private INavigationManager navigationManager;

	@Autowired
	private ISellerManager sellerManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer nav_id = (Integer) params.get("nav_id");
		Navigation navigation =this.navigationManager.getNavication(nav_id);
		//增加权限
		Seller seller = sellerManager.getSeller();
		if(navigation==null || !navigation.getStore_id().equals(seller.getStore_id())){
			throw new UrlNotFoundException();
		}

		return navigation;
	}
}
