package com.enation.app.shop.goods.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.goods.model.po.Brand;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.vo.BrandVo;
import com.enation.app.shop.goods.model.vo.SelectVo;
import com.enation.framework.database.Page;

/**
 * 
 * 品牌管理
 * 
 * @author zh
 * @version v1.0
 * @since v1.0 2017年3月5日 下午3:11:57
 */
public interface IBrandManager {
	/**
	 * 读取品牌详细
	 * 
	 * @param brand_id
	 *            品牌
	 * @return 品牌
	 */
	public Brand get(Integer brand_id);

	/**
	 * 检测品牌名称是否存在
	 * 
	 * @param name
	 *            品牌名称
	 * @param brandid
	 *            要排除的brandid,用于修改时的检测
	 * @return 存在返回true 不存在返回false
	 */
	public boolean checkname(String name, Integer brandid);

	/**
	 * 添加品牌
	 * 
	 * @param brand
	 *            品牌,Brand
	 */
	public void add(Brand brand);

	/**
	 * 修改品牌
	 * 
	 * @param brand
	 *            品牌,Brand
	 */
	public void update(Brand brand);

	/**
	 * 将品牌删除
	 * 
	 * @param bid
	 *            品牌Id数组
	 */
	public void delete(Integer[] bid);


	/**
	 * 获取品牌列表
	 * 
	 * @return 品牌列表
	 */
	public List<Brand> list();

	/**
	 * 查询分类关联的品牌
	 * 
	 * @param category_id
	 * @return
	 */
	public List<BrandVo> getBrandsByCategory(Integer category_id);

	/**
	 * 分页读取某个标签下的品牌 
	 * @param tag_id 标签id
	 * @return List<Brand>
	 */
	public List<Brand> listBrands(Integer tag_id);

	/**
	 * 按照条件搜索品牌列表
	 * 
	 * @param brandMap
	 *            搜索参数
	 * @param page
	 *            分页数
	 * @param pageSize
	 *            分页每页显示数量
	 * @return 品牌列表
	 */
	public Page searchBrand(Map brandMap, int page, int pageSize);


	/**
	 * 查询全部可用品牌
	 * 
	 * @return List<Brand>
	 */
	public List<Brand> getAllBrands();

	/**
	 * 查询所有品牌，包括分类绑定的品牌，select为true，后台分类绑定品牌使用
	 * 
	 * @param category_id 分类id
	 * @return List<SelectVo>
	 */
	public List<SelectVo> getCatBrand(Integer category_id);

	/**
	 * 生成 选择器列表
	 * 
	 * @param map
	 * @param cat
	 */
	public void createSelectorList(Map map, Category cat);

}