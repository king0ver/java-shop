package com.enation.app.shop.promotion.exchange.service;

import java.util.List;

import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.framework.database.Page;

/**
 * 
 * 积分商品操作
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月26日 下午4:12:33
 */
public interface IExchangeGoodsManager {

	/**
	 * 后台查看积分商品列表
	 *
	 * @param goodsQueryParam
	 * @return
	 */
	public Page list(GoodsQueryParam goodsQueryParam);

	/**
	 * 更新排序
	 * 
	 * @param ids
	 * @param sorts
	 */
	public void updateSort(Integer[] goods_id, Integer[] sord);

	/**
	 * 前台输出积分商品可以分页,可以按分类查询
	 * 
	 * @return
	 */
	public Page frontList(Integer pageNo, Integer pageSize, Integer cat_id);

}
