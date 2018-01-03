package com.enation.app.nanshan.service;


import com.enation.app.nanshan.model.ArticleExt;
import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.vo.NCatVo;



public interface ICatManager {
	/**
	 * 初始化分类数据。
	 */
	public void reset();
	/** 
	* @Description: 查询分类信息
	* @author luyanfen  
	* @date 2017年12月18日 下午7:53:53
	*  
	*/ 
	public NCatVo getCatTree();
	

	
	
	
	


}
