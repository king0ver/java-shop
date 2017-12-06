package com.enation.app.shop.shop.seller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.model.StoreThemes;
import com.enation.app.shop.shop.rdesign.service.IStoreThemesManager;
import com.enation.app.shop.shop.setting.model.po.StoreSetting;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺模板列表标签
 * @author Kanon
 *
 */
@Component
public class StoreThemesInfoTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreThemesManager storeThemesManager;

	@Autowired
	private IShopManager shopManager;

	@Autowired
	private ISellerManager sellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		//!!!!!!!!!这里缺少 获取当前店铺信息的server
		Map map=new HashMap();
		ShopDetail storeDetail = shopManager.getShopDetailByMember(sellerManager.getSeller().getMember_id());
		//获取模板目录
		String previewBasePath = "/themes/"+EopSite.getInstance().getThemepath()+StoreSetting.getStore_themes();
		//当前客户选择模板
		StoreThemes storeThemes=storeThemesManager.getStorethThemes(storeDetail.getShop_themeid());

		//店铺选择模板图片
		map.put("previewpath", previewBasePath);

		//店铺选择模板图片
		map.put("storeThemes", storeThemes);

		//店铺模板列表
		map.put("storeThemesList", storeThemesManager.list(this.getPage(), this.getPageSize()));

		return map;
	}
}
