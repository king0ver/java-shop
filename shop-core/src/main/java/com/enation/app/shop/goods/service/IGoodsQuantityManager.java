package com.enation.app.shop.goods.service;

import java.util.List;

import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;

/**
 * 商品库存
 * 
 * @author fk
 * @version v1.0 2017年4月1日 下午12:01:39
 */
public interface IGoodsQuantityManager {

	/**
	 * 增加库存
	 * 
	 * @param goodsQuantity
	 * @return 如果增加成功返回真，否则返回假
	 */
	public boolean addGoodsQuantity(GoodsQuantityVo goodsQuantity);

	/**
	 * 扣减库存
	 * 
	 * @param goodsQuantity
	 * @return 如果扣减成功返回真，否则返回假
	 */
	public boolean reduceGoodsQuantity(GoodsQuantityVo goodsQuantity);

	/**
	 * 商家中心单独维护库存接口
	 * 
	 * @param goodsQuantity
	 */
	public void updateGoodsQuantity(List<GoodsQuantityVo> goodsQuantity);

}
