package com.enation.app.nanshan.core.service;

import com.enation.app.nanshan.model.NanShanArticleVo;
import com.enation.framework.database.Page;

public interface IArticleManager {
	
	public void addArticle(NanShanArticleVo nanShanArticleVo);
	
	public Page queryArticleList(NanShanArticleVo nanShanArticleVo,int page,int pageSize);
	
	public NanShanArticleVo queryArticleById(int id); 
	
	public void updateArticle(NanShanArticleVo nanShanArticleVo);
	
	public void delArticle(int id);
	
	
	

}
