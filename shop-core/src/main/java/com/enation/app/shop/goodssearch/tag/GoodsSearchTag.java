package com.enation.app.shop.goodssearch.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goodssearch.service.IGoodsSearchManager;
import com.enation.app.shop.goodssearch.service.SearchEngineFactory;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 商品搜索标签
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月14日 上午10:58:20
 */
@Component
@Scope("prototype")
public class GoodsSearchTag extends BaseFreeMarkerTag {
	
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer pageSize = (Integer)params.get("pageSize");
		if(pageSize==null) pageSize = this.getPageSize();
		IGoodsSearchManager goodsSearchManager = SearchEngineFactory.getSearchEngine();

		int page = this.getPage();//使支持？号传递
		
		Page webPage  =  goodsSearchManager.search(page, pageSize);
		webPage.setCurrentPageNo(page);
		
		return webPage;
	}
	
}
