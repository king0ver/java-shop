package com.enation.app.shop.goods.tag;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺商品列表
 * @author xulipeng
 *2015年1月7日16:09:40
 */

@Component
public class ShopGoodsListTag extends BaseFreeMarkerTag {
	@Autowired
	private IGoodsManager goodsManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Page page = this.goodsManager.shopGoodsList(this.getPage(), 5, params);
		List list = (List) page.getResult();
		return list;
	}
	
}
