package com.enation.app.shop.goods.service;

import java.util.List;

import com.enation.app.shop.goods.model.po.Parameters;
import com.enation.app.shop.goods.model.vo.GoodsParamsList;

/**
 * 分类参数manager
 * 
 * @author fk
 * @version v1.0 2017年4月6日 下午6:02:01
 */
public interface ICategoryParamsManager {

	/**
	 * 根据分类和商品查询商品参数组
	 * 
	 * @param category_id
	 * @param goods_id
	 * @return
	 */
	public List<GoodsParamsList> getParamByCatAndGoods(Integer category_id, Integer goods_id);

	/**
	 * 查询分类关联的参数
	 * 
	 * @param category_id
	 * @return
	 */
	public List<GoodsParamsList> getParamByCategory(Integer category_id);

	/**
	 * 查询分类关联的可检索的参数集合
	 * 
	 * @param category_id
	 * @return
	 */
	public List<Parameters> getIndexParams(Integer category_id);

	/**
	 * 查询分类关联的参数，包括参数组
	 * 
	 * @param category_id
	 * @param goods_id
	 * @return
	 */
	public List<GoodsParamsList> getCategoryParams(Integer category_id, Integer goods_id);
}
