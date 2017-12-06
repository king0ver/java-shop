package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.DraftGoods;
import com.enation.app.shop.goods.model.po.DraftGoodsSku;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.po.GoodsSku;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.goods.service.IDraftGoodsGalleryManager;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.app.shop.goods.service.IDraftGoodsSkuManager;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.StringMapper;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;
import com.google.gson.Gson;

/**
 * 
 * 草稿箱商品的sku操作
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午9:58:23
 */
@Service
public class DraftGoodsSkuManager implements IDraftGoodsSkuManager {
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ISellerManager sellerMangager;
	@Autowired
	private IShopManager shopManager;
	@Autowired
	private IDraftGoodsManager draftGoodsManager;
	@Autowired
	private IDraftGoodsGalleryManager draftGoodsGalleryManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<GoodsSkuVo> add(List<GoodsSkuVo> skuList, Integer goodsid) {
		Seller seller = sellerMangager.getSeller();
		// 获取当前登陆店铺信息
		Shop shop = shopManager.getShop();
		DraftGoods goods = draftGoodsManager.get(goodsid);
		// 如果有规格
		if (skuList != null && skuList.size() > 0) {
			// 判断sn是否和数据库重复，判断sn数组之间是否重复
			this.check(skuList, goodsid);

			// 清除缓存及库中的sku
			this.clean(goodsid);

			// 添加商品sku
			this.addGoodsSku(skuList, goods, goodsid, seller, shop);
		} else {
			// 清除缓存及库中的sku

			this.clean(goodsid);

			// 添加没有规格的sku信息
			this.addNoSku(goods, shop);
		}
		return skuList;
	}

	@Override
	public List<GoodsSkuVo> edit(List<GoodsSkuVo> skuList, Integer goodsid, Integer has_changed) {
		DraftGoods goods = draftGoodsManager.get(goodsid);
		Seller seller = sellerMangager.getSeller();
		// 获取当前登陆店铺信息
		Shop shop = shopManager.getShop();
		// 如果有规格
		if (skuList != null && skuList.size() > 0) {

			// 判断sn是否和数据库重复，判断sn数组之间是否重复
			this.check(skuList, goodsid);

			// 清除库中的sku
			this.clean(goodsid);
			// 添加商品sku
			this.addGoodsSku(skuList, goods, goodsid, seller, shop);

		} else {
			// 清除缓存及库中的sku
			this.clean(goodsid);
			// 添加没有规格的sku信息
			this.addNoSku(goods, shop);
		}
		return skuList;
	}

	@Override
	public List<GoodsSkuVo> listByGoodsId(Integer goods_id) {
		String sql = "select * from es_draft_goods_sku where draft_goods_id =?";
		List<DraftGoodsSku> list = daoSupport.queryForList(sql, DraftGoodsSku.class, goods_id);
		List<GoodsSkuVo> result = new ArrayList<>();
		for (DraftGoodsSku sku : list) {
			GoodsSkuVo skuVo = new GoodsSkuVo(sku);
			result.add(skuVo);
		}
		return result;
	}

	/**
	 * 判断sn是否和数据库重复，判断sn数组之间是否重复
	 * 
	 * @param skuList
	 * @param goodsid
	 * @param seller
	 */
	private void check(List<GoodsSkuVo> skuList, Integer goodsid) {
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
		List<String> res = this.checkSns(snStr, goodsid);
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
	private List<String> checkSns(String snStr, Integer goods_id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select s.sn from es_draft_goods_sku s where s.sn in(?) ");
		if (goods_id == null) {
			goods_id = 0;
		}
		sql.append(" and s.draft_goods_id != ?");
		List<String> list = this.daoSupport.queryForList(sql.toString(), new StringMapper(), snStr, goods_id);
		return list;
	}

	/**
	 * 清除商品规格信息和sku信息 及缓存
	 */
	private void clean(Integer goodsid) {
		this.daoSupport.execute("delete from es_draft_goods_sku where draft_goods_id=? ", goodsid);
	}

	/**
	 * 添加商品sku 还有更改了sku组合时使用
	 */
	private void addGoodsSku(List<GoodsSkuVo> skuList, DraftGoods goods, Integer goodsid, Seller seller, Shop shop) {
		for (GoodsSkuVo sku : skuList) {
			sku.setGoods_name(goods.getGoods_name());
			// 规格字段修改成json
			String specJson = this.getSpecListJson(sku.getSpecList());
			sku.setSpecs(specJson);
			sku.setGoods_id(goodsid);
			// po vo转换
			DraftGoodsSku draftGoodsSku = new DraftGoodsSku(sku);
			this.daoSupport.insert("es_draft_goods_sku", draftGoodsSku);
		}
	}

	/**
	 * 添加没有规格的sku信息
	 * 
	 * @param goods
	 * @param shop
	 */
	private void addNoSku(DraftGoods goods, Shop shop) {
		DraftGoodsSku goodsSku = new DraftGoodsSku(goods);
		this.daoSupport.insert("es_draft_goods_sku", goodsSku);
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
		List<SpecValueVo> list = new ArrayList<>();
		for (SpecValueVo specvalue : specList) {
			if (specvalue.getSpec_type() == null) {
				specvalue.setSpec_type(0);
			}
			if (specvalue.getSpec_type() == 1) {
				GoodsGallery goodsGallery = draftGoodsGalleryManager.getGoodsGallery(specvalue.getSpec_image());
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
}
