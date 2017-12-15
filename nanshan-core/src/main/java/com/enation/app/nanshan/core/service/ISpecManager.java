package com.enation.app.nanshan.core.service;

import java.util.List;
import java.util.Map;

import com.enation.app.nanshan.core.model.Spec;

/**
 * 属性规格信息服务
 * @author jianjianming
 * @version $Id: ISpecManager.java,v 0.1 2017年12月13日 下午4:58:42$
 */
public interface ISpecManager {
	
	/**
	 * 新增
	 * @param spec
	 */
	public void add(Spec spec);

	/**
	 * 所有规格列表
	 * @param map
	 * @return
	 */
	public List<Spec> list(Map<String,Object> params);

	/**
	 * 修改
	 * @param themeUri
	 */
	public void edit(Spec spec);
	
	/**
	 * 删除
	 * @param id
	 */
	public void delete(int id);


}
