package com.enation.app.shop.goods.service;

import com.enation.app.shop.goods.model.po.Parameters;

/**
 * 参数
 * @author fk
 * @version v1.0
 * 2017年4月10日 下午4:45:02
 */
public interface IParametersManager {

	/**
	 * 添加参数
	 * @param param
	 * @return
	 */
	public Parameters add(Parameters param);

	/**
	 * 删除参数
	 * @param param_id
	 */
	public void delete(Integer param_id);

	/**
	 * 修改参数
	 * @param param
	 * @return
	 */
	public Parameters update(Parameters param);

	/**
	 * 删除参数组下的参数
	 * @param group_id
	 */
	public void deleteByGroup(Integer group_id);

	/**
	 * 参数上移或者下移
	 * @param param_id
	 * @param sort_type
	 */
	public void paramSort(Integer param_id, String sort_type);

}
