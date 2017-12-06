package com.enation.app.shop.promotion.exchange.service;

import java.util.List;

import com.enation.app.shop.promotion.exchange.model.po.ExchangeGoodsCategory;
import com.enation.app.shop.promotion.exchange.model.vo.ExchangeGoodsCategoryVo;

/**
 * 
 * 积分商城分类操作接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月25日 下午6:04:24
 */
public interface IExchangeCategoryManager {

	/**
	 * 根据分类id 获取积分商品分类 直接查库
	 * 
	 * @param cat_id
	 * @return
	 */
	public ExchangeGoodsCategory getById(int cat_id);

	/**
	 * 积分商城首页
	 * 
	 * @param parentid
	 * @return
	 */
	public List<ExchangeGoodsCategoryVo> listAllChildren(Integer parentid);

	/**
	 * 获取所有子分类（积分商城 前台后台分类使用） 列表和插件都可以使用
	 * 
	 * @param parentid
	 *            父分类ID
	 * @return
	 */
	public List getListChildren(Integer parentId);

	/**
	 * 后台添加积分商城的商品类别
	 * 
	 * @param category
	 */
	public void saveAdd(ExchangeGoodsCategory category);

	/**
	 * 修改分类
	 * 
	 * @param category
	 * @throws Exception
	 */
	public void update(ExchangeGoodsCategory category) throws Exception;

	/**
	 * 删除分类
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void delete(Integer id) throws Exception;
}
