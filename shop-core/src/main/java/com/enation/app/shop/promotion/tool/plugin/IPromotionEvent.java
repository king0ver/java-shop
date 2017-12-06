package com.enation.app.shop.promotion.tool.plugin;

import com.enation.app.shop.trade.model.vo.Cart;

/**
 * 促销插件接口
 * 
 * @author mengyuanming    			yanlin
 * @version v1.0         			v1.1 修改onPriceProcess()方法参数的import
 * @since v6.4.0          			v6.4 
 * @date 2017年8月18日下午8:52:10     2017-8-24
 *
 */
public interface IPromotionEvent {
	/**
	 * 降价过程
	 * 
	 * @param 购物车
	 */
	public void onPriceProcess(Cart cart);

}
