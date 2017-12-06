package com.enation.app.core.event;

/**
 * 店铺变更
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:18:50
 */
public interface IShopChangeEvent {

	/**
	 * 店铺变更的消费
	 * @param shop_id
	 */
	public void shopChange(Integer shop_id);
}
