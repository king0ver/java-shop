package com.enation.app.nanshan.core.service;

import java.util.List;

import com.enation.app.nanshan.model.ArticleCat;

/**
 * 分类后端服务
 * @author jianjianming
 * @version $Id: ICatService.java,v 0.1 2017年12月22日 上午11:15:04$
 */
public interface ICatManager{
	
	/**
	 * 通过分类ID查询基础属性
	 * @param catIds (例如：1,2) 多个用逗号   注：最后不要出现逗号
	 * @return
	 */
	public List<ArticleCat> queryCatInfoByCatIds(String catIds);
	
	/**
	 * 通过分类ID查询该分类的下一级分类
	 * @param catIds (例如：1,2) 多个用逗号   注：最后不要出现逗号
	 * @return
	 */
	public List<ArticleCat> queryCatChildrenInfoByCatIds(String catIds);
	
	
	/** 
	* @param 
	* @Description:新增分类 
	* @date 2018年1月4日 下午5:37:35
	*  
	*/ 
	public void addCat(ArticleCat articleCat);
	
	/** 
	* @param @param id
	* @Description: 删除分类
	* @date 2018年1月4日 下午7:15:30
	*  
	*/ 
	public void delCat(int id);
	
	/** 
	* @Description: 查询单个分类
	* @author luyanfen  
	* @date 2018年1月8日 下午2:43:01
	*  
	*/ 
	public ArticleCat queryCatById(int id);
	
	
	/** 
	* @Description: 更改分类
	* @author luyanfen  
	* @date 2018年1月8日 下午2:47:31
	*  
	*/ 
	public void updateCat(ArticleCat articleCat);
	
}
