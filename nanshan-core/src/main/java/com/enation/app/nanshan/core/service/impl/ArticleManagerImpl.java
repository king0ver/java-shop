package com.enation.app.nanshan.core.service.impl;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.nanshan.model.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.nanshan.core.service.IArticleManager;
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
		nanShanArticle.setWork_place(nanShanArticleVo.getWork_place());
		nanShanArticle.setJob_cat(nanShanArticleVo.getJob_cat());
		nanShanArticle.setDept_name(nanShanArticleVo.getDept_name());
		NanShanClob nanShanClob=new NanShanClob();
		if(StringUtils.isNotEmpty(nanShanArticleVo.getContent())){
			nanShanClob.setContent(nanShanArticleVo.getContent().replaceAll("\\\\n",""));
		}
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
				"select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,a.work_place,a.job_cat,a.dept_name,c.content,t.cat_name from es_nanshan_article a,es_nanshan_clob c,es_nanshan_article_category t where a.cat_id=t.cat_id and a.content=c.id ");
		if(!StringUtil.isEmpty(param.getCatId())){
			sql.append(" and a.cat_id in ("+param.getCatId()+")");
		}
		if(!StringUtil.isEmpty(param.getArticleId())){
			sql.append(" and a.id="+param.getArticleId());
		}
		if(!StringUtil.isEmpty(param.getArticleName())){
			sql.append(" and a.title like '%"+param.getArticleName()+"%'");
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
		sql.append("  order by a.create_time desc");
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		return webpage;
	}

	@Override
	public NanShanArticleVo queryArticleById(int id) {
		String sql ="select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,a.work_place,a.job_cat,a.dept_name,c.content,t.cat_name,ifnull(c.id,0) as content_id,IFNULL(e.reserve_num,0) reserve_num,IFNULL(e.reserved_num,0) reserved_num,e.act_name,IFNULL(e.act_cost,0) act_cost,e.act_address,ifnull(e.expiry_date,0) expiryDate,(select group_concat(l.specval_id) from es_nanshan_article_rel l where l.article_id = a.id group by l.article_id) specValIds from es_nanshan_article a left join es_nanshan_clob c on a.content=c.id LEFT JOIN es_nanshan_article_category t on a.cat_id=t.cat_id LEFT JOIN es_nanshan_article_ext e on a.id=e.article_id where    a.id=?";
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
		if(!StringUtils.isEmpty(nanShanArticleVo.getContent())) clobFields.put("content", nanShanArticleVo.getContent().replaceAll("\\\\n",""));
		if(!StringUtils.isEmpty(nanShanArticleVo.getJob_cat())) articleFields.put("job_cat", nanShanArticleVo.getJob_cat());
		if(!StringUtils.isEmpty(nanShanArticleVo.getDept_name())) articleFields.put("dept_name", nanShanArticleVo.getDept_name());
		if(!StringUtils.isEmpty(nanShanArticleVo.getWork_place())) articleFields.put("work_place", nanShanArticleVo.getWork_place());
		articleFields.put("pic_url", nanShanArticleVo.getPic_url());
		this.delArtSpeRel(nanShanArticleVo.getId());
		
		this.addArtSpecRel(nanShanArticleVo.getSpecValIds(), nanShanArticleVo.getId());
		if(articleFields.size()>0){
			this.daoSupport.update("es_nanshan_article", articleFields, "id="+nanShanArticleVo.getId());
		}
		if(nanShanArticleVo.getContent_id()>0){

			this.daoSupport.update("es_nanshan_clob", clobFields, "id="+nanShanArticleVo.getContent_id());
		}
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
		if(params.containsKey("id")){
			if(StringUtils.isNotBlank(String.valueOf(params.get("id")))){
				sql.append(" and a.id="+String.valueOf(params.get("id")));
			}
		}
		if(params.containsKey("title")){
			if(StringUtils.isNotBlank(String.valueOf(params.get("title")))){
				sql.append(" and  a.title like '%").append(String.valueOf(params.get("title"))).append("%' ");
			}
		}
		if(params.containsKey("startDate")){
			if(StringUtils.isNotBlank(String.valueOf(params.get("startDate")))){
				sql.append(" and  a.create_time >=").append(DateUtil.getDateline(String.valueOf(params.get("startDate")), "yyyy-MM-dd hh:mm:ss"));
			}
		}
		if(params.containsKey("endDate")){
			if(StringUtils.isNotBlank(String.valueOf(params.get("endDate")))){
				sql.append(" and a.create_time <=").append(DateUtil.getDateline(String.valueOf(params.get("endDate")), "yyyy-MM-dd hh:mm:ss"));
			}
		}
		sql.append(" order by a.create_time desc");
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

		ext.setReserve_num(vo.getReserve_num());
		ext.setAct_cost(vo.getAct_cost());
		if(!StringUtil.isEmpty(vo.getExpiryDate())){
			long create_time = DateUtil.getDateline(vo.getExpiryDate(), "yyyy-MM-dd hh:mm:ss");
			ext.setExpiry_date(create_time);
		}
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
		String sql ="select a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,a.is_del,a.work_place,a.job_cat,a.dept_name,c.content,t.cat_name,ifnull(c.id,0) as content_id,IFNULL(e.reserve_num,0) reserve_num,IFNULL(e.reserved_num,0) reserved_num,e.act_name,IFNULL(e.act_cost,0) act_cost,e.act_address,ifnull(e.expiry_date,0) expiryDate  from es_nanshan_article a left join es_nanshan_clob c on a.content=c.id LEFT JOIN es_nanshan_article_category t on a.cat_id=t.cat_id LEFT JOIN es_nanshan_article_ext e on a.id=e.article_id  where    a.cat_id=?";
		return this.daoSupport.queryForObject(sql, NanShanArticleVo.class, id);
	}

	@Override
	public Page queryReserveList(ReserveQueryParam param, int page, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.activity_id,title,t.member_id,t.is_del,t.activity_time,t.attend_name,age,phone_number,email from es_nanshan_act_reserve t,es_nanshan_article a WHERE t.activity_id=a.id");

		if(StringUtils.isNotBlank(param.getArticleName())){
				sql.append(" and title like '%"+param.getArticleName()+"%'");
		}
		if(StringUtils.isNotBlank(param.getMemberName())){
			sql.append(" and t.attend_name like '%"+param.getMemberName()+"%'");
	    }
		if(StringUtils.isNotBlank(param.getArticleId())){
				sql.append(" and t.activity_id="+param.getArticleId());
		}
		if(StringUtils.isNotBlank(param.getMemberId())){
			sql.append(" and t.member_id="+param.getMemberId());
	    }
		if(StringUtils.isNotBlank(param.getEmail())){
			sql.append(" and t.email="+param.getEmail());
			
	    }
		if(StringUtils.isNotBlank(param.getPhoneNumber())){
			sql.append(" and t.phone_number="+param.getPhoneNumber());
	    }
		if(StringUtils.isNotBlank(param.getIsDel())){
			sql.append(" and t.is_del="+param.getIsDel());
	    }
		sql.append(" order by t.activity_time desc");
		return this.daoSupport.queryForPage(sql.toString(), page, pageSize);
	}

	@Override
	public void updateActivityExpire() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("update es_nanshan_article t set t.cat_id = 39 where t.cat_id = 38")
			  .append(" and t.id in ( select b.article_id from b2b2c.es_nanshan_article_ext b")
			  .append(" where  b.expiry_date < now())");

		daoSupport.execute(buffer.toString());
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



	public static void main(String[] args){

		System.out.println("{\"content\":[{\"index\":0,\"type\":\"text\",\"content\":\"“志合者，不以山海为远”，为积极落实国家提出的“一带一路”倡议，实现沿线国家科普场馆互通互联、繁荣发展，11月27 -28日，由中国自然科学博物馆协会主办、中国科学技术馆和上海科技馆联合承办的首届“一带一路”科普场馆发展国际研讨会在北京隆重召开。来自“一带一路”沿线22个国家24个科普场馆和机构的44位馆长或负责人，以及中国国内包括自然历史博物馆、科学技术馆、天文馆、国土资\\n\\n\\n\\n源博物馆等在内的8大类74家科普场馆和机构、15家科普企业的130余位馆长或负责人齐聚中国科学技术馆，围绕“协同共享、场馆互惠、共建科学传播丝绸之路”大会主题，共话沿线国家科普场馆间长远合作愿景。11月29-30日，与会外方代表赴上海科技馆、上海自然博物馆（上海科技馆分馆）进行专业参观。\"}]}".replaceAll("\\\\n",""));

	}
}
