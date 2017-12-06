package com.enation.app.shop.promotion.halfprice.service;


import java.util.List;

import com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.framework.database.Page;


/**
 * 第二件半价促销接口
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月20日 下午5:05:23
 */
public interface IHalfPriceManager{
	
	/**
	 * 获取店铺促销活动分页列表
	 * @param keyword 搜索关键字、
	 * @param store_id 店铺ID
	 * @param pageNo 页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	public Page getHalfPriceList(String keyword, Integer store_id, Integer pageNo, Integer pageSize);
	
	/**
	 * 添加第二次半价促销活动
	 * @param halfPriceVo 第二次半价促销vo实体
	 */
	public void add(HalfPriceVo halfPriceVo);
	

	/**
	 * 修改第二次半价促销活动
	 * @param halfPriceVo 第二次半价促销vo实体
	 */
	public void edit(HalfPriceVo halfPriceVo);
	
	/**
	 * 根据第二件半价活动ID获取第二件半价活动
	 * @param hp_id 第二件半价活动ID
	 * @return 第二件半价活动实体
	 */
	public HalfPriceVo get(Integer hp_id);
	
	/**
	 * 删除第二件半价促销活动
	 * @param activity_id 活动ID
	 */
	public void delete(Integer activity_id);

	/**
	 * 查询添加和修改的活动与正在进行的活动冲突
	 * @param halfPriceVo 第二次半价促销vo实体
	 * @param shop_id 店铺ID
	 * @return 返回封装的冲突商品的信息
	 */
	public List<PromotionGoodsVo> HalfMap(HalfPriceVo halfPriceVo);

	/**
	 * 获取当前状态
	 * @param start_time 活动开始时间
	 * @param end_time 活动结束时间
	 * @return
	 */
	public String getCurrentStatus(Long start_time, Long end_time);
	
	/**
	 * 根据第二件半价活动ID获取第二件半价活动
	 * @param hp_id 第二件半价活动ID
	 * @return 第二件半价活动实体
	 */
	public HalfPriceVo getFromDB(Integer hp_id);

	
}