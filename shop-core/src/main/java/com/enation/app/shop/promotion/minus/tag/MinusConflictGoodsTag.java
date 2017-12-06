package com.enation.app.shop.promotion.minus.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取单品立减活动中有冲突的商品列表
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月30日下午4:45:14
 *
 */
@Component
public class MinusConflictGoodsTag extends BaseFreeMarkerTag {

	@Autowired
	private IDaoSupport daoSupport;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.framework.taglib.BaseFreeMarkerTag#exec(java.util.Map)
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		// 获取controller中存储的冲突商品列表
		List<Map<String, Object>> list = (List<Map<String, Object>>) ThreadContextHolder.getSession()
				.getAttribute("minusConflictGoods");

		Integer fromIndex = (this.getPage() - 1) * this.getPageSize();
		Integer toIndex = fromIndex + this.getPageSize();
		if (toIndex > list.size()) {
			toIndex = list.size();
		}

		List conflictGoods = list.subList(fromIndex, toIndex);

		// 填充返回值
		Map result = new HashMap();
		result.put("goodsList", conflictGoods);
		result.put("totalCount", list.size());
		result.put("pageNo", this.getPage());
		result.put("pageSize", this.getPageSize());
		return result;

	}

}
