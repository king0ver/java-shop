package com.enation.app.shop.goods.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.po.GoodsSku;
import com.enation.app.shop.goods.model.vo.CacheSku;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.framework.database.Page;

/**
 * 
 * 商品sku
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 下午2:09:54
 */
public interface IGoodsSkuManager {

	/**
	 * 添加sku
	 * 
	 * @param productList
	 *            sku集合
	 */
	public List<GoodsSkuVo> add(List<GoodsSkuVo> skuList, Goods goods);

	/**
	 * 修改sku
	 * 
	 * @param productList
	 *            sku集合
	 */
	public List<GoodsSkuVo> edit(List<GoodsSkuVo> skuList, Goods goods, Integer has_changed);

	/**
	 * 删除sku
	 * 
	 * @param goods_id
	 *            商品id 组
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer[] goods_id);

	/**
	 * 从缓存中快速读取sku信息
	 * 
	 * @param sku_id
	 * @return
	 */
	public GoodsSkuVo getSkuFromCache(Integer sku_id);

	/**
	 * 查询上商品sku列表
	 * 
	 * @param goodsQueryParam
	 * @return
	 */
	public Page query(GoodsQueryParam goodsQueryParam);

	/**
	 * 从数据库中获取sku
	 * 
	 * @param goods_id
	 * @param sku_id
	 * @return
	 */
	public CacheSku getSkuFromDatabase(Integer goods_id, Integer sku_id);

	/**
	 * 从缓存中读取sku list
	 * 
	 * @param goods_id
	 * @return
	 */
	public List<CacheSku> getSkuListFromCache(Integer goods_id);

	/**
	 * 根据skuid查询集合
	 * 
	 * @param skuidAr
	 * @return
	 */
	public List<GoodsSku> query(Integer[] skuidAr);

	/**
	 * 从数据库中读取sku list， 为商品编辑时使用
	 * 
	 * @param goods_id
	 * @return
	 */
	public List<GoodsSkuVo> listByGoodsId(Integer goods_id);
}
