package com.enation.app.shop.goods.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.goods.model.vo.GoodsCache;
import com.enation.app.shop.goods.model.vo.GoodsLine;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.framework.database.Page;

/**
 * 
 * 查询商品接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 2017年8月14日 下午2:29:28
 */
public interface IGoodsQueryManager {

	/**
	 * 管理员查询商品
	 * 
	 * @param goodsQueryParam
	 *            商品查询条件
	 * @return
	 */
	public Page query(GoodsQueryParam goodsQueryParam);

	public List<GoodsLine> query(Integer[] goodsidAr);

	/**
	 * 查询所有商品给缓存使用
	 * 
	 * @return
	 */
	public List<GoodsCache> queryAllGoodsForCache();

	/**
	 * 查询商品和商品的参数
	 * 
	 * @param goods_ids
	 *            为空时，查询所有
	 * @return
	 */
	public List<Map<String, Object>> getGoodsAndParams(Integer[] goods_ids);

	/**
	 * 查询某商家的所有商品
	 * 
	 * @param seller_id
	 * @return
	 */
	public List<Map> getAllGoodsBySeller(Integer seller_id);

	/**
	 * 根据店铺ID获取本店铺的商品的商品id和货品ID列表
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return 本店铺的商品的商品id和货品ID列表
	 */
	public List<Map> getGoodsList(int shopId);

	/**
	 * 获取正在出售的商品数量
	 * @param seller_id
	 * @return
	 */
	public Integer getSellerGoodsCount(Integer seller_id);

	/**
	 * 获取待上架的商品数量
	 * @param seller_id
	 * @return
	 */
	public Integer getWaitShelvesGoodsCount(Integer seller_id);
}
