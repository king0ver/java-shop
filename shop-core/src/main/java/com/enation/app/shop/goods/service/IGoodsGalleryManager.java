package com.enation.app.shop.goods.service;


import java.util.List;

import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.po.GoodsGallery;



/**
 * 
 * 新相册明细数据管理接口 
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月19日 上午10:32:11
 */
@Component
public interface IGoodsGalleryManager {
	/**
	 * 添加商品相册
	 * @param galleryList 相册集合
	 * @param goods_id	  商品id
	 */
	public void add(List<GoodsGallery> galleryList,Integer goods_id);
	/**
	 * 修改商品相册
	 * @param galleryList 相册集合
	 * @param goods_id	 商品id
	 */
	public void edit(List<GoodsGallery> galleryList,Integer goods_id);
	/**
	 * 删除相册信息
	 * @param goodsIds	商品id数组
	 */
	public void delete(Integer[] goodsIds);
	/**
	 * 获取商品相册信息
	 * @param goods_id	商品信息
	 * @return
	 */
	public List<GoodsGallery> list(Integer goods_id); 
	/**
	 * 根据原图获取相关缩略图
	 * @param original 原图地址
	 * @return
	 */
	public GoodsGallery getGoodsGallery(String original);
	/**
	 * 根据商品id返回此商品默认的缩略图对象
	 * @param goods_id
	 * @return
	 */
	public GoodsGallery getGoodsGallery(Integer goods_id);


}
