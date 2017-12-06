package com.enation.app.shop.goods.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.po.DraftGoods;
import com.enation.app.shop.goods.model.po.DraftGoodsGallery;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.service.IDraftGoodsGalleryManager;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.app.shop.goods.service.IGoodsGalleryManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 店铺商品信息
 * 
 * @author LiFenLong
 *
 */
@Component
public class StoreGoodsInfoTag extends BaseFreeMarkerTag {

	@Autowired
	private IGoodsManager goodsManager;

	@Autowired
	private IGoodsGalleryManager goodsGalleryManager;
	@Autowired
	private IDraftGoodsGalleryManager draftGoodsGalleryManager;
	@Autowired
	private IDraftGoodsManager draftGoodsManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result = new HashMap();
		// mode==1说明是草稿箱图片
		Integer mode = params.get("mode") == null ? 0 : Integer.valueOf(params.get("mode").toString());
		if (mode == 1) {
			List<DraftGoodsGallery> galleryList = draftGoodsGalleryManager
					.list(Integer.valueOf(params.get("goods_id").toString()));
			
			DraftGoods goods = draftGoodsManager.get(Integer.valueOf(params.get("goods_id").toString()));
			if (params.containsKey("store_id")) {
				Integer store_id = Integer.valueOf(params.get("store_id").toString());
				if (store_id == null || !store_id.equals(StringUtil.toInt(goods.getSeller_id().toString(), true))) {
					this.redirectTo404Html();
				}
			}
			result.put("goods", goods);
			this.getRequest().setAttribute("goods", goods);

			result.put("galleryList", galleryList);
		} else {
			List<GoodsGallery> galleryList = goodsGalleryManager
					.list(Integer.valueOf(params.get("goods_id").toString()));
			Goods goods = goodsManager.getFromDB(Integer.valueOf(params.get("goods_id").toString()));
			if (params.containsKey("store_id")) {
				Integer store_id = Integer.valueOf(params.get("store_id").toString());
				if (store_id == null || !store_id.equals(StringUtil.toInt(goods.getSeller_id().toString(), true))) {
					this.redirectTo404Html();
				}
			}
			result.put("goods", goods);
			this.getRequest().setAttribute("goods", goods);
			result.put("galleryList", galleryList);
		}
		return result;
	}
}
