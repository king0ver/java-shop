package com.enation.app.shop.shop.seller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.rdesign.model.WapShopThemes;
import com.enation.app.shop.shop.rdesign.service.IShopWapThemesManager;
import com.enation.app.shop.shop.setting.model.po.StoreSetting;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * (wap店铺模板列表标签) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月25日 下午1:09:12
 */
@Component
public class ShopWapThemesInfoTag extends BaseFreeMarkerTag{
	@Autowired
	private IShopWapThemesManager shopWapThemesManager;
	
	@Autowired
	private IShopManager shopManager;
	
	@Autowired
	private ISellerManager sellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		 
		Map map=new HashMap();
		ShopDetail shopDetail = shopManager.getShopDetailByMember(sellerManager.getSeller().getMember_id());
		//获取模板目录
		String previewBasePath = "/themes/"+EopSite.getInstance().getThemepath()+StoreSetting.getShop_wap_themes();
		//当前客户选择模板
		WapShopThemes wapShopThemes = shopWapThemesManager.getStorethThemes(shopDetail.getWap_themeid());
		
		//店铺选择模板图片
		map.put("previewpath", previewBasePath);
		
		//店铺选择模板图片
		map.put("shopWapThemes", wapShopThemes);
		
		//店铺模板列表
		map.put("shopWapThemesList", shopWapThemesManager.list(this.getPage(), this.getPageSize()));
		
		return map;
	}
}
