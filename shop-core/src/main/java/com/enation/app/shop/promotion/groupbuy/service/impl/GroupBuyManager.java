package com.enation.app.shop.promotion.groupbuy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.enation.app.shop.goods.service.IGoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.upload.UploadFactory;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.service.IGoodsGalleryManager;
import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuy;
import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuyActive;
import com.enation.app.shop.promotion.groupbuy.model.vo.GroupBuyVo;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyActiveManager;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.cache.ICache;
import com.enation.framework.component.ComponentView;
import com.enation.framework.component.IComponentManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 团购管理类
 * 
 * @author Chopper
 * @version v1.0
 * @since v6.4 2017年8月29日 下午8:54:17
 *
 */
@Service
public class GroupBuyManager implements IGroupBuyManager {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private IGoodsGalleryManager goodsGalleryManager;

	@Autowired
	private IGroupBuyActiveManager groupBuyActiveManager;

	@Autowired
	private IComponentManager componentManager;

	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;
	@Autowired
	private IGoodsManager goodsManager;
	@Autowired
	private ICache cache;

	@Autowired
	private UploadFactory uploadFactory;

	PromotionServiceConstant constant = new PromotionServiceConstant();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#add(com.
	 * enation.app.shop.component.groupbuy.model.GroupBuy)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(GroupBuy groupBuy) {

		groupBuy.setAdd_time(DateUtil.getDateline());
		this.daoSupport.insert("es_groupbuy_goods", groupBuy);
		groupBuy.setGb_id(this.daoSupport.getLastId("es_groupbuy_goods"));

		return groupBuy.getGb_id();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#update(
	 * com.enation.app.shop.component.groupbuy.model.GroupBuy)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(GroupBuy groupBuy) {
		this.daoSupport.update("es_groupbuy_goods", groupBuy, "gb_id=" + groupBuy.getGb_id());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#delete(
	 * int)
	 */
	@Override
	public void delete(int gbid) {
		GroupBuy gb = this.get(gbid);
		String sql = "delete from  es_groupbuy_goods where gb_id=?";
		this.daoSupport.execute(sql, gbid);
		this.daoSupport.execute(
				"update es_groupbuy_active set goods_num=goods_num-1 where act_id=(select act_id from es_groupbuy_goods where gb_id=? and gb_status=1)",
				gbid);
		
		
		sql = "delete from  es_groupbuy_goods where gb_id=?";
		// 移除缓存
		cache.remove(constant.getGroupbuyKey(gb.getStore_id(), gb.getGb_id()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#auth(
	 * int, int)
	 */
	@Override
	public void auth(int gbid, int status) {
		String sql = "update es_groupbuy_goods set gb_status=? where gb_id=?";
		this.daoSupport.execute(sql, status, gbid);
		// 获取审核的团购
		// 更改商品为团购商品
		GroupBuy groupBuy = (GroupBuy) this.daoSupport.queryForObject("select * from es_groupbuy_goods where gb_id=?",
				GroupBuy.class, gbid);

		if (status == 1) {
			// 修改团购活动添加已经参与团购商品
			this.daoSupport.execute("update es_groupbuy_active set goods_num=goods_num+1 where act_id=?",
					groupBuy.getAct_id());

			this.addPromotionGoods(groupBuy);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#
	 * listByActId(int, int, int, java.lang.Integer)
	 */
	@Override
	public Page listByActId(int page, int pageSize, int actid, Integer status) {
		// TODO 应该操作缓存

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select g.*,a.act_name,a.start_time,a.end_time,s.store_name from es_groupbuy_goods g ,es_groupbuy_active a ,es_store s");
		sql.append(" where g.act_id= ? and  g.act_id= a.act_id and s.store_id = g.store_id");
		if (status != null) {
			sql.append(" and g.gb_status=" + status);
		}
		sql.append(" order by g.add_time ");
		return this.daoSupport.queryForPage(sql.toString(), page, pageSize, actid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#
	 * listByActId(int, int, int, java.lang.Integer)
	 */
	@Override
	public Page list(int page, int pageSize, int actid, Integer status, String store_name, String keyword,String goods_name,String start_time,String end_time,String title) {

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select g.*,a.act_name,a.start_time,a.end_time,s.shop_name from es_groupbuy_goods g ,es_groupbuy_active a ,es_shop s");
		sql.append(" where g.act_id= " + actid + " and  g.act_id= a.act_id and s.shop_id = g.store_id");
		// 详细搜索
		if (StringUtil.isEmpty(keyword)) {
			if (!StringUtil.isEmpty(store_name)) {
				sql.append(" and  s.shop_name like(\"%" + store_name + "%\") " );
			}
			if (status != null) {
				if (status != -1) {
					sql.append(" and g.gb_status = " + status);
				}
			}
			if(!StringUtil.isEmpty(goods_name)) {
				sql.append(" and g.goods_name like (\"%" + goods_name + "%\") ");
			} 
			if(!StringUtil.isEmpty(title)) {
				sql.append(" and g.gb_name like (\"%" + title + "%\") ");
			} 
			if(!StringUtil.isEmpty(start_time)) {
				sql.append(" and g.add_time > "+DateUtil.getDateline(start_time));
			}
			if(!StringUtil.isEmpty(end_time)) {
				sql.append(" and g.add_time < "+DateUtil.getDateline(end_time));
			}
			// 普通搜索
		} else {
			if (keyword != null) {
				sql.append(" and ( s.shop_name like(\"%" + keyword + "%\") or g.gb_name like (\"%" + keyword
						+ "%\") or g.goods_name like (\"%" + keyword + "%\"))");
			}
		}
		sql.append(" order by g.add_time ");
		return this.daoSupport.queryForPage(sql.toString(), page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#search(
	 * int, int, java.lang.Integer, java.lang.Double, java.lang.Double, int,
	 * int, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page search(int page, int pageSize, Integer catid, Double minprice, Double maxprice, Integer sort_key,
			Integer sort_type, Integer search_type, Integer area_id) {

		// TODO 需要优化，不能频繁访问数据库

		StringBuffer sql = new StringBuffer(
				"select gg.*,g.thumbnail AS g_thumbnail,g.price as ori_price from es_groupbuy_goods gg inner join es_goods g ON g.goods_id=gg.goods_id  where gg.gb_status=1 and g.disabled=0 and g.market_enable=1");
		if (catid != 0) {
			sql.append(" and gg.cat_id=" + catid);
		}

		if (minprice != null) {
			sql.append(" and gg.price>=" + minprice);
		}

		if (maxprice != null && maxprice != 0) {
			sql.append(" and gg.price<=" + maxprice);
		}

		if (sort_type == 0) {
			sql.append(" and gg.act_id in (select act_id from es_groupbuy_active where end_time <"
					+ DateUtil.getDateline() + ")");
		}

		//
		GroupBuyActive active = groupBuyActiveManager.get();
		int actid = 0;
		if (active != null) {
			actid = active.getAct_id();
		}
		if (sort_type == 1 && catid == 0) {
			sql.append(" and gg.act_id=" + actid);
		}

		if (sort_type == 1 && catid != 0) {
			sql.append(" and gg.act_id=" + actid);
		}
		if (sort_type == 2 && catid != 0) {
			sql.append(" and gg.act_id in (select act_id from es_groupbuy_active where start_time >"
					+ DateUtil.getDateline() + ")");
		}

		if (sort_type == 2 && catid == 0) {
			sql.append(" and gg.act_id in (select act_id from es_groupbuy_active where start_time >"
					+ DateUtil.getDateline() + ")");
		}
		if (area_id != 0) {
			sql.append(" and gg.area_id=" + area_id);
		}
		if (sort_key == 0) {
			sql.append(" order by gg.add_time ");
		}

		if (sort_key == 1) {
			sql.append(" order by gg.price ");
		}

		if (sort_key == 2) {
			sql.append(" order by gg.price/gg.original_price ");
		}

		if (sort_key == 3) {
			sql.append(" order by gg.buy_num + gg.visual_num DESC");
		}

		return this.daoSupport.queryForPage(sql.toString(), page, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#get(int)
	 */
	@Override
	public GroupBuy get(int gbid) {
		// TODO 缓存操作，这里频繁操作了数据库
		String sql = "select * from es_groupbuy_goods where gb_id=?";
		GroupBuy groupBuy = (GroupBuy) this.daoSupport.queryForObject(sql, GroupBuy.class, gbid);
		sql = "select * from es_goods where goods_id=?";

		Goods goods = (Goods) this.daoSupport.queryForObject(sql, Goods.class, groupBuy.getGoods_id());
		groupBuy.setGoods(goods);

		return groupBuy;
	}
	
	@Override
	public GroupBuyVo getVo(int gbid) {
		// TODO 缓存操作，这里频繁操作了数据库
		String sql = "select * from es_groupbuy_goods where gb_id=?";
		GroupBuyVo groupBuy = (GroupBuyVo) this.daoSupport.queryForObject(sql, GroupBuyVo.class, gbid);

		return groupBuy;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#
	 * getBuyGoodsId(int)
	 */
	@Override
	public GroupBuy getBuyGoodsId(int goodsId) {
		// TODO 缓存操作，这里频繁操作了数据库
		String sql = "SELECT * from es_groupbuy_goods WHERE goods_id=? and act_id=?";
		GroupBuy groupBuy = (GroupBuy) this.daoSupport.queryForObject(sql, GroupBuy.class, goodsId,
				groupBuyActiveManager.get().getAct_id());
		groupBuy.setGoods((Goods) this.daoSupport.queryForObject("select * from es_goods where goods_id=?", Goods.class,
				groupBuy.getGoods_id()));
		return groupBuy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IGroupBuyManager#getBuyGoodsList( int)
	 */
	@Override
	public List<GroupBuy> getBuyGoodsList(int goodsId) {
		// TODO 缓存操作，这里频繁操作了数据库
		String sql = "SELECT * from es_groupbuy_goods WHERE goods_id=? and act_id=?";
		List<GroupBuy> queryForList = this.daoSupport.queryForList(sql, GroupBuy.class, goodsId,
				groupBuyActiveManager.get().getAct_id());
		// groupBuy.setGoods((Goods)this.daoSupport.queryForObject("select *
		// from es_goods where goods_id=?", Goods.class,
		// groupBuy.getGoods_id()));
		return queryForList;
	}

	@Override
	public Page listByStoreId(int page, int pageSize, int storeid, Map params) {

		String gb_name = (String) params.get("gb_name");
		String gb_status = (String) params.get("gb_status");

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select g.*,a.act_name,a.start_time,a.end_time from es_groupbuy_goods g ,es_groupbuy_active a where g.store_id= ? and  g.act_id= a.act_id ");

		if (!StringUtil.isEmpty(gb_name)) {
			sql.append(" and g.gb_name like '%" + gb_name + "%'");
		}
		if (!StringUtil.isEmpty(gb_status)) {
			sql.append(" and g.gb_status=" + gb_status);
		}
		sql.append(" order by g.add_time ");

		return this.daoSupport.queryForPage(sql.toString(), page, pageSize, storeid);
	}

	@Override
	public GroupBuy getBuyGoodsId(int goodsId, int act_id) {
		String sql = "SELECT * from es_groupbuy_goods WHERE goods_id=? and gb_id=? ";
		GroupBuy groupBuy = (GroupBuy) this.daoSupport.queryForObject(sql, GroupBuy.class, goodsId, act_id);
		groupBuy.setGoods(goodsManager.getFromDB(groupBuy.getGoods_id()));
		return groupBuy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IGroupBuyManager#storeGoodsList( int, java.util.Map)
	 */
	@Override
	public List<Map> storeGoodsList(int storeid, Map map) {
		StringBuffer sql = new StringBuffer(
				"SELECT g.* from es_goods g where g.seller_id=" + storeid + " and  g.disabled=0");
		String store_catid = String.valueOf(map.get("store_catid"));
		String keyword = String.valueOf(map.get("keyword"));
		String act_id = String.valueOf(map.get("act_id"));
		if (!StringUtil.isEmpty(store_catid) && !"0".equals(store_catid)) { // 按店铺分类搜索
			sql.append(" and g.shop_cat_id=" + store_catid);
		}

		if (!StringUtil.isEmpty(keyword)) {
			sql.append(" and ((g.goods_name like '%" + keyword + "%') or (g.sn like '%" + keyword + "%') )");
		}
		if (!StringUtil.isEmpty(act_id)) {
			sql.append(" AND g.goods_id NOT IN(select goods_id from es_groupbuy_goods where act_id=" + act_id + ")");
		}
		/** 判断是否存在积分商品，过滤积分商品 */
		ComponentView exchangeIsOn = componentManager.getCmptView("积分兑换组件");
		// ComponentView b2b2cExchangeIsOn=
		// componentManager.getCmptView("多店积分兑换组件");
		if (exchangeIsOn != null) {
			if (exchangeIsOn.getEnable_state() == 1) {
				sql.append(
						" AND goods_id not in (select goods_id from es_exchange_setting where enable_exchange = 1  )  ");
			}
		}

		return this.daoSupport.queryForList(sql.toString());
	}

	/**
	 * 进行压缩
	 * 
	 * @param filepath
	 */
	private void process(GroupBuy groupbuy, String thumbnail, String wap_thumbnail) {
		int width = 800;
		int height = 800;

		int wap_width = 400;
		int wap_height = 400;
		String filepath = StaticResourcesUtil.convertToUrl(groupbuy.getImg_url());
		groupbuy.setThumbnail(thumbnail);
		groupbuy.setWap_thumbnail(wap_thumbnail);

	}

	/**
	 * 获取缩略图地址
	 * 
	 * @param filePath
	 *            图片原生地址
	 * @param shortName
	 *            替换名称
	 * @return
	 */
	private String getThumbPath(String filePath, String shortName) {
		return StaticResourcesUtil.getThumbPath(filePath, shortName);
	}




	/**
	 * 缓存到redis
	 * 
	 * @param groupBuy
	 */
	private void addPromotionGoods(GroupBuy groupBuy) {
		// 插入数据库 promotiongoods

		GroupBuyActive active = groupBuyActiveManager.get(groupBuy.getAct_id());

		List<PromotionGoodsVo> pgvs = new ArrayList<PromotionGoodsVo>();
		PromotionGoodsVo pgv = new PromotionGoodsVo();
		pgv.setActivity_id(groupBuy.getGb_id());
		pgv.setEnd_time(active.getEnd_time());
		pgv.setStart_time(active.getStart_time());
		pgv.setGoods_id(groupBuy.getGoods_id());
		pgv.setProduct_id(groupBuy.getProduct_id());
		pgv.setPromotion_type(PromotionTypeEnum.GROUPBUY.getType());
		pgv.setTitle(groupBuy.getGb_title());
		pgvs.add(pgv);
		promotionGoodsManager.add(pgvs);
		// 填充vo至redis
		String pg_key = constant.getGroupbuyKey(groupBuy.getStore_id(), groupBuy.getGb_id());
		GroupBuyVo gbv = new GroupBuyVo();
		gbv.setBuy_num(groupBuy.getBuy_num());
		gbv.setGa_id(groupBuy.getAct_id());
		gbv.setGb_id(groupBuy.getGb_id());
		gbv.setGoods_num(groupBuy.getGoods_num());
		gbv.setLimit_num(groupBuy.getLimit_num());
		gbv.setOriginal_price(groupBuy.getOriginal_price());
		gbv.setPrice(groupBuy.getPrice());
		cache.put(pg_key, gbv);
	}

}
