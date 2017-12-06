package com.enation.app.shop.goods.service;

import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.framework.database.Page;

/**
 * 
 * 商品审核接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月29日 下午5:30:38
 */
public interface IGoodsAuthManager {
	/**
	 * 获取需要审核的商品
	 * 
	 * @return
	 */
	public Page getList(GoodsQueryParam goodsQueryParam);

	/**
	 * 审核商品
	 * 
	 * @param goods_id
	 *            商品id
	 * @param pass
	 *            审核状态
	 * @param message
	 *            未审核原因
	 */
	public void authGoods(Integer goods_id, Integer pass, String message);
}
