package com.enation.app.nanshan.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.core.service.ICatManager;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.framework.database.IDaoSupport;

/**
 * 分类服务实现
 * @author jianjianming
 * @version $Id: CatManagerImpl.java,v 0.1 2017年12月22日 上午11:20:30$
 */
@Service
public class CatManagerImpl implements ICatManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public List<ArticleCat> queryCatInfoByCatIds(String catIds) {
		if(StringUtils.isBlank(catIds)){
			return null;
		}
		String sql = "select * from es_nanshan_article_category t where t.cat_id in ("+catIds+")";
		return daoSupport.queryForList(sql, ArticleCat.class);
	}

	@Override
	public List<ArticleCat> queryCatChildrenInfoByCatIds(String catIds) {
		if(StringUtils.isBlank(catIds)){
			return null;
		}
		String sql = "select * from es_nanshan_article_category t where t.parent_id in ("+catIds+")";
		return daoSupport.queryForList(sql, ArticleCat.class);
	}

	@Override
	public void addCat(ArticleCat articleCat) {
		int id=this.daoSupport.queryForInt("select max(cat_id) from  es_nanshan_article_category", null);
		articleCat.setCat_id(id+1);
		String url="/nanshan/facility.html?catId="+articleCat.getCat_id();
		articleCat.setPc_url(url);
		articleCat.setWap_url(url);
		daoSupport.insert("es_nanshan_article_category", articleCat);
		
	}

	@Override
	public void delCat(int id) {
		if(id>0){
		daoSupport.execute("delete from es_nanshan_article_category where cat_id="+id);
		}
		
	}

	@Override
	public ArticleCat queryCatById(int id) {
		return daoSupport.queryForObject("select * from es_nanshan_article_category where cat_id=?", ArticleCat.class, id);
	}

	@Override
	public void updateCat(ArticleCat articleCat) {
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isNotBlank(articleCat.getCat_name())) map.put("cat_name", articleCat.getCat_name());
		if(StringUtils.isNotBlank(articleCat.getWap_cat_name())) map.put("wap_cat_name", articleCat.getWap_cat_name());
		if(StringUtils.isNotBlank(articleCat.getPc_icon())) map.put("pc_icon", articleCat.getPc_icon());
		if(StringUtils.isNotBlank(articleCat.getWap_icon())) map.put("wap_icon", articleCat.getWap_icon());
		daoSupport.update("es_nanshan_article_category", map, "cat_id="+articleCat.getCat_id());
		
		
		
	}

}
