package com.enation.app.nanshan.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.database.IDaoSupport;

/**
 * 文章服务实现
 * @author jianjianming
 * @version $Id: ArticleServiceImpl.java,v 0.1 2017年12月21日 下午9:49:17$
 */
@Service("articleService")
public class ArticleServiceImpl implements IArticleService {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public List<ArticleVo> querySpecInfoByCatId(Integer catId, String specValIds) {
		if(catId == null){
			return null;
		}
		String sql = "select esns.id,esns.title,esns.cat_id catId,esns.url,esns.pic_url,esns.summary imgUrl from es_nanshan_article esns where EXISTS ("+
				"select 1 from es_nanshan_article_rel esnsar where " +
				"esns.id = esnsar.article_id ";
		if(StringUtils.isNotBlank(specValIds)){
			sql+= " and esnsar.specVal_id in ("+specValIds+")";
		}
		sql += ") and esns.cat_id  = "+ catId;
		return daoSupport.queryForList(sql, ArticleVo.class);
	}
	
}
