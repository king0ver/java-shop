package com.enation.app.shop.comments.service;

import com.enation.app.shop.comments.model.po.ShopScore;

/**
 * 店铺动态评分管理类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午5:25:31
 */
public interface IShopScoreManager {

	/**
	 * 增加店铺动态评分
	 * @param shopScore
	 * @return
	 */
	public ShopScore add(ShopScore shopScore);
}
