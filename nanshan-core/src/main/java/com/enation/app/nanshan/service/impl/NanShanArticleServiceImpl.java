package com.enation.app.nanshan.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.nanshan.model.NanShanArticle;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.app.nanshan.model.NanShanClob;
import com.enation.app.nanshan.service.INanShanArticleService;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.database.data.IDataOperation;

/**  
*
* @Description: 
* @author luyanfen
* @date 2017年12月14日 下午5:13:42
*  
*/ 
@Service("nanShanArticleService")
public class NanShanArticleServiceImpl implements INanShanArticleService  {

	@Autowired
	private IDataOperation dataOperation;	

	@Autowired
	private IDaoSupport  daoSupport;
	
	@Transactional
	@Override
	public void addArticle(NanShanArticleVo nanShanArticleVo) {	
		NanShanArticle nanShanArticle=new NanShanArticle();
		nanShanArticle.setCat_id(nanShanArticleVo.getCat_id());
		nanShanArticle.setSummary(nanShanArticleVo.getSummary());
		nanShanArticle.setCreate_time(nanShanArticleVo.getCreate_time());
		nanShanArticle.setIs_del(nanShanArticleVo.getIs_del());
		nanShanArticle.setPic_url(nanShanArticleVo.getPic_url());
		nanShanArticle.setTitle(nanShanArticleVo.getTitle());
		nanShanArticle.setUrl(nanShanArticleVo.getUrl());
		NanShanClob nanShanClob=new NanShanClob();
		nanShanClob.setContent(nanShanArticleVo.getContent());
		nanShanClob.setCategory(nanShanArticleVo.getCat_id());
		this.daoSupport.insert("es_nanshan_clob", nanShanClob);
		int clobId=this.daoSupport.queryForInt("SELECT LAST_INSERT_ID()");
		nanShanArticle.setContent(clobId);
		this.daoSupport.insert("es_nanshan_article", nanShanArticle);
	}

	@Override
	public Page queryArticleList(NanShanArticleVo nanShanArticleVo,int page,int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,c.content,t.cat_name from es_nanshan_article a,es_nanshan_clob c,es_nanshan_article_category t where a.cat_id=t.cat_id and a.content=c.id");
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		return webpage;
	
	}

	@Override
	public NanShanArticleVo queryArticleById(int id) {
		String sql ="select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,c.content,t.cat_name,c.id as content_id from es_nanshan_article a,es_nanshan_clob c,es_nanshan_article_category t where a.cat_id=t.cat_id and a.content=c.id and a.id=?";
		return this.daoSupport.queryForObject(sql, NanShanArticleVo.class, id);
	}

	@Override
	public void updateArticle(NanShanArticleVo nanShanArticleVo) {
		Map<String,Object> articleFields=new HashMap<String,Object>();	
		Map<String,Object> clobFields=new HashMap<String,Object>();
		if(!StringUtils.isEmpty(nanShanArticleVo.getTitle())&&!StringUtils.isBlank(nanShanArticleVo.getTitle())) articleFields.put("title", nanShanArticleVo.getTitle());
		if(!StringUtils.isEmpty(nanShanArticleVo.getContent())&&!StringUtils.isBlank(nanShanArticleVo.getContent())) clobFields.put("content", nanShanArticleVo.getContent());
		this.daoSupport.update("es_nanshan_article", articleFields, "id="+nanShanArticleVo.getId());
		this.daoSupport.update("es_nanshan_clob", clobFields, "id="+nanShanArticleVo.getContent_id());
	}

	@Override
	public void delArticle(int id) {
		Map map=new HashMap();
		map.put("is_del", 1);
		this.daoSupport.update("es_nanshan_article", map, "id="+id);
		
	}

}
