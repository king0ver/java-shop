package com.enation.app.nanshan.core.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.model.ArtSpecRel;
import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.model.NanShanArticle;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.app.nanshan.model.NanShanClob;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.database.data.IDataOperation;
import com.enation.framework.util.StringUtil;

/**  
*
* @Description: 
* @author luyanfen
* @date 2017年12月14日 下午5:13:42
*  
*/ 
@Service("articleManager")
public class ArticleManagerImpl implements IArticleManager  {

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
		int clobId=this.daoSupport.getLastId("es_nanshan_clob");
		nanShanArticle.setContent(clobId);
		this.daoSupport.insert("es_nanshan_article", nanShanArticle);
		int artId=this.daoSupport.getLastId("es_nanshan_article");
		this.addArtSpecRel(nanShanArticleVo.getSpecValIds(), artId);
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
		String sql ="select c.id content_id, a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,c.content,t.cat_name,group_concat(l.id) specValIds "+ 
					"from es_nanshan_article a LEFT JOIN es_nanshan_clob c on a.content=c.id "+
					"left JOIN es_nanshan_article_category t on a.cat_id=t.cat_id "+
					"LEFT JOIN es_nanshan_article_rel l on a.id=l.article_id where a.id=? group by a.id";
		return this.daoSupport.queryForObject(sql, NanShanArticleVo.class, id);
	}

	@Override
	public void updateArticle(NanShanArticleVo nanShanArticleVo) {
		Map<String,Object> articleFields=new HashMap<String,Object>();	
		Map<String,Object> clobFields=new HashMap<String,Object>();
		if(!StringUtils.isEmpty(nanShanArticleVo.getTitle())&&!StringUtils.isBlank(nanShanArticleVo.getTitle())) articleFields.put("title", nanShanArticleVo.getTitle());
		if(nanShanArticleVo.getCat_id()>0) articleFields.put("cat_id", nanShanArticleVo.getCat_id());
		if(!StringUtils.isEmpty(nanShanArticleVo.getSummary())&&!StringUtils.isBlank(nanShanArticleVo.getSummary())) articleFields.put("summary", nanShanArticleVo.getSummary());
		if(nanShanArticleVo.getCreate_time()>0) articleFields.put("create_time", nanShanArticleVo.getCreate_time());
		if(!StringUtils.isEmpty(nanShanArticleVo.getContent())&&!StringUtils.isBlank(nanShanArticleVo.getContent())) clobFields.put("content", nanShanArticleVo.getContent());
		this.delArtSpeRel(nanShanArticleVo.getId());
		this.addArtSpecRel(nanShanArticleVo.getSpecValIds(), nanShanArticleVo.getId());
		this.daoSupport.update("es_nanshan_article", articleFields, "id="+nanShanArticleVo.getId());
		this.daoSupport.update("es_nanshan_clob", clobFields, "id="+nanShanArticleVo.getContent_id());
		
	}

	@Override
	public void delArticle(int id) {
		Map map=new HashMap();
		map.put("is_del", 1);
		this.daoSupport.update("es_nanshan_article", map, "id="+id);
		
	}

	@Override
	public List<ArticleCat> getCats(int id) {
		
		String sql = "select * from es_nanshan_article_category where parent_id="+id;
		List<ArticleCat> list = (List<ArticleCat>) this.daoSupport.queryForList(sql, ArticleCat.class);
		return list;
	}

	
	public void addArtSpeRel(ArtSpecRel artSpecRel) {
		this.daoSupport.insert("es_nanshan_article_rel", artSpecRel);
		
	}
	
	public void delArtSpeRel(int artId){
		this.daoSupport.execute("delete from es_nanshan_article_rel where article_id=?", artId);
	}
	
	private void addArtSpecRel(String specIdStr,int artId){
		String[] specIds;
		ArtSpecRel  artSpecRel;
		 if(!StringUtil.isEmpty(specIdStr)){
			 specIds=specIdStr.split(",");
			 for (String string : specIds) {
				if(!StringUtil.isEmpty(specIdStr)){
					artSpecRel=new ArtSpecRel();
					artSpecRel.setArticle_id(artId);
					artSpecRel.setSpecval_id(Integer.valueOf(string));
					this.addArtSpeRel(artSpecRel);
				}
			}
		 }	
	}

}
