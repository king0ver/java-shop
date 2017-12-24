package com.enation.app.nanshan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.app.nanshan.service.IArticleService;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;

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
	public List<ArticleVo> querySpecInfoByCatId(Integer catId, String specValIds,int pageNo,int pageSize) {
		if(catId == null){
			return null;
		}
		String sql = "select esns.id,esns.title,esns.cat_id catId,esns.url,esns.pic_url,esns.summary imgUrl from es_nanshan_article esns where EXISTS ("+
				"select 1 from es_nanshan_article_rel esnsar where " +
				"esns.id = esnsar.article_id ";
		if(StringUtils.isNotBlank(specValIds)){
			sql+= " and esnsar.specval_id in ("+specValIds+")";
		}
		sql += ") and esns.is_del = 0 and esns.cat_id  = "+ catId;
		@SuppressWarnings("unchecked")
		List<ArticleVo> list = (List<ArticleVo>) daoSupport.queryForPage(sql, pageNo,pageSize);
		return list;
	}

	@Override
	public ArticleVo queryArticleInfoById(Integer articleId) {
		ArticleVo articleVo = new ArticleVo();
		if(null == articleId){
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,c.content ");
		sql.append("from es_nanshan_article a,es_nanshan_clob c");
		sql.append(" where a.content=c.id and a.is_del = 0 and a.id= ?");
		NanShanArticleVo articleInfo = daoSupport.queryForObject(sql.toString(),NanShanArticleVo.class, articleId);
		if(articleInfo == null){
			return null;
		}
		articleVo.setId(articleInfo.getId());
		articleVo.setCatId(articleInfo.getCat_id());
		articleVo.setTitle(articleInfo.getTitle());
		articleVo.setSummary(articleInfo.getSummary());
		articleVo.setUrl(articleInfo.getUrl());
		articleVo.setDateTime(DateUtil.toString(articleInfo.getCreate_time(), null));
		articleVo.setImgUrl(articleInfo.getPic_url());
		if(StringUtils.isNotBlank(articleInfo.getContent())){
			if(articleInfo.getContent().startsWith("[")){
				JSONObject articleInfoContent = new JSONObject();
				articleInfoContent.put("content", JSONArray.fromObject(articleInfo.getContent()));
				articleVo.setContent(articleInfoContent);
			}else{
				JSONObject articleInfoContent = JSONObject.fromObject(articleInfo.getContent());
				articleVo.setContent(articleInfoContent);
			}
		}
		return articleVo;
	}
	
}
