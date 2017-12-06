package com.enation.app.shop.goods.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.vo.GoodsSkuVo;

/**
 * 
 * 草稿箱商品sku操作接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午9:48:11
 */
public interface IDraftGoodsSkuManager {
	/**
	 * 添加sku
	 * 
	 * @param productList
	 *            sku集合
	 */
	public List<GoodsSkuVo> add(List<GoodsSkuVo> skuList, Integer goodsid);

	/**
	 * 修改sku
	 * 
	 * @param productList
	 *            sku集合
	 */
	public List<GoodsSkuVo> edit(List<GoodsSkuVo> skuList, Integer goodsid, Integer has_changed);

	/**
	 * 从数据库中读取sku list， 为商品编辑时使用
	 * 
	 * @param goods_id
	 * @return
	 */
	public List<GoodsSkuVo> listByGoodsId(Integer goods_id);
}
