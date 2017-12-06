package com.enation.app.shop.comments.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.comments.model.po.ShopScore;
import com.enation.app.shop.comments.service.IShopScoreManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 店铺动态评分管理类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月28日 下午5:28:00
 */
@Service("shopScoreManager")
public class ShopScoreManager implements IShopScoreManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public ShopScore add(ShopScore shopScore) {
		this.daoSupport.insert("es_shop_score", shopScore);
		return shopScore;
	}

	
	
}
