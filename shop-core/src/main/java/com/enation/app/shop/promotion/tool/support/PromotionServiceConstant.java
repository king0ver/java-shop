package com.enation.app.shop.promotion.tool.support;

import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;

/**
 * 促销活动缓存
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月18日17:58:12
 */
public class PromotionServiceConstant {
	
	public static final String STORE_ID_PREFIX="store_";
	
	public static PromotionTypeEnum promotionTypeEnum;
	
	/**
	 * 读取积分换购redis key
	 * @param store_id
	 * @param activity_id
	 * @return
	 */
	public static final String getExchageKey(Integer store_id,Integer activity_id){
		String key = STORE_ID_PREFIX+store_id+promotionTypeEnum.EXCHANGE.getType()+activity_id;
		return key;
	}
	
	/**
	 * 读取满优惠redis key
	 * @param store_id
	 * @param activity_id
	 * @return
	 */
	public static final String getFullDiscountKey(Integer activity_id){
		String key = STORE_ID_PREFIX+"_"+promotionTypeEnum.FULLDISCOUNT.getType()+activity_id;
		return key;
	}
	
	/**
	 * 读取团购活动redis key
	 * @param store_id
	 * @param activity_id
	 * @return
	 */
	public static final String getGroupbuyKey(Integer store_id,Integer activity_id){
		String key = STORE_ID_PREFIX+store_id+promotionTypeEnum.GROUPBUY.getType()+activity_id;
		return key;
	}
	
	/**
	 * 读取第二件半价活动redis key
	 * @param store_id
	 * @param activity_id
	 * @return
	 */
	public static final String getHalfPriceKey(Integer activity_id){
		String key = STORE_ID_PREFIX+"_"+promotionTypeEnum.HALFPRICE.getType()+activity_id;
		return key;
	}
	
	/**
	 * 读取单品立减活动 redis key
	 * @param store_id
	 * @param activity_id
	 * @return
	 */
	public static final String getMinusKey(Integer activity_id){
		String key = STORE_ID_PREFIX+"_"+promotionTypeEnum.MINUS.getType()+activity_id;
		return key;
	}
	
	
	
}
