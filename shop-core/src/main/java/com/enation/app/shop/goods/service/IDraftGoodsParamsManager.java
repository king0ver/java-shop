package com.enation.app.shop.goods.service;

import java.util.List;

import com.enation.app.shop.goods.model.po.GoodsParams;

/**
 * 
 * 商品草稿箱参数接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午9:44:04
 */
public interface IDraftGoodsParamsManager {
	/**
	 * 添加商品草稿箱关联的参数
	 * 
	 * @param goods_id
	 *            商品id
	 * @param paramList
	 *            参数集合
	 */
	public void addParams(List<GoodsParams> paramList, Integer goods_id);
}
