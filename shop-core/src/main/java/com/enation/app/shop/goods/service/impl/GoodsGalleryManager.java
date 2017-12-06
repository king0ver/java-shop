package com.enation.app.shop.goods.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.service.IGoodsGalleryManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;

@Service("goodsGalleryManager")
public class GoodsGalleryManager implements IGoodsGalleryManager {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private UploadFactory uploadFactory;
	@Autowired
	private ISettingService settingService;
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void add(List<GoodsGallery> galleryList, Integer goods_id) {
		for (int i = 0; i < galleryList.size(); i++) {
			/** 获取带所有缩略的相册 */
			GoodsGallery galley = this.getGoodsGallery(galleryList.get(i));
			galley.setGoods_id(goods_id);
			galley.setSort(galleryList.get(i).getSort());
			/** 默认第一个为默认图片 */
			if(i == 0) {
				galley.setIsdefault(1);
			}else {
				galley.setIsdefault(0);
			}
			this.daoSupport.insert("es_goods_gallery", galley);
		}
	}
	/**
	 * 从前台传入商品相册id，如果为-1则此商品图片为新增图片，需要添加操作，否则为原有图片,无需操作
	 * 1、将传入的商品图片id进行拼接
	 * 2、删除掉不在此商品相册中得图片
	 * 3、如果前台传入id不为-1，则新增商品图片到此商品的相册中
	 * 本次修改 先要删除此商品的相册信息，然后添加
	 */
	@Override
	public void edit(List<GoodsGallery> galleryList, Integer goods_id) {
		/** 删除没有用到的商品相册信息 */
		this.delNoUseGalley(galleryList, goods_id);
		int i = 0;
		/** 如果前台传入id不为-1，则新增商品图片到此商品的相册中 添加相册 */
		for (GoodsGallery goodsGallery : galleryList) {
			if(goodsGallery.getImg_id() != -1 && i == 0) {
				/** 将此图片设置为默认 */
				this.daoSupport.execute("update es_goods_gallery set isdefault = 1 where img_id = ? ", goodsGallery.getImg_id());
				/** 将其他图片设置为不默认 */
				this.daoSupport.execute("update es_goods_gallery set isdefault = 0 where img_id != ? ", goodsGallery.getImg_id());
				GoodsGallery temp = this.getGoodsGallery(goods_id);
				goodsGallery.setBig(temp.getBig());
				goodsGallery.setOriginal(temp.getOriginal());
				goodsGallery.setSmall(temp.getSmall());
				goodsGallery.setThumbnail(temp.getThumbnail());
				goodsGallery.setTiny(temp.getTiny());
			}
			if(galleryList.get(i).getImg_id() == -1) {
				/** 获取带所有缩略的相册 */
				GoodsGallery galley = this.getGoodsGallery(goodsGallery);
				galley.setGoods_id(goods_id);
				/** 默认第一个为默认图片 */
				if(i == 0) {
					galley.setIsdefault(1);
				}else {
					galley.setIsdefault(0);
				}
				this.daoSupport.insert("es_goods_gallery", galley);
			}
			i++;
			this.daoSupport.execute("update es_goods_gallery set sort = ? where img_id = ? ",goodsGallery.getSort(), goodsGallery.getImg_id());
		}
	}

	@Override
	public void delete(Integer[] goodsIds) {
		for (int i = 0; i < goodsIds.length; i++) {
			this.daoSupport.execute("delete from es_goods_gallery where goods_id = ?", goodsIds[i]);
		}
	}

	@Override
	public List<GoodsGallery> list(Integer goods_id) {
		List<GoodsGallery> result = this.daoSupport.queryForList("select gg.* from es_goods_gallery gg left join es_goods g on gg.goods_id=g.goods_id where gg.goods_id = ? ORDER BY gg.sort desc", GoodsGallery.class, goods_id);
		return result;
	}


	/***
	 * 获取系统参数中图片的尺寸
	 * @param code 尺寸的key
	 * @return
	 */
	private String getSettingValue(String code) {
		return settingService.getSetting("photo", code);
	}
	/**
	 * 传入一个只带原图的相册，返回带各种缩略图的先出个
	 * @param goodsGallery 一个只带原图的相册
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
			tiny_pic_width = StringUtil.toInt(getSettingValue("tiny_pic_width"),false);
			tiny_pic_height = StringUtil.toInt(getSettingValue("tiny_pic_height"),false);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			thumbnail_pic_width = StringUtil.toInt(getSettingValue("thumbnail_pic_width"),false);
			thumbnail_pic_height = StringUtil.toInt(getSettingValue("thumbnail_pic_height"),false);
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			small_pic_width = StringUtil.toInt(getSettingValue("small_pic_width"),false);
			small_pic_height = StringUtil.toInt(getSettingValue("small_pic_height"),false);
		} catch (Exception e) {
			logger.error(e);
		}
		try {
			big_pic_width = StringUtil.toInt(getSettingValue("big_pic_width"),false);
			big_pic_height = StringUtil.toInt(getSettingValue("big_pic_height"),false);
		} catch (Exception e) {
			logger.error(e);
		}

		/** 获取文件操作方案 */
		IUploader upload = uploadFactory.getUploader();
		/** 获取原图 */
		/** 微图 */
		String tiny = upload.getThumbnailUrl(goodsGallery.getOriginal(), tiny_pic_width, tiny_pic_height);
		/** 缩略图 */
		String thumbnail = upload.getThumbnailUrl(goodsGallery.getOriginal(), thumbnail_pic_width, thumbnail_pic_height);
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

	@Override
	public GoodsGallery getGoodsGallery(String original) {
		GoodsGallery goodsGallery = new GoodsGallery();
		goodsGallery.setOriginal(original);
		return this.getGoodsGallery(goodsGallery);
	}

	@Override
	public GoodsGallery getGoodsGallery(Integer goods_id) {
		GoodsGallery goodsGallery = this.daoSupport.queryForObject("select * from es_goods_gallery where isdefault = 1 and goods_id= ?",GoodsGallery.class,goods_id);
		return goodsGallery;
	}
	/**
	 * 删除没有用到的商品相册信息
	 * @param galleryList	商品相册
	 * @param goods_id		商品id
	 */
	private void delNoUseGalley(List<GoodsGallery> galleryList, Integer goods_id) {
		/** 将传入的商品图片id进行拼接 */
		List<Object> img_ids = new ArrayList<>();
		String[] temp = new String[galleryList.size()];
		if(galleryList.size() > 0) {
			for (int i = 0; i < galleryList.size(); i++) {
				img_ids.add(galleryList.get(i).getImg_id());
				temp[i] = "?";
			}
		}
		String str = StringUtil.arrayToString(temp, ",");
		img_ids.add(goods_id);
		/** 删除掉不在此商品相册中得图片 */
		this.daoSupport.execute("delete from es_goods_gallery where img_id not in("+str+") and goods_id = ?", img_ids.toArray());
	}
}