package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.goods.model.vo.CacheSku;
import com.enation.app.shop.goods.model.vo.GoodsCache;
import com.enation.app.shop.goods.model.vo.GoodsLine;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.model.vo.ShopCat;
import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.trade.model.vo.Spec;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.IntegerMapper;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 查询商品的实现
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 2017年8月14日 下午4:28:08
 */
@Service
public class GoodsQueryManager implements IGoodsQueryManager {
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public Page query(GoodsQueryParam goodsQueryParam) {
		StringBuffer sqlBuffer = new StringBuffer();
		List<Object> term = new ArrayList<Object>();
		sqlBuffer.append(
				"select g.goods_id,g.goods_name,g.sn,g.thumbnail,g.enable_quantity,g.quantity,g.price,g.create_time,g.market_enable ,c.`name` brand_name,c.`name` category_name "
						+ "from es_goods g left join es_goods_category c on g.category_id = c.category_id left join es_brand b on g.brand_id = b.brand_id "
						+ "where  g.disabled = 0  ");
		if (goodsQueryParam.getMarket_enable() == null || (goodsQueryParam.getMarket_enable().intValue() != 1
				&& goodsQueryParam.getMarket_enable().intValue() != 2)) {
			sqlBuffer.append(" and g.market_enable !=2 ");
		} else {

			sqlBuffer.append(" and g.market_enable = ?");
			term.add(goodsQueryParam.getMarket_enable());
		}

		if (goodsQueryParam.getStype().intValue() != 1) {
			if (!StringUtil.isEmpty(goodsQueryParam.getKeyword())) {
				sqlBuffer.append(" and (g.goods_name like ? or g.sn like ?) ");
				term.add("%"+goodsQueryParam.getKeyword()+"%");
				term.add("%"+goodsQueryParam.getKeyword()+"%");
			}
		} else {
			if (goodsQueryParam.getShop_cat_id() != null) {
				Integer shop_cat = goodsQueryParam.getShop_cat_id();
				List<Integer> shop_cat_ids = this.getCatByParent(shop_cat);
				String id_str = "";
				if (shop_cat_ids != null && shop_cat_ids.size() > 0) {
					id_str = StringUtil.arrayToString(shop_cat_ids.toArray(), ",");
					id_str += "," + shop_cat;
				} else {
					id_str = shop_cat + "";
				}
				sqlBuffer.append(" and g.shop_cat_id in (" + id_str + ")");
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_name())) {
				sqlBuffer.append(" and g.goods_name = ?");
				term.add(goodsQueryParam.getGoods_name());
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_sn())) {
				sqlBuffer.append(" and g.sn = ? ");
				term.add(goodsQueryParam.getGoods_sn());
			}

		}
		if (goodsQueryParam.getSeller_id() != null) {
			sqlBuffer.append(" and g.seller_id = ? ");
			term.add(goodsQueryParam.getSeller_id());
		}
		sqlBuffer.append(" order by g.goods_id desc");
		Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), goodsQueryParam.getPage_no(),
				goodsQueryParam.getPage_size(), term.toArray());

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
	public List<GoodsLine> query(Integer[] goodsidAr) {

		if (goodsidAr == null || goodsidAr.length == 0) {
			return new ArrayList<>();
		}

		StringBuffer term = new StringBuffer();

		/** 形成 ?,?,?的效果 */
		for (Integer goodsid : goodsidAr) {
			if (term.length() != 0) {
				term.append(",");
			}
			term.append("?");

		}

		String sql = "select * from es_goods where goods_id in (" + term + ")";

		return this.daoSupport.queryForList(sql, GoodsLine.class, goodsidAr);
	}

	@Override
	public List<GoodsCache> queryAllGoodsForCache() {
		List<GoodsCache> resultList = new ArrayList<>();
		// 查询所有的商品
		StringBuffer sqlBuffer = new StringBuffer(
				"select goods_id  from es_goods  g  where 1=1 order by goods_id desc");
		List<Integer> list = this.daoSupport.queryForList(sqlBuffer.toString(), new IntegerMapper());
		// 查询所有的sku
		if (list != null) {
			for (Integer goods_id : list) {
				Map<String, Object> map = new HashMap<>();
				GoodsCache goods = new GoodsCache();

				List<CacheSku> skuList = this.getSkuListForCache(goods_id);

				goods.setGoodsid(goods_id);
				goods.setSkuList(skuList);
				map.put("goods_id", goods_id);
				map.put("skuList", skuList);
				resultList.add(goods);
			}
		}

		return resultList;
	}

	/**
	 * 获取商品的sku相应规格
	 * 
	 * @param goods_id
	 * @return
	 */
	private List<CacheSku> getSkuListForCache(Integer goods_id) {

		String skuSql = "select sku_id,sn,quantity,enable_quantity,price,goods_name,seller_id,seller_name,thumbnail,category_id from es_goods_sku where goods_id = ?";
		List<CacheSku> skuList = this.daoSupport.queryForList(skuSql, CacheSku.class, goods_id);
		for (CacheSku sku : skuList) {
			String sql = "select spec_name,spec_value from  es_goods_spec gs inner join es_spec_values sv "
					+ "on gs.spec_value_id = sv.spec_value_id inner join es_specification s "
					+ "on sv.spec_id = s.spec_id where sku_id = ?";
			List<Spec> spec = this.daoSupport.queryForList(sql, Spec.class, sku.getSku_id());
			sku.setSpecList(spec);
		}

		return skuList;
	}

	@Override
	public List<Map<String, Object>> getGoodsAndParams(Integer[] goods_ids) {

		StringBuffer sqlBuffer = new StringBuffer("select g.* from es_goods  g  where 1=1 ");

		List<Object> term = new ArrayList<>();
		if (goods_ids != null) {
			String[] goods = new String[goods_ids.length];
			for (int i = 0; i < goods_ids.length; i++) {
				goods[i] = "?";
				term.add(goods_ids[i]);
			}
			String str = StringUtil.arrayToString(goods, ",");
			sqlBuffer.append(" and goods_id in (" + str + ")");
		}

		sqlBuffer.append(" order by goods_id desc");
		List<Map<String, Object>> list = this.daoSupport.queryForList(sqlBuffer.toString(), term.toArray());
		if (list != null) {
			for (Map<String, Object> map : list) {
				// 查询该商品关联的可检索的参数集合
				String sql = "select gp.* from es_goods_params gp inner join es_parameters p on gp.param_id=p.param_id "
						+ "where goods_id = ? and p.param_type = 2 and is_index = 1";
				List<GoodsParams> listParams = this.daoSupport.queryForList(sql, GoodsParams.class,
						map.get("goods_id"));
				map.put("params", listParams);
			}
		}

		return list;
	}

	@Override
	public List<Map> getAllGoodsBySeller(Integer seller_id) {
		String sql = "select * from es_goods_sku where seller_id = ?";

		return this.daoSupport.queryForList(sql, seller_id);
	}

	@Override
	public List<Map> getGoodsList(int shopId) {
		String sql = "SELECT g.goods_id ,f.sku_id ,f.goods_name , g.thumbnail from es_goods_sku f left join es_goods g on f.goods_id = g.goods_id where g.seller_id = ? and  g.disabled=0 ";
		return this.daoSupport.queryForList(sql, shopId);
	}
//	is_auth =0 需要审核 并且待审核，
//	is_auth =1 不需要审核
//	is_auth =2需要审核 且审核通过
//	is_auth =3 需要审核 且审核未通过
	@Override
	public Integer getSellerGoodsCount(Integer seller_id) {
		String sql = "select count(*) from es_goods where seller_id=? and market_enable=1 and is_auth in (1,2) ";
		return this.daoSupport.queryForInt(sql,seller_id);
	}

	@Override
	public Integer getWaitShelvesGoodsCount(Integer seller_id) {
		//String sql = "select count(*) from es_goods where seller_id=? and (market_enable=0 and is_auth not in(0)) or (market_enable=1 and is_auth in(0,3) )";
		String sql ="select count(*) from es_goods where seller_id=? and (market_enable = 0 or is_auth =0 or  is_auth =3)";
		return this.daoSupport.queryForInt(sql,seller_id);
	}

}
