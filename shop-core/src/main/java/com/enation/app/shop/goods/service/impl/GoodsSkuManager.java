package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.enums.GoodsCacheKey;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.po.GoodsSku;
import com.enation.app.shop.goods.model.vo.CacheSku;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.ShopCat;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.goods.service.IGoodsGalleryManager;
import com.enation.app.shop.goods.service.IGoodsSkuManager;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.vo.Spec;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.database.StringMapper;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;
import com.google.gson.Gson;

/**
 * 
 * 商品sku manager
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 下午2:07:05
 */
@Service
public class GoodsSkuManager implements IGoodsSkuManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ISellerManager sellerMangager;
	@Autowired
	private ICache cache;
	@Autowired
	private IShopManager shopManager;
	@Autowired
	private IGoodsGalleryManager goodsGalleryManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<GoodsSkuVo> add(List<GoodsSkuVo> skuList, Goods goods) {
		Integer goodsid = goods.getGoods_id();
		// 如果有规格
		if (skuList != null && skuList.size() > 0) {
			// 判断sn是否和数据库重复，判断sn数组之间是否重复
			this.check(skuList, goodsid);

			// 清除缓存及库中的sku
			this.clean(goodsid);

			// 添加商品sku
			this.addGoodsSku(skuList, goods);
		} else {

			// 清除缓存及库中的sku
			this.clean(goodsid);

			// 添加没有规格的sku信息
			this.addNoSku(goods);
		}
		return skuList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<GoodsSkuVo> edit(List<GoodsSkuVo> skuList, Goods goods, Integer has_changed) {
		// 如果编辑的时候sku数据有变化(包括规格项组合变了，有规格改成无规格，无规格改成有规格) has_changed=1 规格有改变

		Integer goodsid = goods.getGoods_id();
		if (has_changed == 1) {

			// 如果有规格
			if (skuList != null && skuList.size() > 0) {
				// 清除缓存及库中的sku
				this.clean(goodsid);

				// 判断sn是否和数据库重复，判断sn数组之间是否重复
				this.check(skuList, goodsid);

				// 添加商品sku
				this.addGoodsSku(skuList, goods);

			} else {
				// 清除缓存及库中的sku
				this.clean(goodsid);

				// 添加没有规格的sku信息
				this.addNoSku(goods);
			}
			return skuList;
		} else {
			// 如果没有改变规格组合
			if (skuList != null && skuList.size() > 0) {
				// 清除这个商品的sku缓存
				this.cleanSkuCache(goodsid);

				// 先把与原sku组合缺少的删掉
				this.deleteNoUseSku(skuList, goods);

				// 修改sku
				this.updateGoodsSku(skuList, goods);

			} else {
				// 首先清除这个商品的sku缓存
				this.cleanSkuCache(goodsid);

				// 修改没有规格的sku信息
				this.updateNoSku(goods);
			}
			return skuList;
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer[] goods_id) {
		List<Object> term = new ArrayList<>();
		String[] goods = new String[goods_id.length];
		for (int i = 0; i < goods_id.length; i++) {
			goods[i] = "?";
			term.add(goods_id[i]);
		}
		String id_str = StringUtil.arrayToString(goods, ",");
		// 删除这个商品的sku缓存(必须要在删除库中sku前先删缓存),首先查出商品对应的sku_id
		String sku_id_sql = "select sku_id from es_goods_sku where goods_id in (" + id_str + ")";
		List sku_id = daoSupport.queryForList(sku_id_sql, term.toArray());
		for (int i = 0; i < sku_id.size(); i++) {
			Map skuids = (Map) sku_id.get(i);
			Integer skuid = (Integer) skuids.get("sku_id");
			cache.remove(GoodsCacheKey.SKU.name() + skuid);
		}
		String sql = "delete from es_goods_spec where goods_id in (" + id_str + ")";
		this.daoSupport.execute(sql, term.toArray());
		sql = "delete from es_goods_sku where goods_id in (" + id_str + ")";
		this.daoSupport.execute(sql, term.toArray());
	}

	@Override
	public GoodsSkuVo getSkuFromCache(Integer sku_id) {
		// 从缓存读取sku信息
		GoodsSkuVo skuVo = (GoodsSkuVo) cache.get(GoodsCacheKey.SKU.name() + sku_id);
		// 缓存中没有找到商品，从数据库中查询
		if (skuVo == null) {
			String sql = "select s.* from es_goods_sku s  where s.sku_id=?";
			GoodsSku sku = this.daoSupport.queryForObject(sql, GoodsSku.class, sku_id);
			if (sku == null) {
				return null;
			}
			skuVo = new GoodsSkuVo(sku);
			sql = "select goods_transfee_charge ,market_enable  ,disabled from es_goods where goods_id =?";
			Map result = new HashMap();
			result = this.daoSupport.queryForMap(sql, sku.getGoods_id());
			skuVo.setGoods_transfee_charge(Integer.valueOf(result.get("goods_transfee_charge").toString()));
			skuVo.setDisabled(Integer.valueOf(result.get("disabled").toString()));
			skuVo.setMarket_enable(Integer.valueOf(result.get("market_enable").toString()));
			cache.put(GoodsCacheKey.SKU.name() + sku_id, skuVo);
			return skuVo;
		}
		return skuVo;
	}

	@Override
	public Page query(GoodsQueryParam goodsQueryParam) {

		StringBuffer sqlBuffer = new StringBuffer();
		List<Object> term = new ArrayList<Object>();
		sqlBuffer.append(
				"select u.* from es_goods_sku u inner join es_goods g on u.goods_id = g.goods_id where g.market_enable =1 and g.disabled = 0");

		if (goodsQueryParam.getStype().intValue() != 1) {
			if (!StringUtil.isEmpty(goodsQueryParam.getKeyword())) {
				sqlBuffer.append(" and (g.goods_name like ? or g.sn like ?) ");
				term.add("%" + goodsQueryParam.getKeyword() + "%");
				term.add("%" + goodsQueryParam.getKeyword() + "%");
			}
		} else {
			if (goodsQueryParam.getShop_cat_id() != null) {
				Integer shop_cat = goodsQueryParam.getShop_cat_id();
				List<Integer> shop_cat_ids = this.getCatByParent(shop_cat);

				shop_cat_ids.add(shop_cat);
				String[] ids = new String[shop_cat_ids.size()];
				int i = 0;
				for (Integer cat_id : shop_cat_ids) {
					ids[i] = "?";
					term.add(cat_id);
					i++;
				}

				sqlBuffer.append(" and g.shop_cat_id in (" + ids.toString() + ")");
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_name())) {
				sqlBuffer.append(" and u.goods_name like ?");
				term.add(goodsQueryParam.getGoods_name());
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_sn())) {
				sqlBuffer.append(" and u.sn = ? ");
				term.add(goodsQueryParam.getGoods_sn());
			}

		}
		if (goodsQueryParam.getSeller_id() != null) {
			sqlBuffer.append(" and u.seller_id = ? ");
			term.add(goodsQueryParam.getSeller_id());
		}
		sqlBuffer.append(" order by u.sku_id desc");
		Page<GoodsSku> page = this.daoSupport.queryForPage(sqlBuffer.toString(), goodsQueryParam.getPage_no(),
				goodsQueryParam.getPage_size(), GoodsSku.class, term.toArray());

		return page;
	}

	/**
	 * 获取店铺分类
	 * 
	 * @param parent_id
	 * @return
	 */
	public List<Integer> getCatByParent(Integer parent_id) {
		List<ShopCat> cats = this.daoSupport.queryForList("select shop_cat_id from es_store_cat where shop_cat_pid = ?",
				ShopCat.class, parent_id);
		List<Integer> rs = new ArrayList<>();
		for (ShopCat c : cats) {
			rs.add(c.getShop_cat_id());
		}
		return rs;
	}

	@Override
	public CacheSku getSkuFromDatabase(Integer goods_id, Integer sku_id) {
		String sql = "select * from es_goods_sku where goods_id = ? and sku_id = ? ";
		CacheSku cachesku = this.daoSupport.queryForObject(sql, CacheSku.class, goods_id, sku_id);
		if (cachesku != null) {
			sql = "select spec_name,spec_value from  es_goods_spec gs inner join es_spec_values sv "
					+ "on gs.spec_value_id = sv.spec_value_id inner join es_specification s "
					+ "on sv.spec_id = s.spec_id where sku_id = ?";
			List<Spec> spec = this.daoSupport.queryForList(sql, Spec.class, cachesku.getSku_id());
			cachesku.setSpecList(spec);
		}

		return cachesku;
	}

	@Override
	public List<CacheSku> getSkuListFromCache(Integer goods_id) {

		this.cache.remove("store_" + goods_id);
		List<CacheSku> skuList = (List<CacheSku>) cache.get("store_" + goods_id);
		if (skuList == null) {
			String sql = "select * from es_goods_sku where goods_id = ? ";
			skuList = this.daoSupport.queryForList(sql, CacheSku.class, goods_id);
			// TODO 最终这里sql要重新调，版本在变化，暂时不动，但不够优化
			for (CacheSku sku : skuList) {
				sql = "select gs.spec_name,spec_value from  es_goods_spec gs inner join es_spec_values sv "
						+ "on gs.spec_value_id = sv.spec_value_id inner join es_specification s "
						+ "on sv.spec_id = s.spec_id where sku_id = ?";
				List<Spec> spec = this.daoSupport.queryForList(sql, Spec.class, sku.getSku_id());
				sku.setSpecList(spec);
			}
			cache.put("store_" + goods_id, skuList);
		}

		return skuList;
	}

	@Override
	public List<GoodsSku> query(Integer[] skuidAr) {

		if (skuidAr == null || skuidAr.length == 0) {
			return new ArrayList<>();
		}

		StringBuffer term = new StringBuffer();

		/** 形成 ?,?,?的效果 */
		for (Integer skuid : skuidAr) {
			if (term.length() != 0) {
				term.append(",");
			}
			term.append("?");
		}

		String sql = "select * from es_goods_sku where sku_id in (" + term + ")";

		return this.daoSupport.queryForList(sql, GoodsSku.class, skuidAr);
	}

	@Override
	public List<GoodsSkuVo> listByGoodsId(Integer goods_id) {
		String sql = "select * from es_goods_sku where goods_id =?";
		List<GoodsSku> list = daoSupport.queryForList(sql, GoodsSku.class, goods_id);
		List<GoodsSkuVo> result = new ArrayList<>();
		for (GoodsSku sku : list) {
			GoodsSkuVo skuVo = new GoodsSkuVo(sku);
			result.add(skuVo);
		}
		return result;
	}

	/**
	 * 获取已有的货号数量
	 * 
	 * @param sns
	 *            货号
	 * @return 数量
	 */
	private int getSnsSize(String[] sns) {
		int i = 0;
		for (String sn : sns) {
			if (!StringUtil.isEmpty(sn)) {
				i++;
			}
		}
		return i;
	}

	/**
	 * 清除商品规格信息和sku信息 及缓存
	 * 
	 * @param goodsid
	 *            商品id
	 */
	private void clean(Integer goodsid) {
		String sql = "select * from es_goods_sku where goods_id =?";
		List<GoodsSku> list = daoSupport.queryForList(sql, GoodsSku.class, goodsid);
		if (list != null && list.size() != 0) {
			for (GoodsSku skucache : list) {
				cache.remove(GoodsCacheKey.SKU.name() + skucache.getSku_id());
			}
		}
		this.daoSupport.execute("delete from  es_goods_spec  where goods_id = ? ", goodsid);
		this.daoSupport.execute("delete from es_goods_sku where goods_id=? ", goodsid);
	}

	/**
	 * 清除sku redis缓存
	 * 
	 * @param goodsid
	 *            商品id
	 */
	private void cleanSkuCache(Integer goodsid) {
		String sql = "select * from es_goods_sku where goods_id =?";
		List<GoodsSku> list = daoSupport.queryForList(sql, GoodsSku.class, goodsid);
		if (list != null && list.size() != 0) {
			for (GoodsSku skucache : list) {
				cache.remove(GoodsCacheKey.SKU.name() + skucache.getSku_id());
			}
		}
	}

	/**
	 * 添加没有规格的sku信息
	 * 
	 * @param goods
	 *            商品信息
	 */
	private void addNoSku(Goods goods) {
		// 获取当前登陆店铺信息
		Shop shop = shopManager.getShop();
		GoodsSku goodsSku = new GoodsSku(goods);
		goodsSku.setSeller_name(shop.getShop_name());
		this.daoSupport.insert("es_goods_sku", goodsSku);
		goodsSku.setSku_id(this.daoSupport.getLastId("es_goods_sku"));
	}

	/**
	 * 添加商品sku 还有更改了sku组合时使用
	 * 
	 * @param skuList
	 *            sku集合
	 * @param goods
	 *            商品信息
	 */
	private void addGoodsSku(List<GoodsSkuVo> skuList, Goods goods) {
		// 获取当前登陆店铺信息
		Shop shop = shopManager.getShop();
		Seller seller = sellerMangager.getSeller();
		for (GoodsSkuVo sku : skuList) {
			sku.setGoods_name(goods.getGoods_name());
			sku.setCategory_id(goods.getCategory_id());
			// 得到规格值的json
			String specJson = getSpecListJson(sku.getSpecList());
			sku.setSpecs(specJson);
			sku.setGoods_id(goods.getGoods_id());
			sku.setSeller_id(seller.getStore_id());
			sku.setSeller_name(shop.getShop_name());
			sku.setThumbnail(goods.getThumbnail());
			// 添加商品的时候要添加货品的库存
			sku.setEnable_quantity(sku.getQuantity());
			this.daoSupport.insert("es_goods_sku", sku);
			Integer sku_id = this.daoSupport.getLastId("es_goods_sku");
			// 添加货品对应的规格组合
			this.addGoodsSpec(sku.getSpecList(), goods.getGoods_id(), sku_id);
		}
	}

	/**
	 * 不更改sku组合时 修改sku信息
	 * 
	 * @param skuList
	 *            sku集合
	 * @param goods
	 *            商品信息
	 */
	private void updateGoodsSku(List<GoodsSkuVo> skuList, Goods goods) {
		List<GoodsSkuVo> newSkuList = new ArrayList<>();
		for (GoodsSkuVo sku : skuList) {
			// 没有被用户移除的sku组合要修改
			if (!sku.getSku_id().equals(0)) {
				// 得到规格值的json
				String specJson = getSpecListJson(sku.getSpecList());
				sku.setSpecs(specJson);
				sku.setGoods_name(goods.getGoods_name());
				sku.setCategory_id(goods.getCategory_id());
				sku.setThumbnail(goods.getThumbnail());
				this.daoSupport.execute(
						"update es_goods_sku set category_id=?,goods_name=?,sn=?,price=?,cost=?,weight=?,thumbnail=? ,specs=? where sku_id=?",
						sku.getCategory_id(), sku.getGoods_name(), sku.getSn(), sku.getPrice(), sku.getCost(),
						sku.getWeight(), sku.getThumbnail(), sku.getSpecs(), sku.getSku_id());

				// 先删除商品规格表
				this.daoSupport.execute("delete from es_goods_spec where sku_id=?", sku.getSku_id());
				// 添加货品对应的规格组合
				this.addGoodsSpec(sku.getSpecList(), goods.getGoods_id(), sku.getSku_id());
			}
			// 新加的sku组合要入库 (skuid是0)
			if (sku.getSku_id().equals(0)) {
				newSkuList.add(sku);
			}
		}
		// 如果有新的规格值
		if (newSkuList.size() > 0) {
			this.addGoodsSku(newSkuList, goods);
		}
	}

	/**
	 * 判断sn是否和数据库重复，判断sn数组之间是否重复
	 * 
	 * @param skuList
	 *            sku集合
	 * @param goodsid
	 *            商品id
	 */
	private void check(List<GoodsSkuVo> skuList, Integer goodsid) {
		Seller seller = sellerMangager.getSeller();
		// 先判断数组中的商品编号是否重复
		Set<String> set = new HashSet<>();
		String snStr = "";
		for (GoodsSku sku : skuList) {
			set.add(sku.getSn());
			snStr += sku.getSn() + ",";
		}
		// set可以去重，所以如果set的长度和传过来的skuList相同，说明手动添加的货号没有重复
		if (skuList.size() != set.size()) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "您填写的货号有重复");
		}
		snStr = snStr.substring(0, snStr.length() - 1);
		// 查库，去判断是否sku重复
		List<String> res = this.checkSns(snStr, goodsid, seller.getStore_id());
		if (res != null && res.size() > 0) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "当前货号" + res.get(0) + "已存在其他商品中");
		}
	}

	/**
	 * 检测sku中的编号是否与其他商品重复
	 * 
	 * @param snStr(例：snStr:aa,bb,cc)
	 * @param goods_id
	 *            商品id
	 * @return
	 */
	private List<String> checkSns(String snStr, Integer goods_id, Integer seller_id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select s.sn from es_goods_sku s where s.sn in(?) and seller_id=?");
		if (goods_id == null) {
			goods_id = 0;
		}
		sql.append(" and s.goods_id != ?");
		List<String> list = this.daoSupport.queryForList(sql.toString(), new StringMapper(), snStr, seller_id,
				goods_id);
		return list;
	}

	/**
	 * 字符串排序 “1、3、2”
	 * 
	 * @param psr
	 * @return “1、2、3”
	 */
	private String sort(String psr) {
		String[] arr = psr.split("、");
		Arrays.sort(arr);
		return StringUtil.arrayToString(arr, "、");
	}

	/**
	 * sku中的spec字段的操作，返回json
	 * 
	 * @param specList
	 *            规格值集合
	 * @return 规格值json
	 */
	private String getSpecListJson(List<SpecValueVo> specList) {
		Gson gson = new Gson();
		String spenJson = null;
		List<SpecValueVo> list = new ArrayList<SpecValueVo>();
		for (SpecValueVo specvalue : specList) {
			if (specvalue.getSpec_type() == null) {
				specvalue.setSpec_type(0);
			}
			if (specvalue.getSpec_type() == 1) {
				GoodsGallery goodsGallery = goodsGalleryManager.getGoodsGallery(specvalue.getSpec_image());
				specvalue.setBig(goodsGallery.getBig());
				specvalue.setSmall(goodsGallery.getSmall());
				specvalue.setThumbnail(goodsGallery.getThumbnail());
				specvalue.setTiny(goodsGallery.getTiny());
			}
			list.add(specvalue);
		}
		spenJson = gson.toJson(list);
		return spenJson;
	}

	/**
	 * 删除没有传过来的sku(例：原商品sku组合1，2，3，现传过来1，3 就要把2删掉)
	 * 
	 * @param skuList
	 *            新的sku组合
	 * @param goods
	 *            商品
	 */
	private void deleteNoUseSku(List<GoodsSkuVo> skuList, Goods goods) {
		List<Object> sku_ids = new ArrayList<>();
		String[] temp = new String[skuList.size()];
		if (skuList.size() > 0) {
			for (int i = 0; i < skuList.size(); i++) {
				if(skuList.get(i).getSku_id()==null) {
					throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "sku信息错误");
				}
				sku_ids.add(skuList.get(i).getSku_id());
				temp[i] = "?";
			}
		}
		String str = StringUtil.arrayToString(temp, ",");
		sku_ids.add(goods.getGoods_id());
		/** 删除不在此商品sku组合中sku */
		this.daoSupport.execute("delete from es_goods_sku where sku_id not in(" + str + ") and goods_id = ?",
				sku_ids.toArray());

	}

	/**
	 * 修改没有规格的商品(商品原无规格，现也无规格)
	 * 
	 * @param goods
	 *            商品信息
	 */
	private void updateNoSku(Goods goods) {
		// 获取当前登陆店铺信息
		Shop shop = shopManager.getShop();
		// 然后修改这个sku信息
		GoodsSku goodsSku = new GoodsSku(goods);
		goodsSku.setSeller_name(shop.getShop_name());
		this.daoSupport.update("es_goods_sku", goodsSku, "goods_id=" + goods.getGoods_id());
		goodsSku.setSku_id(this.daoSupport.getLastId("es_goods_sku"));

	}

	/**
	 * 添加货品对应的规格组合
	 * 
	 * @param spec
	 *            当前sku规格
	 * @param goods_id
	 *            商品id
	 * @param sku_id
	 *            当前skuid
	 */
	private void addGoodsSpec(List<SpecValueVo> spec, Integer goods_id, Integer sku_id) {
		List<SpecValueVo> specList = spec;
		if (specList != null) {
			for (SpecValueVo specvalue : specList) {
				this.daoSupport.execute(
						"insert into es_goods_spec (spec_id,spec_name,spec_value_id,goods_id,sku_id)values(?,?,?,?,?)",
						specvalue.getSpec_id(), specvalue.getSpec_name(), specvalue.getSpec_value_id(), goods_id,
						sku_id);
			}
		}
	}
}
