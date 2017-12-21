package com.enation.app.nanshan.service;

import java.util.List;

import com.enation.app.nanshan.vo.SpecVo;

/**
 * 基础属性服务
 * @author jianjianming
 * @version $Id: SpecService.java,v 0.1 2017年12月21日 下午2:57:09$
 */
public interface ISpecService {
	
	/**
	 * 通过分类ID属性信息
	 * @param catId
	 * @return
	 */
	public List<SpecVo> querySpecInfoByCatId(Integer catId);
}
