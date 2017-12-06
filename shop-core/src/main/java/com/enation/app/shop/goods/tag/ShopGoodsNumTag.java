package com.enation.app.shop.goods.tag;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class ShopGoodsNumTag extends BaseFreeMarkerTag{
	@Autowired
	private IShopManager shopManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer shopid=(Integer) params.get("shopid");
		ShopDetail shopDetail = shopManager.getShopDetail(shopid);
		return shopDetail.getGoods_num();
	}

}
