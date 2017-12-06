package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.enation.app.shop.goods.service.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.goods.model.enums.GoodsCacheKey;
import com.enation.app.shop.goods.model.enums.GoodsType;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.model.vo.ShopCat;
import com.enation.app.shop.promotion.exchange.service.IExchangeManager;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.goods.service.IStoreGoodsCatManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.shop.setting.model.po.StoreSetting;
import com.enation.app.shop.statistics.service.IFlowStatisticsManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.jms.support.goods.GoodsChangeMsg;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 商品维护manager
 *
 * @author yanlin
 * @version v1.0
 * @date 2017年9月7日 下午2:30:10
 * @since v6.4.0
 */
@Service
public class GoodsManager implements IGoodsManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IGoodsParamsManager goodsParamsManager;
	@Autowired
	private IGoodsSkuManager goodsSkuManager;
	@Autowired
	private IGoodsGalleryManager goodsGalleryManager;
	@Autowired
	private ISellerManager sellerMangager;
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private IExchangeManager exchangeManager;
	@Autowired
	private ICache cache;
	@Autowired
	private IFlowStatisticsManager flowStatisticsManager;
	@Autowired
	private IStoreGoodsCatManager storeGoodsCatManager;
	@Autowired
	private IShopManager shopManager;
	@Autowired
	private ICategoryManager categoryManager;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.enation.javashop.manager.IGoodsManager#get(java.lang.Integer)
	 */
	@Override
	public GoodsVo getFromCache(Integer goods_id) {
		GoodsVo goods = (GoodsVo) cache.get(GoodsCacheKey.GOODS.name() + goods_id);
		if (goods == null) {
			String sql = "select * from es_goods where goods_id = ?";
			Goods goodsDB = this.daoSupport.queryForObject(sql, Goods.class, goods_id);
			if (goodsDB == null) {
				throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "商品不存在");
			}
			// GoodsVo的对象返回,GoodsVo中的skuList是要必须填充好的
			List<GoodsSkuVo> listByGoodsId = goodsSkuManager.listByGoodsId(goods_id);
			GoodsVo goodsVo = new GoodsVo(goodsDB);
			goodsVo.setSkuList(listByGoodsId);
			cache.put(GoodsCacheKey.GOODS.name() + goods_id, goodsVo);
			return goodsVo;
		}
		return goods;
	}

	/**
	 * 创建商品编号
	 *
	 * @param goods
	 * @return
	 */
	private String createSn() {
		String sn = "G" + DateUtil.toString(new Date(System.currentTimeMillis()), "yyyyMMddhhmmss")
		+ StringUtil.getRandStr(4);
		return sn;
	}

	protected void proessPhoto(String[] picnames, Goods goods, String image_default) {
		if (picnames == null) {
			return;
		}

		// 生成相册列表，待jsm处理器使用
		List<GoodsGallery> galleryList = new ArrayList<GoodsGallery>();

		for (int i = 0; i < picnames.length; i++) {
			GoodsGallery gallery = new GoodsGallery();

			String filepath = picnames[i];
			gallery.setOriginal(filepath); // 相册原始路径
			gallery.setBig(filepath);
			gallery.setSmall(filepath);
			gallery.setThumbnail(filepath);
			gallery.setTiny(filepath);
			galleryList.add(gallery);

			// 设置为默认图片
			if (!StringUtil.isEmpty(image_default) && image_default.equals(filepath)) {
				gallery.setIsdefault(1);
			}
		}
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("galleryList", galleryList);
		param.put("goods", goods);

		// goodsGalleryManager.addGoodsGalleryList(galleryList, goods);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#add(com.enation.app.shop.
	 * goods.model.vo.GoodsVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(GoodsVo goodsVo) throws Exception {
		Seller seller = sellerMangager.getSeller();
		goodsVo.setSeller_id(seller.getStore_id());
		// 添加前判断
		// 创建商品编号
		if (goodsVo.getSn() == null || goodsVo.getSn().equals("")) {
			goodsVo.setSn(this.createSn());
		} else {
			// 检测商品编号是否重复
			if (this.checkSn(goodsVo.getSn(), goodsVo.getGoods_id())) {
				throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "商品编号已存在");
			}
		}
		// 没有规格给这个字段塞0
		if (goodsVo.getSkuList() != null && goodsVo.getSkuList().size() > 0) {
			goodsVo.setHave_spec(1);
		} else {
			goodsVo.setHave_spec(0);
		}
		Goods goods = new Goods(goodsVo);
		// 判断是否添加的是积分商品
		if (goodsVo.getExchange() != null && goodsVo.getExchange().getEnable_exchange() == 1) {
			goods.setGoods_type(GoodsType.point.name());
		} else {
			goods.setGoods_type(GoodsType.normal.name());
		}
		goods.setGoods_id(null);
		Integer self = ShopApp.self_storeid;
		// 当前登陆人的店铺id
		int sellerid = seller.getStore_id();
		int is_self = 0;
		if (sellerid == self) {// 如果is_self=1 当前登陆人就是自营，否则不是
			is_self = 1;
		}
		if (StoreSetting.getAuth() == 1 && is_self != 1) {// 普通商品(不是自营)添加是否需要审核 ,0不需要审核,1需要
			goods.setIs_auth(0);
		} else if (StoreSetting.getSelf_auth() == 1 && is_self == 1) {// 自营商品(是自营)添加是否需要审核 ,0不需要审核,1需要
			goods.setIs_auth(0);
		} else {
			goods.setIs_auth(1);
		}
		// 商品状态 是否可用
		goods.setDisabled(0);
		// 商品创建时间
		goods.setCreate_time(DateUtil.getDateline());
		// 商品浏览次数
		goods.setView_count(0);
		// 商品购买数量
		goods.setBuy_count(0);
		// 评论次数
		goods.setComment_num(0);
		// 商品最后更新时间
		goods.setLast_modify(DateUtil.getDateline());
		// 商品库存
		goods.setQuantity(goodsVo.getQuantity());
		// 可用库存
		goods.setEnable_quantity(goodsVo.getQuantity());
		// 向goods加入图片
		GoodsGallery goodsGalley = goodsGalleryManager.getGoodsGallery(goodsVo.getGoodsGalleryList().get(0).getOriginal());
		goods.setOriginal(goodsVo.getGoodsGalleryList().get(0).getOriginal());
		goods.setBig(goodsGalley.getBig());
		goods.setSmall(goodsGalley.getSmall());
		goods.setThumbnail(goodsGalley.getThumbnail());
		if (goodsVo.getMarket_enable() == 0) {
			goods.setMarket_enable(0);
		} else {
			goods.setMarket_enable(1);
		}
		goods.setSeller_id(seller.getStore_id());
		// 需要插入店铺名字
		Shop shop = shopManager.getShop();
		goods.setSeller_name(shop.getShop_name());
		// 添加商品
		this.daoSupport.insert("es_goods", goods);
		// 获取添加商品的商品ID
		Integer goods_id = this.daoSupport.getLastId("es_goods");
		if (goods_id == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "添加失败,请刷新页面后重新添加");
		}
		goods.setGoods_id(goods_id);
		// 添加商品参数
		goodsParamsManager.addParams(goodsVo.getGoodsParamsList(), goods_id);
		// 添加商品sku信息
		goodsSkuManager.add(goodsVo.getSkuList(),goods);
		goodsGalleryManager.add(goodsVo.getGoodsGalleryList(), goods_id);
		// 添加积分换购商品
		exchangeManager.add(goodsVo.getGoods_id(), goodsVo.getExchange());
		// 发送增加商品消息，店铺增加自身商品数量，静态页使用
		GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[] { goods.getGoods_id() },
				GoodsChangeMsg.ADD_OPERATION);
		this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey", goodsChangeMsg);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#edit(com.enation.app.shop.
	 * goods.model.vo.GoodsVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(GoodsVo goodsVo) {
		Seller seller = sellerMangager.getSeller();
		goodsVo.setSeller_id(seller.getStore_id());
		goodsVo.setDisabled(0);
		Goods goods = new Goods(goodsVo);
		// 判断是否把商品修改成积分商品
		if (goodsVo.getExchange() != null && goodsVo.getExchange().getEnable_exchange() == 1) {
			goods.setGoods_type(GoodsType.point.name());
		} else {
			goods.setGoods_type(GoodsType.normal.name());
		}
		if (goodsVo.getMarket_enable() == 0) {
			goods.setMarket_enable(0);
		} else {
			goods.setMarket_enable(1);
		}
		if (StoreSetting.getEdit_auth() == 1) {// 所有店铺修改商品是否需要审核 ，修改商品不区分是否为自营
			goods.setIs_auth(0);
		} else {
			goods.setIs_auth(1);
		}
		// 如果切换了规格组合或者 把有规格的商品改为无规格的商品时，（也就是说sku的总库存时0的时候要把商品表的可用库存和总库存都置为0）
		if (goodsVo.getHas_changed() == 1) {
			goods.setQuantity(goodsVo.getQuantity());
			goods.setEnable_quantity(goodsVo.getQuantity());
		}
		goods.setLast_modify(DateUtil.getDateline()); // 添加商品更新时间
		// 修改相册信息
		List <GoodsGallery> goodsGalleys  = goodsVo.getGoodsGalleryList();
		goodsGalleryManager.edit(goodsGalleys, goodsVo.getGoods_id());
		// 向goods加入图片
		goods.setOriginal(goodsGalleys.get(0).getOriginal());
		goods.setBig(goodsGalleys.get(0).getBig());
		goods.setSmall(goodsGalleys.get(0).getSmall());
		goods.setThumbnail(goodsGalleys.get(0).getThumbnail());
		this.daoSupport.update("es_goods", goods, "goods_id=" + goodsVo.getGoods_id());
		if (goods.getGoods_id() == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "修改失败,请刷新页面后重新修改");
		}
		// 处理参数信息
		goodsParamsManager.addParams(goodsVo.getGoodsParamsList(), goodsVo.getGoods_id());
		// 处理规格信息
		List<GoodsSkuVo> skuList = goodsSkuManager.edit(goodsVo.getSkuList(),goods,
				goodsVo.getHas_changed());
		// 添加商品的积分换购信息
		exchangeManager.edit(goodsVo.getGoods_id(), goodsVo.getExchange());

		// 商品信息添加成功后塞入缓存
		goodsVo.setSkuList(skuList);
		cache.remove(GoodsCacheKey.GOODS.name() + goodsVo.getGoods_id());
		// 发送增加商品消息，店铺增加自身商品数量，静态页使用
		GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[] { goodsVo.getGoods_id() },
				GoodsChangeMsg.UPDATE_OPERATION);
		this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey", goodsChangeMsg);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#delete(java.lang.Integer[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer[] goods_ids) throws Exception {
		if (goods_ids != null) {
			List<Object> term = new ArrayList<>();
			String[] goods = new String[goods_ids.length];
			for (int i = 0; i < goods_ids.length; i++) {
				goods[i] = "?";
				term.add(goods_ids[i]);
				// 删除缓存中的商品
				cache.remove(GoodsCacheKey.GOODS.name() + goods_ids[i]);
			}
			String id_str = StringUtil.arrayToString(goods, ",");
			String sql = "delete  from es_goods  where goods_id in (" + id_str + ")";

			this.daoSupport.execute(sql, term.toArray());
			// 删除库存
			this.daoSupport.execute("delete from es_goods_depot where goods_id in (" + id_str + ")", term.toArray());
			// this.goodsGalleryManager.delete(goods_ids);
			// 删除商品sku信息
			this.goodsSkuManager.delete(goods_ids);
			// 删除相册信息
			goodsGalleryManager.delete(goods_ids);
			/**
			 * 删除商品暂时不发消息，因为商品已经物理删除，消费者消费的商品不存在
			 */
			//			GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goods_ids, GoodsChangeMsg.DEL_OPERATION);
			//			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey",
			//					goodsChangeMsg);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#under(java.lang.Integer[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void under(Integer[] goods_ids, String message) {
		if (goods_ids != null) {
			List<Object> term = new ArrayList<>();
			String[] goods = new String[goods_ids.length];
			for (int i = 0; i < goods_ids.length; i++) {
				goods[i] = "?";
				term.add(goods_ids[i]);
				// 处理缓存中的商品
				cache.remove(GoodsCacheKey.GOODS.name() + goods_ids[i]);
			}
			String id_str = StringUtil.arrayToString(goods, ",");
			String sql = "update es_goods set market_enable = 0 where goods_id in (" + id_str + ")";
			this.daoSupport.execute(sql, term.toArray());

			GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goods_ids, GoodsChangeMsg.UNDER_OPERATION, message);
			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey",
					goodsChangeMsg);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#revert(java.lang.Integer[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void revert(Integer[] goods_ids) {
		if (goods_ids != null) {
			List<Object> term = new ArrayList<>();
			String[] goods = new String[goods_ids.length];
			for (int i = 0; i < goods_ids.length; i++) {
				goods[i] = "?";
				term.add(goods_ids[i]);
				// 处理缓存中的商品
				cache.remove(GoodsCacheKey.GOODS.name() + goods_ids[i]);
			}
			String id_str = StringUtil.arrayToString(goods, ",");
			String sql = "update  es_goods set disabled=0  where goods_id in (" + id_str + ")";
			this.daoSupport.execute(sql, term.toArray());

			GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goods_ids, GoodsChangeMsg.REVERT_OPERATION);
			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey",
					goodsChangeMsg);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#inRecycle(java.lang.Integer[
	 * ])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void inRecycle(Integer[] goods_ids) {
		if (goods_ids != null) {
			List<Object> term = new ArrayList<>();
			String[] goods = new String[goods_ids.length];
			for (int i = 0; i < goods_ids.length; i++) {
				goods[i] = "?";
				term.add(goods_ids[i]);
				// 商品放入回收站应在缓存中删除商品
				cache.remove(GoodsCacheKey.GOODS.name() + goods_ids[i]);
			}
			String id_str = StringUtil.arrayToString(goods, ",");
			String sql = "update  es_goods set disabled=1,market_enable=0  where goods_id in (" + id_str + ")";
			this.daoSupport.execute(sql, term.toArray());
			GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goods_ids, GoodsChangeMsg.INRECYCLE_OPERATION);
			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey",
					goodsChangeMsg);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#authStoreGoods(java.lang.
	 * Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GoodsVo authStoreGoods(Integer goods_id, Integer pass, String message) {

		// 审核通过
		if (pass.intValue() == 1) {
			daoSupport.execute("update es_goods set is_auth=?,auth_message=?,market_enable=? where goods_id=?", 1,
					message, 1, goods_id);
			// TODO 商品审核时生成静态页
		} else {
			daoSupport.execute("update es_goods set is_auth=?,auth_message=?,market_enable=? where goods_id=?", 2,
					message, 0, goods_id);
		}
		return null;
	}

	/**
	 * 判断商品编号是否重复
	 *
	 * @param sn
	 *            商品编号
	 * @param goods_id
	 *            商品id
	 * @return boolean
	 */
	public boolean checkSn(String sn, Integer goods_id) {
		Seller seller = sellerMangager.getSeller();
		String sql = "select count(0) num from es_goods where sn = ? and seller_id=? and goods_id != ?";
		if (goods_id == null) {
			goods_id = -1;
		}
		int count = this.daoSupport.queryForInt(sql, sn, seller.getStore_id(), goods_id);
		return count > 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.enation.app.shop.goods.service.IGoodsManager#getCountByGoodsIds(java.lang
	 * .Integer[], java.lang.Integer)
	 */
	@Override
	public Integer getCountByGoodsIds(Integer[] goodsId, Integer seller_id) {
		List<Object> term = new ArrayList<>();
		String[] goods = new String[goodsId.length];
		for (int i = 0; i < goodsId.length; i++) {
			goods[i] = "?";
			term.add(goodsId[i]);
		}
		String id_str = StringUtil.arrayToString(goods, ",");

		String sql = "select count(1) from es_goods where goods_id in (" + id_str + ") and seller_id = ?";
		term.add(seller_id);
		return daoSupport.queryForInt(sql, term.toArray());
	}

	@Override
	public Page list(GoodsQueryParam goodsQueryParam) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"select g.goods_id,g.goods_name,g.sn,g.thumbnail,g.seller_name,g.enable_quantity,g.quantity,g.price,g.create_time,g.market_enable ,b.`name` brand_name,c.`name` category_name "
						+ "from es_goods g left join es_goods_category c on g.category_id = c.category_id left join es_brand b on g.brand_id = b.brand_id "
						+ "where  g.disabled = 0  ");
		if (goodsQueryParam.getMarket_enable() == null || (goodsQueryParam.getMarket_enable().intValue() != 1
				&& goodsQueryParam.getMarket_enable().intValue() != 2)) {
			sqlBuffer.append(" and g.market_enable !=2 ");
		} else {
			sqlBuffer.append(" and g.market_enable = " + goodsQueryParam.getMarket_enable());
		}

		if (goodsQueryParam.getStype().intValue() == 0) {
			if (!StringUtil.isEmpty(goodsQueryParam.getKeyword())) {
				sqlBuffer.append(" and (g.goods_name like '%" + goodsQueryParam.getKeyword() + "%' or g.sn like '%"
						+ goodsQueryParam.getKeyword() + "%') ");
			}
		} else {
			// 高级搜索
			if (goodsQueryParam.getCategory_id() != null) {
				Category category = this.daoSupport.queryForObject(
						"select * from es_goods_category where category_id=? ", Category.class,
						goodsQueryParam.getCategory_id());
				if (category != null) {
					String cat_path = category.getCategory_path();
					if (cat_path != null) {
						sqlBuffer.append(" and  g.category_id in(");
						sqlBuffer.append("select c.category_id from es_goods_category");
						sqlBuffer.append(" c where c.category_path like '" + cat_path + "%')");
					}
				}
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_name())) {
				sqlBuffer.append(" and g.goods_name like '%" + goodsQueryParam.getGoods_name() + "%'");
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getSeller_name())) {
				sqlBuffer.append(" and g.seller_name like '%" + goodsQueryParam.getSeller_name() + "%'");
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_sn())) {
				sqlBuffer.append(" and g.sn like '%" + goodsQueryParam.getGoods_sn() + "%'");
			}
		}
		sqlBuffer.append(" order by g.goods_id desc");
		Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), goodsQueryParam.getPage_no(),
				goodsQueryParam.getPage_size());
		return page;
	}

	private List<Integer> getCatByParent(Integer parent_id) {
		List<ShopCat> cats = this.daoSupport.queryForList("select shop_cat_id from es_shop_cat where shop_cat_pid = ?",
				ShopCat.class, parent_id);
		List<Integer> rs = new ArrayList<>();
		for (ShopCat c : cats) {
			rs.add(c.getShop_cat_id());
		}
		return rs;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void up(Integer[] goods_id) {
		if (goods_id != null) {
			List<Object> term = new ArrayList<>();
			String[] goods = new String[goods_id.length];
			for (int i = 0; i < goods_id.length; i++) {
				goods[i] = "?";
				term.add(goods_id[i]);
				// 缓存中加入商品
				cache.remove(GoodsCacheKey.GOODS.name() + goods_id[i]);
			}
			String id_str = StringUtil.arrayToString(goods, ",");
			String sql = "update es_goods set market_enable = 1 where goods_id in (" + id_str + ")";
			this.daoSupport.execute(sql, term.toArray());
			GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(goods_id, GoodsChangeMsg.UNDER_OPERATION);
			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey",
					goodsChangeMsg);
		}

	}

	@Override
	public Goods getFromDB(Integer goods_id) {
		String sql = "select * from es_goods where goods_id = ?";
		Goods goodsDB = this.daoSupport.queryForObject(sql, Goods.class, goods_id);
		return goodsDB;
	}

	/**
	 * 逻辑：判断当前session中的viewedGoods变量是否包含了当前goods，如未，则 向变量中加入goods，如果已经存在则
	 * 1、从redis中取出此商品的浏览数 2、如果为空则新增此商品浏览数为1，否则判断浏览数是否大于等于50，逻辑为每50次存入数据库 3、存入相关数据库
	 */
	@Override
	public void visitedGoodsNum(Integer goods_id) {
		HttpSession sessionContext = ThreadContextHolder.getSession();
		List<Integer> visitedGoods = (List<Integer>) sessionContext.getAttribute("visitedGoods");
		boolean visited = false;
		if (visitedGoods == null) {
			visitedGoods = new ArrayList<Integer>();
		}
		for (Integer id : visitedGoods) {
			if (id.equals(goods_id)) {// 说明当前session访问过此商品
				visitedGoods.remove(id);
				visited = true;
				break;
			}
		}
		visitedGoods.add(0, goods_id);
		sessionContext.setAttribute("visitedGoods", visitedGoods);
		if (!visited) {
			String key = "VISIT" + goods_id;
			/** 获取redis中此商品的浏览数 */
			Object num = this.cache.get(key);
			/** 如果redis中未记录此浏览数 则新增此商品浏览数为1 */
			if (num != null) {
				/** 如果浏览数大于等于50则存入相关数据库 */
				if (StringUtil.toInt(num, false) >= 50) {
					/** num为历史访问量，如果满足条件需要将此次浏览数也要加进去 固+1 */
					this.daoSupport.execute("update es_goods set view_count = view_count + ? where goods_id = ?",
							StringUtil.toInt(num, false) + 1, goods_id);
					/** 清空redis */
					this.cache.put(key, 0);
				} else {
					this.cache.put(key, StringUtil.toInt(num, false) + 1);
				}
			} else {
				this.cache.put(key, 1);
			}
			Integer store_id = StringUtil.toInt(
					this.daoSupport.queryForInt("select seller_id from es_goods where goods_id = ?", goods_id), false);
			flowStatisticsManager.addFlowLog(goods_id, store_id);
		}
	}

	// 店铺设置插件使用，系统设置把所有商品置为可审核或不需要审核时调用
	@Override
	public void editAllGoodsAuthAndMarketenable(Integer selfOrstore) {
		String sql = "update es_goods set is_auth =1 where is_auth in(0,2,3) ";
		if (selfOrstore.intValue() == 1) {
			sql += " and seller_id=1";
		}
		this.daoSupport.execute(sql);
	}

	@Override
	public void editStoreGoodsGrade(Integer goods_id) {
		int gradeAvg = this.getGoodsGradeAvg(goods_id);
		String sql = "update es_goods set grade = ? where goods_id = ?";
		this.daoSupport.execute(sql, gradeAvg, goods_id);
	}

	@Override
	public void addStoreGoodsComment(Integer goods_id) {
		String sql = "update es_goods set comment_num=comment_num+1 where goods_id=" + goods_id;
		this.daoSupport.execute(sql);
		// 发送修改商品消息
		GoodsChangeMsg goodsChangeMsg = new GoodsChangeMsg(new Integer[] {goods_id}, GoodsChangeMsg.UPDATE_OPERATION);
		this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE.name(), "goods-change-routingKey",
				goodsChangeMsg);
	}

	/**
	 * 根据商品id获取商品评分的平均值
	 *
	 * @param goods_id
	 *            商品id
	 * @return gradeAvg 商品评分的平均值
	 */
	private int getGoodsGradeAvg(Integer goods_id) {
		int gradeAvg;
		String sql = "select avg(grade) from es_member_comment where goods_id = ? and type = 1";
		gradeAvg = this.daoSupport.queryForInt(sql, goods_id);
		return gradeAvg;
	}

	@Override
	public Page shopGoodsList(Integer pageNo, Integer pageSize, Map map) {
		Integer store_id = Integer.valueOf(map.get("store_id").toString());
		Integer disable = Integer.valueOf(map.get("disable") + "");
		String store_cat = String.valueOf(map.get("store_cat"));
		String goodsName = String.valueOf(map.get("goodsName"));
		String market_enable = String.valueOf(map.get("market_enable"));
		String goods_type = String.valueOf(map.get("goods_type"));
		StringBuffer sql = new StringBuffer(
				"SELECT g.*,c.store_cat_name from es_goods g LEFT JOIN es_store_cat c ON g.shop_cat_id=c.store_cat_id where g.seller_id="
						+ store_id + " and  g.disabled=" + disable);
		if (!StringUtil.isEmpty(store_cat) && !StringUtil.equals(store_cat, "null")
				&& !StringUtil.equals(store_cat, "0")) {
			// 根据店铺分类ID获取分类的父ID add by DMRain 2016-1-19
			Integer pId = this.storeGoodsCatManager.is_children(Integer.parseInt(store_cat));

			// 如果店铺分类父ID为0，证明要查询的分类为父分类下的所有子分类和父分类本身 add by DMRain 2016-1-19
			if (pId == 0) {
				sql.append(" and (c.store_cat_pid=" + store_cat + " or g.shop_cat_id=" + store_cat + ")");
			} else {
				sql.append(" and g.shop_cat_id=" + store_cat);
			}
		}
		if (!StringUtil.isEmpty(goodsName) && !StringUtil.equals(goodsName, "null")) {
			sql.append(" and ((g.goods_name like '%" + goodsName + "%') or (g.sn like '%" + goodsName + "%') )");
		}
		if (!StringUtil.isEmpty(market_enable) && !StringUtil.equals(market_enable, "null")) {
			if (!market_enable.equals("99")) {
				sql.append(" and g.market_enable=" + market_enable);
			} else {
				sql.append(" and g.market_enable !=2 ");
			}
		}
		if (!StringUtil.isEmpty(goods_type) && !StringUtil.equals(goods_type, "null")) {
			sql.append(" and g.goods_type= '"+ goods_type+"' ");
		}
		sql.append(" order by g.create_time desc");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}

	@Override
	public Page shopSearchGoodsList(Integer page, Integer pageSize, Map params) {
		Integer storeid = (Integer) params.get("storeid");
		String keyword = (String) params.get("keyword");
		String start_price = (String) params.get("start_price");
		String end_price = (String) params.get("end_price");
		Integer key = (Integer) params.get("key");
		String order = (String) params.get("order");
		Integer cat_id = (Integer) params.get("stc_id");

		StringBuffer sql = new StringBuffer(
				"select * from es_goods g where disabled=0 and market_enable=1 and seller_id=" + storeid);

		if (!StringUtil.isEmpty(keyword)) {
			sql.append("  and g.goods_name like '%" + keyword + "%' ");
		}

		if (!StringUtil.isEmpty(start_price)) {
			sql.append(" and price>=" + Double.valueOf(start_price));
		}
		if (!StringUtil.isEmpty(end_price)) {
			sql.append(" and price<=" + Double.valueOf(end_price));
		}

		if (cat_id != null && cat_id != 0) {

			List<Map> list = this.daoSupport.queryForList("select store_cat_id from es_store_cat where store_cat_pid=?",
					cat_id);
			String cat_str = cat_id + ",";
			for (Map map : list) {
				cat_str += map.get("store_cat_id").toString() + ",";
			}
			sql.append(" and  g.shop_cat_id in(" + cat_str.substring(0, cat_str.length() - 1) + ")");
		}

		if (key != null) {
			if (key == 1) { // 1:新品
				sql.append(" order by goods_id " + order);
			} else if (key == 2) { // 2:价格
				sql.append(" order by price " + order);
			} else if (key == 3) { // 3:销量
				sql.append(" order by buy_count " + order);
			} else if (key == 4) { // 4:收藏
				sql.append(" order by goods_id " + order);
			} else if (key == 5) { // 5:人气
				sql.append(" order by goods_id " + order);
			} else {
				// sqlserver，当没有id字段默认执行
				sql.append(" order by goods_id  desc");
			}
		} else {
			// sqlserver，当没有id字段默认执行
			sql.append(" order by goods_id  desc");
		}

		Page webpage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		return webpage;
	}

	@Override
	public List listGoods(String catid, String tagid, String goodsnum) {
		int num = 10;
		if (!StringUtil.isEmpty(goodsnum)) {
			num = Integer.valueOf(goodsnum);
		}

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select g.* from es_tag_rel r LEFT JOIN es_goods g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");

		if (!StringUtil.isEmpty(catid)) {
			Category cat = this.categoryManager.get(Integer.valueOf(catid));
			if (cat != null) {
				String cat_path = cat.getCategory_path();
				if (cat_path != null) {
					sql.append(" and  g.cat_id in(");
					sql.append("select c.cat_id from es_goods_cat");
					sql.append(" c where c.cat_path like '" + cat_path + "%')");
				}
			}
		}

		if (!StringUtil.isEmpty(tagid)) {
			sql.append(" AND r.tag_id=" + tagid + "");
		}

		sql.append(" order by r.ordernum asc");
		List list = this.daoSupport.queryForListPage(sql.toString(), 1, num);
		return list;
	}

	@Override
	public Page storeWarningGoodsList(Integer pageNo, Integer pageSize, Map map) {
		Integer store_id = Integer.valueOf(map.get("store_id").toString());
		String store_cat = String.valueOf(map.get("store_cat"));
		String goodsName = String.valueOf(map.get("goodsName"));
		// 查询第三方卖家的商品预警数
		String sql_store = "select * from es_shop_detail where shop_id = ?";
		Map queryForMap = daoSupport.queryForMap(sql_store, store_id);
		Integer goods_warning_count = Integer.valueOf(queryForMap.get("goods_warning_count").toString());
		/*
		 * 拼写sql语句查询数据库预警商品数目，先查询库存表中相同productid且大于预警数的商品数目，如果数目等于仓库数量，则表示
		 * 该货品不需要预警，查询所有不需要预警的货品，与货品表进行比对，在货品表中而不再商品库存表中的货品为需要预警的商品
		 */
		StringBuffer sql = new StringBuffer("select g.*,c.store_cat_name " + " from (select distinct goods_id "
				+ " from es_goods_sku p " + " where  enable_quantity<= ? ) tem," + " (select * from es_goods "
				+ " where seller_id=?) g,es_store_cat c " + " where g.shop_cat_id=c.store_cat_id "
				+ "	and g.goods_id=tem.goods_id " + "	and g.market_enable=1 " + " and g.disabled=0 ");
		if (!StringUtil.isEmpty(store_cat) && !StringUtil.equals(store_cat, "null")
				&& !StringUtil.equals(store_cat, "0")) {
			// 根据店铺分类ID获取分类的父ID
			Integer pId = this.storeGoodsCatManager.is_children(Integer.parseInt(store_cat));

			// 如果店铺分类父ID为0，证明要查询的分类为父分类下的所有子分类和父分类本身
			if (pId == 0) {
				sql.append(" and (c.store_cat_pid=" + store_cat + " or g.shop_cat_id=" + store_cat + ")");
			} else {
				sql.append(" and g.shop_cat_id=" + store_cat);
			}
		}
		if (!StringUtil.isEmpty(goodsName) && !StringUtil.equals(goodsName, "null")) {
			sql.append(" and ((g.goods_name like '%" + goodsName + "%') or (g.sn like '%" + goodsName + "%') )");
		}
		String countsql = "select count(*) from (" + sql + ") temp_table";
		sql.append(" order by g.create_time desc");
		return this.daoSupport.queryForPage(sql.toString(), countsql, pageNo, pageSize, goods_warning_count, store_id);
	}

	@Override
	public List<GoodsSkuVo> warningGoodsList(Integer goodsId, Integer store_id) {
		String sql = "select * from es_shop_detail where shop_id=?";
		Map queryForMap = daoSupport.queryForMap(sql, store_id);
		Integer goods_warning_count = Integer.valueOf(queryForMap.get("goods_warning_count").toString());
		sql = "select * from es_goods_sku where goods_id=? and enable_quantity<=? order by sku_id asc";
		List<GoodsSkuVo> prolist = daoSupport.queryForList(sql, GoodsSkuVo.class, goodsId, goods_warning_count);
		return prolist;
	}
}
