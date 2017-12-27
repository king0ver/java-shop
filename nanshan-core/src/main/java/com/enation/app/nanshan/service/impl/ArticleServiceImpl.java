package com.enation.app.nanshan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.model.ArticleExt;
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
	public Page<ArticleVo> querySpecInfoByCatId(Integer catId, String specValIds, int pageNo, int pageSize) {
		if(catId == null){
			return null;
		}
		String sql = "select esns.id,esns.title,esns.cat_id catId,esns.url,esns.create_time createTime,esns.pic_url imgUrl,esns.summary,t.reserve_num reserveNum,t.reserved_num reservedNum,t.expiry_date expiryDate,t.act_name actName,t.act_cost actCost,t.act_address actAddress from es_nanshan_article esns left join es_nanshan_article_ext t on esns.id=t.article_id  where 1=1  ";
		if(StringUtils.isNotBlank(specValIds)){
			sql+= " EXISTS ( select 1 from es_nanshan_article_rel esnsar where " +
				"esns.id = esnsar.article_id and esnsar.specval_id in ("+specValIds+") ) ";
		}
		sql += " and esns.is_del = 0 and esns.cat_id = "+ catId;
		Page<ArticleVo> page=daoSupport.queryForPage(sql, pageNo,pageSize);
		return page;
	}

	@Override
	public ArticleVo queryArticleInfoById(Integer articleId) {

		if(null == articleId){
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url imgUrl,c.content ,t.reserve_num,t.reserved_num,t.expiry_date expiryDate,t.act_name,t.act_cost ,t.act_address ");
		sql.append("from es_nanshan_article a left join es_nanshan_clob c on a.content=c.id left join es_nanshan_article_ext t on a.id=t.article_id  ");
		sql.append(" where a.is_del = 0 and a.id= ?");
		NanShanArticleVo articleInfo = daoSupport.queryForObject(sql.toString(),NanShanArticleVo.class, articleId);
		if(articleInfo == null){
			return null;
		}
		return buildArticleVo(articleInfo);
	}

	@Override
	public ArticleVo queryArticleInfoByCatId(Integer catId) {
		if(null == catId){
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" a.id,a.title,a.cat_id,a.url,a.create_time,a.summary,a.pic_url,c.content ,t.reserve_num,t.reserved_num,t.expiry_date expiryDate,t.act_name,t.act_cost ,t.act_address ");
		sql.append("from es_nanshan_article a left join es_nanshan_clob c on a.content=c.id left join es_nanshan_article_ext t on a.id=t.article_id  ");
		sql.append(" where a.is_del = 0 and a.cat_id= ? limit 1");
		NanShanArticleVo articleInfo = daoSupport.queryForObject(sql.toString(),NanShanArticleVo.class, catId);
		if(articleInfo == null){
			return null;
		}
		return buildArticleVo(articleInfo);
	}

	private List<ArticleVo> queryArticleList(JSONArray ids){
	   List<ArticleVo> list=new ArrayList<ArticleVo>();
       for (Object id : ids) {
    	   ArticleVo articleVo=this.daoSupport.queryForObject("select id,title,cat_id catId,url,create_time dateTime,summary,pic_url imgUrl from es_nanshan_article where is_del=0 and id=?", ArticleVo.class, id.toString());
    	   list.add(articleVo);
	   }
	   return list;

	}

	/**
	 *
	 * @param articleInfo
	 * @return
     */
	private ArticleVo buildArticleVo(NanShanArticleVo articleInfo){

		ArticleVo articleVo = new ArticleVo();

		articleVo.setId(articleInfo.getId());
		articleVo.setCatId(articleInfo.getCat_id());
		articleVo.setTitle(articleInfo.getTitle());
		articleVo.setSummary(articleInfo.getSummary());
		articleVo.setUrl(articleInfo.getUrl());
		articleVo.setCreateTime(articleInfo.getCreate_time());
		articleVo.setImgUrl(articleInfo.getPic_url());
		if(StringUtils.isNotBlank(articleInfo.getContent())){
			if(articleInfo.getContent().startsWith("[")){
				JSONObject articleInfoContent = new JSONObject();
				articleInfoContent.put("content", JSONArray.fromObject(articleInfo.getContent()));
				articleVo.setContent(articleInfoContent);
			}else{
				JSONObject articleInfoContent = JSONObject.fromObject(articleInfo.getContent());
				articleVo.setContent(articleInfoContent);
				if(articleInfoContent.containsKey("articleIds")){
					JSONArray ar=articleInfoContent.getJSONArray("articleIds");
					if(ar!=null){
						articleVo.setArticleList(this.queryArticleList(ar));
					}

				}
			}
		}
		articleVo.setActAddress(articleInfo.getAct_address());
		articleVo.setActName(articleInfo.getAct_name());
		articleVo.setActCost(articleInfo.getAct_cost());
		articleVo.setReserveNum(articleInfo.getReserve_num());
		articleVo.setReservedNum(articleInfo.getReserved_num());
		articleVo.setExpiryDate(articleInfo.getExpiryDate());

		return articleVo;
	}
}
