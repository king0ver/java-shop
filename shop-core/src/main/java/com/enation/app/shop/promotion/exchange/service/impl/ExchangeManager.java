package com.enation.app.shop.promotion.exchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.exchange.model.po.Exchange;
import com.enation.app.shop.promotion.exchange.model.po.ExchangeGoodsCategory;
import com.enation.app.shop.promotion.exchange.service.IExchangeManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 积分兑换管理
 * 
 * @author kingapex 2016年3月10日下午10:07:54
 * @version v1.0
 * @since v5.2
 */
@Service
public class ExchangeManager implements IExchangeManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ExchangeCategoryManager exchangeCatManager;
	@Autowired
	private ICache cache;
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.component.exchange.IExchangeManager#getSetting(int)
	 */
	@Override
	public Exchange getSetting(int goodsid) {

		String sql = "select * from es_exchange_setting where goods_id=?";
		List<Exchange> list = daoSupport.queryForList(sql, Exchange.class, goodsid);

		/**
		 * 试着由数据库获取此商品的配置 如果没有记录，则返空.
		 */
		Exchange setting = null;
		if (list.isEmpty()) {
			setting = null;

		} else {
			setting = list.get(0);
		}

		return setting;
	}

	@Override
	public void add(Integer goodsId, Exchange exchange) {
		if (StringUtil.isEmpty(goodsId.toString())) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "参数错误,请刷新页面后重新修改");
		}
		exchange.setGoods_id(goodsId);
		// 如果添加商品时积分换购被勾选
		if (exchange != null && exchange.getEnable_exchange() == 1) {
			if (exchange.getCategory_id() == null) {
				exchange.setCategory_id(0);
			}
			Seller storeMember = sellerManager.getSeller();
			exchange.setSetting_id(null);
			this.daoSupport.insert("es_exchange_setting", exchange);
			int activity_id = this.daoSupport.getLastId("es_exchange_setting");
			// 添加一个积分换购的促销时需要在商品对照表添加一条记录
			long currTime = DateUtil.getDateline();
			long endTime = DateUtil.endOfSomeDay(365);
			PromotionGoodsVo promotionGoods = new PromotionGoodsVo();
			promotionGoods.setGoods_id(goodsId);
			promotionGoods.setActivity_id(activity_id);
			promotionGoods.setPromotion_type(PromotionTypeEnum.EXCHANGE.getType());
			promotionGoods.setStart_time(currTime);
			promotionGoods.setEnd_time(endTime);
			promotionGoods.setTitle("积分兑换活动");
			List<PromotionGoodsVo> promotionGoodsVos = new ArrayList<>();
			promotionGoodsVos.add(promotionGoods);
			promotionGoodsManager.add(promotionGoodsVos);
			// 为了存redis中
			exchange.setGoodsList(promotionGoodsVos);
			cache.put(PromotionServiceConstant.getExchageKey(storeMember.getStore_id(), activity_id), exchange);
		}
	}

	@Override
	public void edit(Integer goodsId, Exchange exchange) {
		if (StringUtil.isEmpty(goodsId.toString())) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "参数错误,请刷新页面后重新修改");
		}

		exchange.setGoods_id(goodsId);
		if (exchange != null) {
			// 修改分两种情况，勾选和未勾选
			if (exchange.getEnable_exchange() == 1) {
				this.delete(goodsId);

				// 清除商品对照表信息和缓存
				cleanPromotionGoods(exchange);

				// 添加积分换购信息
				this.add(goodsId, exchange);
			} else {

				cleanPromotionGoods(exchange);

				this.delete(goodsId);
			}
		}
	}

	/**
	 * 清除商品对照表信息和缓存
	 * 
	 * @param exchange
	 */
	private void cleanPromotionGoods(Exchange exchange) {
		if (exchange.getSetting_id() != 0) {
			Seller seller = sellerManager.getSeller();
			promotionGoodsManager.delete(exchange.getSetting_id(), PromotionTypeEnum.EXCHANGE.getType());
			cache.remove(PromotionServiceConstant.getExchageKey(seller.getStore_id(), exchange.getSetting_id()));
		}
	}

	@Override
	public void delete(Integer goodsid) {
		String sql = "delete from es_exchange_setting where goods_id=?";
		this.daoSupport.execute(sql, goodsid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.exchange.service.IExchangeManager#get(java.
	 * lang.Integer)
	 */
	@Override
	public Exchange get(Integer activity_id, Integer seller_id) {
		Exchange exchange = (Exchange) cache.get(PromotionServiceConstant.getExchageKey(seller_id, activity_id));
		if (exchange == null) {
			String sql = "select * from es_exchange_setting where setting_id=?";
			Exchange exchanges = daoSupport.queryForObject(sql, Exchange.class, activity_id);
			cache.put(PromotionServiceConstant.getExchageKey(seller_id, activity_id), exchanges);
			return exchanges;
		}
		return exchange;

	}

	@Override
	public List getExchangeGoodsList() {
		String sql = "select * from es_exchange_setting";
		return this.daoSupport.queryForList(sql, Exchange.class);

	}

	@Override
	public List listGoods(String catid, String tag_id, String goodsnum) {
		int num = 10;
		if (!StringUtil.isEmpty(goodsnum)) {
			num = Integer.valueOf(goodsnum);
		}

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select g.* from es_tag_rel r LEFT JOIN es_goods g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");

		if (!StringUtil.isEmpty(catid)) {
			ExchangeGoodsCategory exchangeGoodsCat = this.exchangeCatManager.getByGoodId(Integer.valueOf(catid));
			if (exchangeGoodsCat != null) {
				String cat_path = exchangeGoodsCat.getCategory_path();
				if (cat_path != null) {
					sql.append(" and  g.cat_id in(");
					sql.append("select c.cat_id from es_goods_cat");
					sql.append(" c where c.cat_path like '" + cat_path + "%')");
				}
			}
		}

		if (!StringUtil.isEmpty(tag_id)) {
			sql.append(" AND r.tag_id=" + tag_id + "");
		}

		/*
		 * xin 修改为升序 2015-12-17
		 */
		sql.append(" order by r.ordernum asc");
		List list = this.daoSupport.queryForListPage(sql.toString(), 1, num);
		// this.exchangeGoodsDataFilterBundle.filterGoodsData(list);
		return list;
	}

	@Override
	public Exchange getSettingToShow(int goodsId, Integer active_id) {
		String sql = "select * from es_exchange_setting  e left join es_promotion_goods p on p.goods_id = e.goods_id where p.goods_id=? and e.enable_exchange=? and p.activity_id=?";
		List<Exchange> list = daoSupport.queryForList(sql, Exchange.class, goodsId, 1, active_id);

		/**
		 * 试着由数据库获取此商品的配置 如果没有记录，则返空.
		 */
		Exchange setting = null;
		if (list.isEmpty()) {
			setting = null;

		} else {
			setting = list.get(0);
		}

		return setting;
	}

	@Override
	public Page searchGoods(Map goodsMap, int page, int pageSize, String other, String sort, String order) {
		String sql = creatTempSql(goodsMap, other);

		StringBuffer _sql = new StringBuffer(sql);
		// this.goodsPluginBundle.onSearchFilter(_sql);

		if (StringUtil.isEmpty(sort)) {
			sort = " create_time";
		}

		if (StringUtil.isEmpty(order)) {
			order = " desc";
		}

		if (StringUtil.isEmpty(other)) {
			other = " ";
		}

		if (goodsMap.get("market_enable") != null && !StringUtil.isEmpty(goodsMap.get("market_enable").toString())) {
			_sql.append(" and market_enable=" + goodsMap.get("market_enable"));
		} else {
			_sql.append(" and market_enable!=2");
		}

		_sql.append(" order by " + sort + " " + order);
		Page webpage = this.daoSupport.queryForPage(_sql.toString(), page, pageSize);
		return webpage;
	}

	private String creatTempSql(Map goodsMap, String other) {

		other = other == null ? "" : other;
		String sql = "";
		Integer brandid = (Integer) goodsMap.get("brandid");
		Integer catid = (Integer) goodsMap.get("catid");
		String name = (String) goodsMap.get("name");
		String sn = (String) goodsMap.get("sn");
		Integer[] tagid = (Integer[]) goodsMap.get("tagid");
		Integer stype = (Integer) goodsMap.get("stype");
		String keyword = (String) goodsMap.get("keyword");
		String order = (String) goodsMap.get("order");
		Integer market_enable = (Integer) goodsMap.get("market_enable");

		if (brandid != null && brandid != 0) {
			sql += " and g.brand_id = " + brandid + " ";
		}

		if ("1".equals(other)) {
			// 商品属性为不支持打折的商品
			sql += " and g.no_discount=1";
		}
		if ("2".equals(other)) {
			// 特殊打折商品，即单独设置了会员价的商品
			sql += " and (select count(0) from es_goods_lv_price glp where glp.goodsid=g.goods_id) >0";
		}

		if (stype != null && keyword != null) {
			if (stype == 0) {
				sql += " and ( g.name like '%" + keyword + "%'";
				sql += " or g.sn like '%" + keyword + "%')";
			}
		}

		if (name != null && !name.equals("")) {
			name = name.trim();
			String[] keys = name.split("\\s");
			for (String key : keys) {
				sql += (" and g.name like '%");
				sql += (key);
				sql += ("%'");
			}
		}

		if (sn != null && !sn.equals("")) {
			sql += "   and g.sn like '%" + sn + "%'";
		}

		if (catid != null && catid != 0) {
			// Cat cat = this.goodsCatManager.getById(catid);
			// sql += " and g.cat_id in(";
			// sql += "select c.cat_id from es_goods_cat" + " c where c.cat_path like '" +
			// cat.getCat_path() + "%') ";
		}

		if (tagid != null && tagid.length > 0) {
			String tagidstr = StringUtil.arrayToString(tagid, ",");
			sql += " and g.goods_id in(select rel_id from es_tag_rel where tag_id in(" + tagidstr + "))";
		}

		if (market_enable != null && market_enable >= 0) {
			sql += " and market_enable=" + market_enable;
		}
		// System.out.println(sql);
		return sql;
	}

}
