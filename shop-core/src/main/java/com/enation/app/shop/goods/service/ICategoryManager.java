package com.enation.app.shop.goods.service;

import java.util.List;

import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.vo.CategoryVo;

/**
 * 
 * 商品分类
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月25日 下午5:06:34
 */
public interface ICategoryManager {

	/**
	 * 查询某个商品分类
	 * 
	 * @param id
	 * @return
	 */
	public Category get(Integer id);

	/**
	 * 查询分类列表，format不传代表普通业务调用
	 * 
	 * @param parentId
	 *            父id
	 * @param format
	 *            传plugin代表插件调用
	 * @return
	 */
	public List list(Integer parentId, String format);

	/**
	 * 修改分类
	 * 
	 * @param category
	 * @throws Exception
	 */
	public void update(Category category) throws Exception;

	/**
	 * 删除分类
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void delete(Integer id) throws Exception;

	/**
	 * 保存分类关联的规格(在后台关联使用)
	 * 
	 * @param categoryId
	 * @param choose_specs
	 * @return
	 */
	public Category saveSpec(Integer categoryId, Integer[] choose_specs);

	/**
	 * 保存分类关联的品牌
	 * 
	 * @param categoryId
	 * @param choose_brands
	 * @return
	 */
	public Category saveBrand(Integer categoryId, Integer[] choose_brands);

	/**
	 * 初始化缓存
	 */
	public List<Category> initCategory();

	/**
	 * 获取某个分类的所有父,商家中心商品编辑和添加时使用
	 * 
	 * @param catid
	 * @return
	 */
	public List<CategoryVo> getParents(int category_id);

	/**
	 * 商品发布，获取当前登录用户选择经营类目的所有父
	 * 
	 * @param category_id
	 * @return
	 */
	public List<Category> getGoodsParentsType(Integer category_id);

	/**
	 * 首页用 获取某个类别的所有子类，所有的子孙
	 * 
	 * @param parentid
	 * @return
	 */
	public List<CategoryVo> listAllChildren(Integer parentid);

	/**
	 * 后台添加商品类别
	 * 
	 * @param category
	 */
	public void saveAdd(Category category);

	/**
	 * 保存排序
	 * 
	 * @param category_ids
	 * @param category_sorts
	 */
	public void saveSort(Integer[] category_ids, Integer[] category_sorts);

	/**
	 * 保存分类关联的规格(在商家中心自定义关联使用)
	 * 
	 * @param categoryId
	 *            分类ID
	 * @param spec
	 *            规格id
	 */
	public void saveSellerSpec(Integer categoryId, Integer spec);
}
