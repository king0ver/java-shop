package com.enation.app.shop.goods.service;

import java.util.List;

import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.vo.SpecValueVo;

/**
 * 规格值manager
 * 
 * @author fk
 * @version v1.0 2017年3月22日 下午2:06:47
 */
public interface ISpecValueManager {

	/**
	 * 查询某规格的规格值
	 * 
	 * @param spec_id
	 * @return
	 */
	public List<SpecValueVo> querySpecValues(Integer spec_id);

	/**
	 * 查询某些规格的规格值集合
	 * 
	 * @param spec_ids
	 * @return
	 */
	public List<SpecValueVo> querySpecsValues(String spec_ids);

	/**
	 * 添加规格值
	 * 
	 * @param value
	 */
	public void add(Integer spec_id, List<SpecValue> value);

	/**
	 * 修改规格值
	 * 
	 * @param value
	 */
	public void update(SpecValue value);

	/**
	 * 商家某分类规格的获取
	 * 
	 * @param catid
	 * @return Specification 集合
	 */
	public List<SpecValue> listBySpecid(Integer specid);

	/**
	 * 商家手动自定义添加某规格项的规格值
	 * 
	 * @param specValue
	 *            规格值
	 * @return SpecValue 规格值
	 */
	public SpecValue addSpecValue(SpecValue specValue);
}
