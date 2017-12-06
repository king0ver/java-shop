package com.enation.app.shop.promotion.tool.service;

import java.util.List;

import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;

/**
 * 活动商品对照表
 * @author xulipeng
 * @since v6.4
 * @version  v1.0
 */
public interface IPromotionGoodsManager {

	/**
	 * 添加活动商品对照表
	 * @param list
	 */
	public void add(List<PromotionGoodsVo> list);
	
	
	/**
	 * 修改活动商品对照表
	 * @param list	
	 * @param activity_id
	 * @param promotion_type
	 */
	public void edit(List<PromotionGoodsVo> list,Integer activity_id,String promotion_type);
	
	
	/**
	 * 根据活动id和活动工具类型删除活动商品对照表
	 * @param activity_id
	 * @param promotion_type
	 */
	public void delete(Integer activity_id,String promotion_type);
	
	/**
	 * 根据商品id读取商品参与的所有活动（有效的活动）
	 * @param goods_id
	 * @return 返回活动的集合
	 */
	public List<PromotionGoodsVo> getPromotion(Integer goods_id);
	
}
