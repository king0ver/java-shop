package com.enation.app.nanshan.core.service;

import java.util.List;
import java.util.Map;

import com.enation.app.nanshan.model.ArticleCat;
import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.framework.database.Page;

public interface IArticleManager {
	
	public void addArticle(NanShanArticleVo nanShanArticleVo);
	
	public Page queryArticleList(NanShanArticleVo nanShanArticleVo,int page,int pageSize);
	
	public NanShanArticleVo queryArticleById(int id); 
	
	public void updateArticle(NanShanArticleVo nanShanArticleVo);
	
	public void delArticle(int id);
	
	/**
	 * 条件查询信息
	 * @return
	 */
	public Page queryArticleListByConiditon(Map<String, Object> params,int page,int pageSize);

	/**
	 * 通过分类查询信息
	 * @param catId
	 * @return
	 */
	public List<NanShanArticleVo> queryArticleByCatId(int catId);
	
	public List<ArticleCat> getCats(int id);
	
}
