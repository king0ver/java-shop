package com.enation.app.shop.goods.service;

import com.enation.app.shop.goods.model.po.ParametersGroup;

/**
 * 参数组
 * @author fk
 * @version v1.0
 * 2017年4月10日 下午3:41:19
 */
public interface IParametersGroupManager {

	/**
	 * 增加分类关联的参数组
	 * @param categoryParamsGroup
	 * @return
	 */
	public ParametersGroup add(ParametersGroup categoryParamsGroup);

	/**
	 * 删除参数组
	 * @param group_id
	 */
	public void deleteGroup(Integer group_id);

	/**
	 * 修改参数组
	 * @param categoryParamsGroup
	 * @return
	 */
	public ParametersGroup update(ParametersGroup categoryParamsGroup);

	/**
	 * 参数组上移或者下移
	 * @param group_id
	 * @param sort_type
	 */
	public void groupSort(Integer group_id, String sort_type);

}
