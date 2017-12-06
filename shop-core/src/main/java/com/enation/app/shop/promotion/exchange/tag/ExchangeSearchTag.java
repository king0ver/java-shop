package com.enation.app.shop.promotion.exchange.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goodssearch.service.IGoodsSearchManager;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 积分商品搜索标签
 * @author    jianghongyan
 * @version   1.0.0,2016年6月20日
 * @since     v6.1
 */
@Component
@Scope("prototype")
public class ExchangeSearchTag extends BaseFreeMarkerTag{

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer pageSize = (Integer)params.get("pageSize");
		if(pageSize==null) pageSize = this.getPageSize();
		IGoodsSearchManager goodsSearchManager = SpringContextHolder.getBean("goodsSearchLuceneManager");;
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		request.setAttribute("is_exchange", true);
		int page=this.getPage();//使支持？号传递
		Page webPage  =  goodsSearchManager.search(page, pageSize);
		webPage.setCurrentPageNo(page);
		return webPage;
	}
	
}
