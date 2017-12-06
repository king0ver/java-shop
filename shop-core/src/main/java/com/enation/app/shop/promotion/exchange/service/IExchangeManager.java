package com.enation.app.shop.promotion.exchange.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.promotion.exchange.model.po.Exchange;
import com.enation.framework.database.Page;

/**
 * 积分兑换管理接口
 * 
 * @author kingapex 修改 jianghongyan 修改 yanlin yanlin
 * @date 2016-06-20 2017-8-23 2017-9-7
 * @version v1.0.0 saveSetting() 方法修改参数
 * @since v6.1 v6.4
 */
public interface IExchangeManager {

	/**
	 * 获取某个商品的兑换设置
	 * 
	 * @param goodsid
	 *            商品id
	 * @return 兑换设置
	 */
	public Exchange getSetting(int goodsid);

	/**
	 * 添加积分换购商品
	 * 
	 * @param goodsId
	 * @param exchange
	 */
	public void add(Integer goodsId, Exchange exchange);

	/**
	 * 修改某个商品的兑换设置
	 * 
	 * @param goodsId
	 *            要保存的设置的商品id
	 */
	public void edit(Integer goodsId, Exchange exchange);

	/**
	 * 根据商品id删除积分换购信息
	 * 
	 * @param goodsId
	 */
	public void delete(Integer goodsId);

	/**
	 * 获取全部积分商品
	 * 
	 * @return 集合
	 */
	public List getExchangeGoodsList();

	/**
	 * 获取积分商品
	 * 
	 * @param catid
	 *            分类id
	 * @param tagid
	 *            标签id
	 * @param goodsnum
	 *            商品数量
	 * @return 集合
	 */
	public List listGoods(String catid, String tagid, String goodsnum);

	/**
	 * 获取用了展示的积分商品数据
	 * 
	 * @param goodsId
	 *            商品id
	 * @param active_id
	 *            活动id
	 * @return Exchange模型
	 */
	public Exchange getSettingToShow(int goodsId, Integer active_id);

	/**
	 * 商品搜索
	 * 
	 * @param goodsMap
	 *            搜索参数
	 * @param page
	 *            分页
	 * @param pageSize
	 *            分页每页数量
	 * @param other
	 *            其他
	 * @return Page
	 */
	public Page searchGoods(Map goodsMap, int page, int pageSize, String other, String sort, String order);

	/**
	 * 查询积分换购缓存，先查缓存，缓存是空查库。
	 * 
	 * @param activity_id
	 * @return
	 */
	public Exchange get(Integer activity_id, Integer seller_id);

}
