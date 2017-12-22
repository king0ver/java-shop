package com.enation.app.nanshan.service;

import java.util.List;

import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.vo.NCatVo;



public interface ICatManager {
	/**
	 * 初始化分类数据。
	 */
	public void reset();
	
	

	/** 
	* @Description:预约接口，前端调用 
	* @author luyanfen  
	* @date 2017年12月18日 下午7:53:24
	*  
	*/ 
	public void reserve(NanShanActReserve NanShanActReserve);
	
	/** 
	* @Description: 查询分类信息
	* @author luyanfen  
	* @date 2017年12月18日 下午7:53:53
	*  
	*/ 
	public List<NCatVo> getCatList();
	
	
	


}
