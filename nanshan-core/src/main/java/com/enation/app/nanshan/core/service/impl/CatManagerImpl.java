package com.enation.app.nanshan.core.service.impl;

import java.util.List;

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

}
