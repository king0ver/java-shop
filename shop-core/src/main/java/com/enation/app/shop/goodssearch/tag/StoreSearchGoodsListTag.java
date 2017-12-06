package com.enation.app.shop.goodssearch.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 
 * wap查询店铺导航列表 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月14日 下午12:01:32
 */
@Component
public class StoreSearchGoodsListTag extends BaseFreeMarkerTag {
	@Autowired
	private IGoodsManager goodsManager;

	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer num = (Integer) params.get("num");
		if (num == null || num == 0) {
			num = this.getPageSize();
		}
		Page webpage = this.goodsManager.shopSearchGoodsList(this.getPage(), num, params);
		Long totalCount = webpage.getTotalCount();
		Map result = new HashMap();
		// map.put(arg0, arg1);
		result.put("page", this.getPage());
		result.put("pageSize", num);
		result.put("totalCount", totalCount);
		result.put("storegoods", webpage);
		return result;
	}

}
