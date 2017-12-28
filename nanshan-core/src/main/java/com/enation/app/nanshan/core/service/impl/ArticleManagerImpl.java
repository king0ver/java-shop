package com.enation.app.nanshan.core.service.impl;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.nanshan.model.ArticleCat;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.nanshan.core.service.IArticleManager;
import com.enation.app.nanshan.model.ArtSpecRel;
import com.enation.app.nanshan.model.ArticleExt;
import com.enation.app.nanshan.model.ArticleQueryParam;
import com.enation.app.nanshan.model.NanShanArticle;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.app.nanshan.model.NanShanClob;
import com.enation.app.nanshan.util.EnumUtil;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.database.data.IDataOperation;
import com.enation.framework.util.DateUtil;
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
		nanShanArticleVo.setId(this.daoSupport.getLastId("es_nanshan_article"));
		if(!StringUtil.isEmpty(nanShanArticleVo.getAct_name())){
			ArticleExt articleExt=this.covertArticleExt(nanShanArticleVo);
			this.insertArtcleExt(articleExt);
		}
		if(!StringUtil.isEmpty(nanShanArticleVo.getSpecValIds())){
			this.addArtSpecRel(nanShanArticleVo.getSpecValIds(),nanShanArticleVo.getId());
		}
	}

	@Override
	public Page queryArticleList(ArticleQueryParam param,int page,int pageSize) {
		StringBuffer sql = new StringBuffer();
        
		sql.append(
				"select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,c.content,t.cat_name from es_nanshan_article a,es_nanshan_clob c,es_nanshan_article_category t where a.cat_id=t.cat_id and a.content=c.id and a.is_del<>1");
		if(!StringUtil.isEmpty(param.getCatId())){
			sql.append(" and a.cat_id in ("+param.getCatId()+")");
		}
		if(!StringUtil.isEmpty(param.getArticleId())){
			sql.append(" and a.id="+param.getArticleId());
		}
		if(!StringUtil.isEmpty(param.getArticleName())){
			sql.append(" and a.titile like %"+param.getArticleName()+"%");
		}
		if(!StringUtil.isEmpty(param.getParentId())){
			sql.append(" and t.parent_id="+param.getParentId());
		}
		if(!StringUtil.isEmpty(param.getIsDel())){
			sql.append(" and a.is_del="+param.getIsDel());
		}
		if(!StringUtil.isEmpty(param.getStartDate())){
			sql.append(" and a.create_time>="+DateUtil.getDateline(param.getStartDate(), "yyyy-MM-dd hh:mm:ss"));
		};
		if(!StringUtil.isEmpty(param.getEndDate())){
			sql.append(" and a.create_time<="+DateUtil.getDateline(param.getEndDate(), "yyyy-MM-dd hh:mm:ss"));
		};
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		return webpage;
	}

	@Override
	public NanShanArticleVo queryArticleById(int id) {
		String sql ="select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,c.content,t.cat_name,ifnull(c.id,0) as content_id,IFNULL(e.reserve_num,0) reserve_num,IFNULL(e.reserved_num,0) reserved_num,e.act_name,IFNULL(e.act_cost,0) act_cost,e.act_address,ifnull(e.expiry_date,0) expiryDate,(select group_concat(l.specval_id) from es_nanshan_article_rel l where l.article_id = a.id group by l.article_id) specValIds from es_nanshan_article a left join es_nanshan_clob c on a.content=c.id LEFT JOIN es_nanshan_article_category t on a.cat_id=t.cat_id LEFT JOIN es_nanshan_article_ext e on a.id=e.article_id where    a.id=?";
		return this.daoSupport.queryForObject(sql, NanShanArticleVo.class, id);
	}

	@Override
	public void updateArticle(NanShanArticleVo nanShanArticleVo) {
		Map<String,Object> articleFields=new HashMap<String,Object>();	
		Map<String,Object> clobFields=new HashMap<String,Object>();
		if(!StringUtils.isEmpty(nanShanArticleVo.getTitle())) articleFields.put("title", nanShanArticleVo.getTitle());
		if(nanShanArticleVo.getCat_id()>0) articleFields.put("cat_id", nanShanArticleVo.getCat_id());
		if(!StringUtils.isEmpty(nanShanArticleVo.getSummary())) articleFields.put("summary", nanShanArticleVo.getSummary());
		if(nanShanArticleVo.getCreate_time()>0) articleFields.put("create_time", nanShanArticleVo.getCreate_time());
		if(!StringUtils.isEmpty(nanShanArticleVo.getContent())) clobFields.put("content", nanShanArticleVo.getContent());
		if(!StringUtils.isEmpty(nanShanArticleVo.getPic_url())) articleFields.put("pic_url", nanShanArticleVo.getPic_url());
		this.delArtSpeRel(nanShanArticleVo.getId());
		this.addArtSpecRel(nanShanArticleVo.getSpecValIds(), nanShanArticleVo.getId());
		if(articleFields.size()>0){
			this.daoSupport.update("es_nanshan_article", articleFields, "id="+nanShanArticleVo.getId());
		}
		this.daoSupport.update("es_nanshan_clob", clobFields, "id="+nanShanArticleVo.getContent_id());
		if(!StringUtil.isEmpty(nanShanArticleVo.getAct_name())){
			ArticleExt articleExt=new ArticleExt();
			articleExt=this.covertArticleExt(nanShanArticleVo);
			this.updateArtcleExt(articleExt);
		}
		
	}

	@Override
	public void delArticle(int id) {
		Map map=new HashMap();
		map.put("is_del", 1);
		this.daoSupport.update("es_nanshan_article", map, "id="+id);
		
	}

	/**
	 *  条件查询信息
	 * @param params
	 * @param page
	 * @param pageSize
	 * @return
	 * @see com.enation.app.nanshan.core.service.IArticleManager#queryArticleListByConiditon(java.util.Map, int, int)
	 */
	@Override
	public Page queryArticleListByConiditon(Map<String, Object> params,int page, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,c.content,t.cat_name ");
		sql.append("from es_nanshan_article a,es_nanshan_clob c,es_nanshan_article_category t ");
		sql.append(" where a.cat_id=t.cat_id and a.content=c.id ");
		if(params.containsKey("catParentIds")){
			if(StringUtils.isNotBlank(String.valueOf(params.get("catParentIds")))){
				sql.append(" and t.parent_id in (").append(String.valueOf(params.get("catParentIds"))).append(") ");
			}
		}
		if(params.containsKey("catIds")){
			if(StringUtils.isNotBlank(String.valueOf(params.get("catIds")))){
				sql.append(" and t.cat_id in (").append(String.valueOf(params.get("catIds"))).append(") ");
			}
		}
		return this.daoSupport.queryForPage(sql.toString(), page, pageSize);
	}


	@Override
	public List<ArticleCat> getCats(int id) {

		String sql = "select * from es_nanshan_article_category where parent_id="+id;
		List<ArticleCat> list = (List<ArticleCat>) this.daoSupport.queryForList(sql, ArticleCat.class);
		return list;
	}

	
	private void insertArtcleExt(ArticleExt articleExt ){
		
		this.daoSupport.insert("es_nanshan_article_ext", articleExt);
	}
	
	private void updateArtcleExt(ArticleExt articleExt){
		this.daoSupport.update("es_nanshan_article_ext", this.objectToMap(articleExt), "article_id="+articleExt.getArticle_id());
	}
	
	private ArticleExt covertArticleExt(NanShanArticleVo vo){
		ArticleExt ext=new ArticleExt();
		ext.setArticle_id(vo.getId());
		ext.setAct_address(vo.getAct_address());
		ext.setAct_cost(vo.getAct_cost());
		ext.setAct_name(vo.getAct_name());
		ext.setReserved_num(vo.getReserved_num());
		ext.setReserve_num(vo.getReserve_num());
		ext.setAct_cost(vo.getAct_cost());
		ext.setExpiry_date(vo.getExpiryDate());
		return ext;
		
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
					this.addSpecRel(artSpecRel);
				}
			}
		 }	
	}
	
	
	public void addSpecRel(ArtSpecRel artSpecRel) {
		this.daoSupport.insert("es_nanshan_article_rel", artSpecRel);
		
	}
	
	private Map<String,Object>objectToMap(Object obj) {
		Map<String, Object> map = new HashMap<>();
		try {
			 Class<?> clazz = obj.getClass();
		        for (Field field : clazz.getDeclaredFields()) {
		       	 field.setAccessible(true);
		       	 String fieldName = field.getName();
		       	 if(fieldName.equals("serialVersionUID")) continue;
		            if((field.getGenericType().toString().equals("int")||field.getGenericType().toString().equals("long"))&&Integer.valueOf(field.get(obj).toString())>0){
		           	 map.put(fieldName, field.get(obj)); 
		            }
		            if(field.getGenericType().toString().equals("class java.lang.String")&&field.get(obj)!=null&&!StringUtil.isEmpty(field.get(obj).toString())){
		           	 map.put(fieldName, field.get(obj));
		            }
		       
		          }
		} catch (Exception e) {
			
		}
       
	  return map;
	}

	@Override
	public NanShanArticleVo queryArtByCatId(int id) {
		String sql ="select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,c.content,t.cat_name,ifnull(c.id,0) as content_id,IFNULL(e.reserve_num,0) reserve_num,IFNULL(e.reserved_num,0) reserved_num,e.act_name,IFNULL(e.act_cost,0) act_cost,e.act_address,ifnull(e.expiry_date,0) expiryDate  from es_nanshan_article a left join es_nanshan_clob c on a.content=c.id LEFT JOIN es_nanshan_article_category t on a.cat_id=t.cat_id LEFT JOIN es_nanshan_article_ext e on a.id=e.article_id  where    a.cat_id=?";
		return this.daoSupport.queryForObject(sql, NanShanArticleVo.class, id);
	}

	@Override
	public Page queryReserveList(ReserveQueryParam param, int page, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.article_id,title,t.act_time,t.member_name,age,phone_number,email from es_nanshan_act_reserve t,es_nanshan_article a WHERE t.article_id=a.id");

		if(StringUtils.isNotBlank(param.getArticleName())){
				sql.append(" and title like '%"+param.getArticleName()+"%'");
		}
		if(StringUtils.isNotBlank(param.getMemberName())){
			sql.append(" and t.member_name like '%"+param.getMemberName()+"%'");
	    }
		if(StringUtils.isNotBlank(param.getArticleId())){
				sql.append(" and t.article_id="+param.getArticleId());
		}
		return this.daoSupport.queryForPage(sql.toString(), page, pageSize);
	}

	/*
	 * 通过分类查询信息
	 * @see com.enation.app.nanshan.core.service.IArticleManager#queryArticleByCatId(int)
	 */
	@Override
	public List<NanShanArticleVo> queryArticleByCatId(int catId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.content content_id,a.is_del,c.content,t.cat_name ");
		sql.append("from es_nanshan_article a,es_nanshan_clob c,es_nanshan_article_category t ");
		sql.append(" where a.cat_id=t.cat_id and a.content=c.id and t.cat_id = ?");
		return daoSupport.queryForList(sql.toString(),NanShanArticleVo.class,catId);
	}
}
