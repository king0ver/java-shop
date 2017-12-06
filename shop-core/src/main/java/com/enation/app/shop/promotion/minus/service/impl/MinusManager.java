package com.enation.app.shop.promotion.minus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.promotion.minus.model.po.Minus;
import com.enation.app.shop.promotion.minus.model.vo.MinusVo;
import com.enation.app.shop.promotion.minus.service.IMinusManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Service
public class MinusManager implements IMinusManager {

	@Autowired
	private ISellerManager sellerManager;

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private ICache cache;

	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;

	private PromotionServiceConstant promotionServiceConstant;

	@Autowired
	private IGoodsQueryManager goodsQueryManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.promotion.minus.service.IMinusManager#list()
	 */
	@Override
	public List list() {
		String sql = "SELECT * FROM es_minus WHERE shop_id = ?";
		return this.daoSupport.queryForList(sql, sellerManager.getSeller().getStore_id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.minus.service.IMinusManager#listJson(java.
	 * lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page listJson(String keyword, Integer shop_id, Integer pageNo, Integer pageSize) {

		String search = "";
		if (!StringUtil.isEmpty(keyword)) {
			search = " AND title like '%" + keyword + "%'";
		}
		String sql = "SELECT * FROM es_minus WHERE shop_id = ? " + search + " ORDER BY minus_id DESC ";

		return this.daoSupport.queryForPage(sql, pageNo, pageSize, shop_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.promotion.minus.service.IMinusManager#add(com.
	 * enation.app.shop.promotion.minus.model.vo.MinusVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(MinusVo minusVo) {
		// 获取当前登陆的店铺ID
		Integer shop_id = sellerManager.getSeller().getStore_id();

		List<Map> list = this.goodsQueryManager.getAllGoodsBySeller(shop_id);
		// 如果参与活动的是全部商品，商品列表goodsList为空，如果参与活动的是部分商品，商品列表缺少部分字段
		List<PromotionGoodsVo> goodsList = new ArrayList();
		if (minusVo.getRange_type() == 1) {
			for (Map map : list) {
				PromotionGoodsVo promotionGoodsVo = new PromotionGoodsVo();
				promotionGoodsVo.setGoods_id((Integer) map.get("goods_id"));
				promotionGoodsVo.setProduct_id((Integer) map.get("sku_id"));
				promotionGoodsVo.setStart_time(minusVo.getStart_time());
				promotionGoodsVo.setEnd_time(minusVo.getEnd_time());
				promotionGoodsVo.setTitle(minusVo.getTitle());
				goodsList.add(promotionGoodsVo);
			}
		} else if (minusVo.getRange_type() == 2) {
			goodsList = minusVo.getGoodsList();
			for (PromotionGoodsVo promotionGoodsVo : goodsList) {
				for (Map map : list) {
					if (promotionGoodsVo.getGoods_id().equals((Integer) map.get("goods_id"))) {
						promotionGoodsVo.setProduct_id((Integer) map.get("sku_id"));
						promotionGoodsVo.setStart_time(minusVo.getStart_time());
						promotionGoodsVo.setEnd_time(minusVo.getEnd_time());
					}
				}
			}
		}

		// 填充minus
		Minus minus = new Minus();
		minusVo.setDisabled(1);
		minusVo.setShop_id(shop_id);
		BeanUtils.copyProperties(minusVo, minus);
		minus.setSingle_reduction_value(minusVo.getMinus_price());

		
		// 向数据库插入minus
		this.daoSupport.insert("es_minus", minus);

		// 获取活动Id
		Integer minus_id = this.daoSupport.getLastId("es_minus");
		minusVo.setMinus_id(minus_id);

		for (PromotionGoodsVo promotionGoodsVo : goodsList) {
			promotionGoodsVo.setActivity_id(minus_id);
			promotionGoodsVo.setPromotion_type(PromotionTypeEnum.MINUS.getType());
		}

		// 向es_promotion_goods表中插入活动商品
		this.promotionGoodsManager.add(goodsList);

		String key = promotionServiceConstant.getMinusKey(minus_id);

		// 压入缓存
		cache.put(key, minusVo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.promotion.minus.service.IMinusManager#edit(com.
	 * enation.app.shop.promotion.minus.model.vo.MinusVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(MinusVo minusVo) {
		// 获取当前登陆的店铺ID
		Integer shop_id = this.sellerManager.getSeller().getStore_id();

		Integer minusId = minusVo.getMinus_id();
		long start_time = minusVo.getStart_time();
		long end_time = minusVo.getEnd_time();

		// 前台选择全部商品，goodsList为空，此处填充goodsList，选择部分商品，缺少部分字段
		List<PromotionGoodsVo> goodsList = new ArrayList();
		List<Map> list = this.goodsQueryManager.getAllGoodsBySeller(shop_id);
		if (minusVo.getRange_type() == 1) {
			for (Map map : list) {
				PromotionGoodsVo promotionGoodsVo = new PromotionGoodsVo();
				promotionGoodsVo.setGoods_id((Integer) map.get("goods_id"));
				promotionGoodsVo.setProduct_id((Integer) map.get("sku_id"));
				promotionGoodsVo.setStart_time(start_time);
				promotionGoodsVo.setEnd_time(end_time);
				promotionGoodsVo.setTitle(minusVo.getTitle());
				promotionGoodsVo.setActivity_id(minusId);
				promotionGoodsVo.setPromotion_type(PromotionTypeEnum.MINUS.getType());
				goodsList.add(promotionGoodsVo);
			}

		} else if (minusVo.getRange_type() == 2) {
			goodsList = minusVo.getGoodsList();
			for (PromotionGoodsVo promotionGoodsVo : goodsList) {
				for (Map map : list) {
					if (promotionGoodsVo.getGoods_id() == (Integer) map.get("goods_id")) {
						promotionGoodsVo.setProduct_id((Integer) map.get("sku_id"));
					}
					promotionGoodsVo.setStart_time(start_time);
					promotionGoodsVo.setEnd_time(end_time);
					promotionGoodsVo.setActivity_id(minusId);
					promotionGoodsVo.setPromotion_type(PromotionTypeEnum.MINUS.getType());
				}
			}
		}

		// 填充minus
		Minus minus = new Minus();
		BeanUtils.copyProperties(minusVo, minus);
		minus.setSingle_reduction_value(minusVo.getMinus_price());
		this.daoSupport.update("es_minus", minus, "minus_id=" + minusId);

		// 更新es_promotion_goods表活动商品
		this.promotionGoodsManager.edit(goodsList, minusId, PromotionTypeEnum.MINUS.getType());

		// 获取redis的key
		String key = promotionServiceConstant.getMinusKey(minusVo.getMinus_id());

		// 重新压入缓存
		cache.put(key, minusVo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.minus.service.IMinusManager#get(java.lang.
	 * Integer)
	 */
	@Override
	public MinusVo get(Integer minusId) {
		MinusVo minusVo = null;

		// 在redis中获取活动对象
		String key = promotionServiceConstant.getMinusKey(minusId);
		minusVo = (MinusVo) cache.get(key);

		// 数据库中获取活动对象，不包含商品列表
		if (minusVo == null) {
			String minus_sql = "SELECT * FROM es_minus WHERE minus_id = ? ";
			String goods = "SELECT * FROM es_promotion_goods WHERE activity_id = ? AND promotion_type = ? ";
			List<Map> list = this.daoSupport.queryForList(goods, minusId, PromotionTypeEnum.MINUS.getType());
			Minus minus = this.daoSupport.queryForObject(minus_sql, Minus.class, minusId);
			List<PromotionGoodsVo> goodsList = new ArrayList<>();
			for (Map map : list) {
				PromotionGoodsVo promotionGoodsVo = new PromotionGoodsVo();
				promotionGoodsVo.setActivity_id((Integer) map.get("activity_id"));
				promotionGoodsVo.setEnd_time((long) map.get("end_time"));
				promotionGoodsVo.setGoods_id((Integer) map.get("goods_id"));
				promotionGoodsVo.setProduct_id((Integer) map.get("sku_id"));
				promotionGoodsVo.setPromotion_type((String) map.get("promotion_type"));
				promotionGoodsVo.setTitle((String) map.get("title"));
				promotionGoodsVo.setStart_time((Long) map.get("start_time"));
				goodsList.add(promotionGoodsVo);
			}
			minusVo = new MinusVo();
			BeanUtils.copyProperties(minus, minusVo);
			minusVo.setMinus_price(minus.getSingle_reduction_value());
			minusVo.setGoodsList(goodsList);
			cache.put(key, minusVo);
			return minusVo;
		}

		return minusVo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.minus.service.IMinusManager#delete(java.
	 * lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer minus_id) {

		// 删除数据库中活动
		String deleteFromMinus = "DELETE FROM es_minus WHERE minus_id = " + minus_id;
		this.daoSupport.execute(deleteFromMinus);
		this.promotionGoodsManager.delete(minus_id, PromotionTypeEnum.MINUS.getType());

		// 删除redis中活动

		String key = promotionServiceConstant.getMinusKey(minus_id);
		this.cache.remove(key);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.minus.service.IMinusManager#isOutOfDate(
	 * java.lang.Integer)
	 */
	@Override
	public boolean isOutOfDate(Integer minusId) {
		Integer shop_id = sellerManager.getSeller().getStore_id();

		// 获取系统时间
		long date = (long) CurrencyUtil.div(System.currentTimeMillis(), 1000);

		String key = promotionServiceConstant.getMinusKey(minusId);
		MinusVo minusVo = (MinusVo) cache.get(key);
		long start_time = minusVo.getStart_time();
		long end_time = minusVo.getEnd_time();

		// 判断是否正在进行中
		if (start_time <= date & end_time >= date) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.minus.service.IMinusManager#isRepetition(
	 * java.util.List, java.sql.Timestamp, java.sql.Timestamp)
	 */
	@Override
	public List isRepetition(MinusVo minusVo, Long start_time, Long end_time) {

		// 获取商品列表
		List<PromotionGoodsVo> list = minusVo.getGoodsList();

		// 如果商品list为空，且活动商品为全部商品，则查询本店铺所有商品
		if (list.size() == 0 && minusVo.getRange_type() == 1) {
			Integer shop_id = this.sellerManager.getSeller().getStore_id();
			List<Map> allGoods = this.goodsQueryManager.getAllGoodsBySeller(shop_id);
			for (Map map : allGoods) {
				PromotionGoodsVo promotionGoodsVo = new PromotionGoodsVo();
				promotionGoodsVo.setGoods_id((Integer) map.get("goods_id"));
				promotionGoodsVo.setName((String) map.get("goods_name"));
				promotionGoodsVo.setThumbnail((String) map.get("thumbnail"));
				list.add(promotionGoodsVo);
			}
		}

		// 获取商品id的数组
		Integer[] goodsIds = new Integer[list.size()];
		int i = 0;
		for (PromotionGoodsVo promotionGoodsVo : list) {
			goodsIds[i] = promotionGoodsVo.getGoods_id();
			i++;
		}

		List<Object> goodsTerm = new ArrayList();
		String[] goods_ids = new String[goodsIds.length];
		for (int j = 0; j < goodsIds.length; j++) {
			goods_ids[j] = "?";
			goodsTerm.add(goodsIds[j]);
		}

		String goods_ids_str = StringUtil.arrayToString(goods_ids, ",");

		String sql = "SELECT pg.`goods_id`,pg.`start_time`,pg.`end_time`,pg.`title`,m.`start_time_str`,m.`end_time_str` FROM es_promotion_goods pg LEFT JOIN es_minus m ON pg.`activity_id` = m.`minus_id` WHERE pg.`goods_id` IN("
				+ goods_ids_str + ") AND pg.`promotion_type` = '" + PromotionTypeEnum.MINUS.getType() + "'";

		// 获取活动商品表中数据
		List<Map<String, Object>> activityList = this.daoSupport.queryForList(sql, goodsTerm.toArray());

		List requestList = new ArrayList();
		for (PromotionGoodsVo promotionGoodsVo : list) {
			for (Map<String, Object> map : activityList) {
				if (promotionGoodsVo.getGoods_id().equals(map.get("goods_id"))) {
					if (!(start_time >= (long) map.get("end_time") || end_time <= (long) map.get("start_time"))) {
						Map resultMap = new HashMap();
						resultMap.put("thumbnail", promotionGoodsVo.getThumbnail());
						resultMap.put("name", promotionGoodsVo.getName());
						resultMap.put("start_time_str", map.get("start_time_str"));
						resultMap.put("end_time_str", map.get("end_time_str"));
						resultMap.put("title", map.get("title"));
						requestList.add(resultMap);
					}
				}
			}
		}

		// 返回有冲突的商品列表
		return requestList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.minus.service.IMinusManager#checkTime(com.
	 * enation.app.shop.promotion.minus.model.vo.MinusVo)
	 */
	@Override
	public boolean checkTime(MinusVo minusVo) {

		// 查询过去的活动时间
		MinusVo oldMinus = this.get(minusVo.getMinus_id());

		Long old_start_time = DateUtil.getDateline(oldMinus.getStart_time_str(), "yyyy-MM-dd HH:mm:ss");
		Long old_end_time = DateUtil.getDateline(oldMinus.getEnd_time_str(), "yyyy-MM-dd HH:mm:ss");
		// 获取页面的活动时间
		Long start_time = DateUtil.getDateline(minusVo.getStart_time_str(), "yyyy-MM-dd HH:mm:ss");
		Long end_time = DateUtil.getDateline(minusVo.getEnd_time_str(), "yyyy-MM-dd HH:mm:ss");

		// 对比，开始时间不能大于过去的开始时间，结束时间不能小于过去的结束时间
		if (start_time > old_start_time && start_time < old_end_time
				|| end_time > old_start_time && end_time < old_end_time) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.minus.service.IMinusManager#isAfterTime(
	 * com.enation.app.shop.promotion.minus.model.vo.MinusVo)
	 */
	@Override
	public boolean isAfterTime(Integer minus_id) {

		Integer shop_id = sellerManager.getSeller().getStore_id();
		// 获取系统时间
		long date = (long) CurrencyUtil.div(System.currentTimeMillis(), 1000);

		String key = promotionServiceConstant.getMinusKey(minus_id);
		MinusVo minusVo = (MinusVo) cache.get(key);
		long end_time = minusVo.getEnd_time();

		// 判断是否正在进行中
		if (end_time < date) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public MinusVo getFromDB(Integer minusId) {
		String sql = "select * from es_minus where minus_id = ?";
		MinusVo minusVo = this.daoSupport.queryForObject(sql, MinusVo.class, minusId);
		return minusVo;
	}

}
