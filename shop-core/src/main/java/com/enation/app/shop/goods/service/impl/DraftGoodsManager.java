package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.DraftGoods;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IDraftGoodsGalleryManager;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.app.shop.goods.service.IDraftGoodsParamsManager;
import com.enation.app.shop.goods.service.IDraftGoodsSkuManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.shop.apply.model.po.Shop;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.shop.goods.service.IStoreGoodsCatManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 草稿箱操作接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 下午8:03:22
 */
@Service
public class DraftGoodsManager implements IDraftGoodsManager {
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ISellerManager sellerMangager;
	@Autowired
	private IDraftGoodsGalleryManager draftGoodsGalleryManager;
	@Autowired
	private IShopManager shopManager;
	@Autowired
	private IDraftGoodsSkuManager draftGoodsSkuManager;
	@Autowired
	private IDraftGoodsParamsManager draftGoodsParamsManager;
	@Autowired
	private IStoreGoodsCatManager storeGoodsCatManager;
	@Autowired
	private IGoodsManager goodsManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DraftGoods add(GoodsVo goodsVo) {
		Seller seller = sellerMangager.getSeller();
		Shop shop = shopManager.getShop();
		goodsVo.setSeller_id(seller.getStore_id());
		// 没有规格给这个字段塞0
		if (goodsVo.getSkuList() != null && goodsVo.getSkuList().size() > 0) {
			goodsVo.setHave_spec(1);
		} else {
			goodsVo.setHave_spec(0);
		}
		DraftGoods draftGoods = new DraftGoods(goodsVo);
		// 商品状态 是否可用
		draftGoods.setDisabled(0);// 商品状态 是否可用
		draftGoods.setCreate_time(DateUtil.getDateline());// 商品创建时间
		draftGoods.setQuantity(goodsVo.getQuantity());// 商品库存
		draftGoods.setEnable_quantity(goodsVo.getQuantity());// 可用库存
		draftGoods.setSeller_id(goodsVo.getSeller_id());// 店铺id
		if (goodsVo.getGoodsGalleryList() != null && goodsVo.getGoodsGalleryList().size() != 0) {
			GoodsGallery goodsGalley = draftGoodsGalleryManager
					.getGoodsGallery(goodsVo.getGoodsGalleryList().get(0).getOriginal());// 向goods加入图片
			draftGoods.setOriginal(goodsVo.getGoodsGalleryList().get(0).getOriginal());
			draftGoods.setBig(goodsGalley.getBig());
			draftGoods.setSmall(goodsGalley.getSmall());
			draftGoods.setThumbnail(goodsGalley.getThumbnail());
		}

		draftGoods.setSeller_name(shop.getShop_name());
		// 添加草稿箱商品
		this.daoSupport.insert("es_draft_goods", draftGoods);
		// 获取添加商品的商品ID
		Integer draft_goods_id = this.daoSupport.getLastId("es_draft_goods");
		if (draft_goods_id == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "添加失败,请刷新页面后重新添加");
		}
		goodsVo.setGoods_id(draft_goods_id);
		// 添加商品参数
		if (goodsVo.getGoodsParamsList() != null && goodsVo.getGoodsParamsList().size() != 0) {
			draftGoodsParamsManager.addParams(goodsVo.getGoodsParamsList(), draft_goods_id);
		}
		// 添加商品sku信息
		draftGoodsSkuManager.add(goodsVo.getSkuList(), draft_goods_id);
		if (goodsVo.getGoodsGalleryList() != null && goodsVo.getGoodsGalleryList().size() != 0) {
			draftGoodsGalleryManager.add(goodsVo.getGoodsGalleryList(), draft_goods_id);
		}

		return draftGoods;
	}

	@Override
	public GoodsVo addMarcket(GoodsVo goodsVo) throws Exception {
		Integer goodsid[] = { goodsVo.getGoods_id() };
		this.delete(goodsid);
		goodsManager.add(goodsVo);
		return null;
	}

	@Override
	public String delete(Integer[] goodsid) {
		if (goodsid != null) {
			List<Object> term = new ArrayList<>();
			String[] goods = new String[goodsid.length];
			for (int i = 0; i < goodsid.length; i++) {
				goods[i] = "?";
				term.add(goodsid[i]);
			}
			String id_str = StringUtil.arrayToString(goods, ",");
			String sql = "update es_draft_goods set disabled=1  where draft_goods_id in (" + id_str + ")";

			this.daoSupport.execute(sql, term.toArray());
		}
		return "删除成功";
	}

	@Override
	public DraftGoods edit(GoodsVo goodsVo) {
		Seller seller = sellerMangager.getSeller();
		goodsVo.setSeller_id(seller.getStore_id());
		goodsVo.setDisabled(0);
		DraftGoods goods = new DraftGoods(goodsVo);
		goods.setQuantity(goodsVo.getQuantity());
		goods.setEnable_quantity(goodsVo.getQuantity());
		// 向goods加入图片
		if (goodsVo.getGoodsGalleryList() != null && goodsVo.getGoodsGalleryList().size() != 0) {
			GoodsGallery goodsGalley = draftGoodsGalleryManager
					.getGoodsGallery(goodsVo.getGoodsGalleryList().get(0).getOriginal());
			goods.setOriginal(goodsGalley.getOriginal());
			goods.setBig(goodsGalley.getBig());
			goods.setSmall(goodsGalley.getSmall());
			goods.setThumbnail(goodsGalley.getThumbnail());
		}

		this.daoSupport.update("es_draft_goods", goods, "draft_goods_id=" + goodsVo.getGoods_id());
		if (goods.getDraft_goods_id() == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "修改失败,请刷新页面后重新修改");
		}
		// 处理参数信息
		draftGoodsParamsManager.addParams(goodsVo.getGoodsParamsList(), goodsVo.getGoods_id());
		// 处理规格信息
		draftGoodsSkuManager.edit(goodsVo.getSkuList(), goodsVo.getGoods_id(), goodsVo.getHas_changed());
		// 修改相册信息
		if (goodsVo.getGoodsGalleryList() != null && goodsVo.getGoodsGalleryList().size() != 0) {
			draftGoodsGalleryManager.edit(goodsVo.getGoodsGalleryList(), goodsVo.getGoods_id());
		}
		return null;
	}

	@Override
	public DraftGoods get(Integer goodsid) {
		String sql = "select * from es_draft_goods where draft_goods_id = ?";
		DraftGoods goodsDB = this.daoSupport.queryForObject(sql, DraftGoods.class, goodsid);
		return goodsDB;
	}

	@Override
	public List<GoodsSkuVo> draftGoodsSkuList(Integer goods_id) {
		String sql = "select * from es_draft_goods where draft_goods_id = ?";
		DraftGoods goodsDB = this.daoSupport.queryForObject(sql, DraftGoods.class, goods_id);
		if (goodsDB == null) {
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "商品不存在");
		}
		List<GoodsSkuVo> listByGoodsId = draftGoodsSkuManager.listByGoodsId(goods_id);
		GoodsVo goodsVo = new GoodsVo(goodsDB);
		goodsVo.setSkuList(listByGoodsId);
		return listByGoodsId;
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

	@Override
	public Page draftGoodsList(Integer pageNo, Integer pageSize, Map map) {
		Integer store_id = Integer.valueOf(map.get("store_id").toString());
		Integer disable = Integer.valueOf(map.get("disable") + "");
		String store_cat = String.valueOf(map.get("store_cat"));
		String goodsName = String.valueOf(map.get("goodsName"));
		StringBuffer sql = new StringBuffer(
				"SELECT g.*,c.store_cat_name from es_draft_goods g LEFT JOIN es_store_cat c ON g.shop_cat_id=c.store_cat_id where g.seller_id="
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
		sql.append(" order by g.create_time desc");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}

	/**
	 * 删除草稿箱商品
	 */
	private void deleteDraftGoods(GoodsVo goodsVo) {
		DraftGoods draftGoods = new DraftGoods(goodsVo);
		String sql = "update es_draft_goods set disabled=1 where draft_goods_id=?";
		this.daoSupport.execute(sql, draftGoods.getDraft_goods_id());
	}

}
