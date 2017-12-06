package com.enation.app.shop.goods.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.shop.goods.model.po.DraftGoodsGallery;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.service.IDraftGoodsGalleryManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 草稿箱相册接口实现
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 下午8:03:43
 */
@Service
public class DraftGoodsGalleryManager implements IDraftGoodsGalleryManager {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private ISettingService settingService;
	@Autowired
	private UploadFactory uploadFactory;
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void add(List<GoodsGallery> galleryList, Integer goods_id) {
		for (int i = 0; i < galleryList.size(); i++) {
			/** 获取带所有缩略的相册 */
			GoodsGallery galley = this.getGoodsGallery(galleryList.get(i));
			galley.setGoods_id(goods_id);
			/** 默认第一个为默认图片 */
			if (i == 0) {
				galley.setIsdefault(1);
			} else {
				galley.setIsdefault(0);
			}
			DraftGoodsGallery draftGoodsGallery = new DraftGoodsGallery(galley);
			this.daoSupport.insert("es_draft_goods_gallery", draftGoodsGallery);
		}
	}

	@Override
	public void edit(List<GoodsGallery> galleryList, Integer goods_id) {
		/** 删除本商品相册 */
		this.daoSupport.execute("delete from es_draft_goods_gallery where  draft_goods_id = ? ", goods_id);
		/** 添加相册 */
		for (int i = 0; i < galleryList.size(); i++) {
			/** 获取带所有缩略的相册 */
			GoodsGallery galley = this.getGoodsGallery(galleryList.get(i));
			galley.setGoods_id(goods_id);
			/** 默认第一个为默认图片 */
			if (i == 0) {
				galley.setIsdefault(1);
			} else {
				galley.setIsdefault(0);
			}
			DraftGoodsGallery draftGoodsGallery = new DraftGoodsGallery(galley);
			this.daoSupport.insert("es_draft_goods_gallery", draftGoodsGallery);
		}

	}

	@Override
	public GoodsGallery getGoodsGallery(String original) {
		GoodsGallery goodsGallery = new GoodsGallery();
		goodsGallery.setOriginal(original);
		return this.getGoodsGallery(goodsGallery);
	}

	/**
	 * 传入一个只带原图的相册，返回带各种缩略图的先出个
	 * 
	 * @param goodsGallery
	 *            一个只带原图的相册
	 * @return
	 */
	private GoodsGallery getGoodsGallery(GoodsGallery goodsGallery) {
		/** 微图 */
		int tiny_pic_width = 60;
		int tiny_pic_height = 60;
		/** 缩略图 */
		int thumbnail_pic_width = 107;
		int thumbnail_pic_height = 107;
		/** 小图 */
		int small_pic_width = 320;
		int small_pic_height = 240;
		/** 大图 */
		int big_pic_width = 550;
		int big_pic_height = 412;

		try {
			tiny_pic_width = StringUtil.toInt(getSettingValue("tiny_pic_width"), false);
			tiny_pic_height = StringUtil.toInt(getSettingValue("tiny_pic_height"), false);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			thumbnail_pic_width = StringUtil.toInt(getSettingValue("thumbnail_pic_width"), false);
			thumbnail_pic_height = StringUtil.toInt(getSettingValue("thumbnail_pic_height"), false);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			small_pic_width = StringUtil.toInt(getSettingValue("small_pic_width"), false);
			small_pic_height = StringUtil.toInt(getSettingValue("small_pic_height"), false);
		} catch (Exception e) {
			logger.error(e);
		}
		try {
			big_pic_width = StringUtil.toInt(getSettingValue("big_pic_width"), false);
			big_pic_height = StringUtil.toInt(getSettingValue("big_pic_height"), false);
		} catch (Exception e) {
			logger.error(e);
		}

		/** 获取文件操作方案 */
		IUploader upload = uploadFactory.getUploader();
		/** 获取原图 */
		/** 微图 */
		String tiny = upload.getThumbnailUrl(goodsGallery.getOriginal(), tiny_pic_width, tiny_pic_height);
		/** 缩略图 */
		String thumbnail = upload.getThumbnailUrl(goodsGallery.getOriginal(), thumbnail_pic_width,
				thumbnail_pic_height);
		/** 小图 */
		String small = upload.getThumbnailUrl(goodsGallery.getOriginal(), small_pic_width, small_pic_height);
		/** 大图 */
		String big = upload.getThumbnailUrl(goodsGallery.getOriginal(), big_pic_width, big_pic_height);
		goodsGallery.setBig(big);
		goodsGallery.setSmall(small);
		goodsGallery.setThumbnail(thumbnail);
		goodsGallery.setTiny(tiny);
		return goodsGallery;
	}

	/***
	 * 获取系统参数中图片的尺寸
	 * 
	 * @param code
	 *            尺寸的key
	 * @return
	 */
	private String getSettingValue(String code) {
		return settingService.getSetting("photo", code);
	}

	@Override
	public List<DraftGoodsGallery> list(Integer goods_id) {
		List<DraftGoodsGallery> result = this.daoSupport.queryForList(
				"select gg.* from es_draft_goods_gallery gg left join es_draft_goods g on gg.draft_goods_id=g.draft_goods_id where gg.draft_goods_id = ? ORDER BY gg.sort",
				DraftGoodsGallery.class, goods_id);
		return result;
	}
}
