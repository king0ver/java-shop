package com.enation.app.shop.promotion.fulldiscount.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscount;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.framework.database.Page;

/**
 * 
 * 满优惠活动管理借口接口
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月4日 下午5:23:21
 */
public interface IFullDiscountManager {
	/**
	 * 查询添加和修改的活动与正在进行的活动冲突
	 * @param fullDiscountVo 满优惠促销vo实体
	 * @return 返回封装的冲突商品的信息
	 */
	public Boolean checkGoods(FullDiscountVo fullDiscountVo);
	/**
	 * 根据满优惠活动ID获取满优惠活动
	 * @param fd_id 满优惠活动ID
	 * @return 满优惠活动实体
	 */
	public FullDiscountVo get(Integer fd_id);
	
	/**
	 * 从数据库获取促销信息
	 * @param fd_id 满优惠活动ID
	 * @return 满优惠活动实体
	 */
	public FullDiscountVo getFromDB(Integer fd_id);
	/**
	 * 添加满优惠促销活动
	 * @param fullDiscountVo 满优惠促销vo实体
	 */
	public void add(FullDiscountVo fullDiscountVo);
	
	/**
	 * 修改满优惠促销活动
	 * @param fullDiscountVo 满优惠促销vo实体
	 */
	public void edit(FullDiscountVo fullDiscountVo);
	
	/**
	 * 获取店铺促销活动分页列表
	 * @param keyword 搜索关键字、
	 * @param store_id 店铺ID
	 * @param pageNo 页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	public Page getFullDiscountList(String keyword, Integer store_id, Integer pageNo, Integer pageSize);
	
	/**
	 * 删除满优惠销活动
	 * @param fd_id 活动ID
	 */
	public void delete(Integer fd_id);
	
}
