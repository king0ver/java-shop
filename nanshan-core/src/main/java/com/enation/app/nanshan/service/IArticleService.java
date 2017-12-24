package com.enation.app.nanshan.service;

import java.util.List;

import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.database.Page;

/**
 * 文章服务
 * @author jianjianming
 * @version $Id: IArticleService.java,v 0.1 2017年12月21日 下午9:41:38$
 */
public interface IArticleService {
	/**
	 * 通过分类ID查询文章信息
	 * @param catId
	 * @param spceValId
	 * @return
	 */
	public Page<ArticleVo> querySpecInfoByCatId(Integer catId, String spceValId, int pageNo, int pageSize);
	
	/**
	 * 通过文章ID查询文章信息
	 * @param articleId
	 * @return
	 */
	public ArticleVo queryArticleInfoById(Integer articleId);
}
