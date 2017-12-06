package com.enation.app.shop.goods.service;

import java.util.List;

import com.enation.app.shop.goods.model.po.DraftGoodsGallery;
import com.enation.app.shop.goods.model.po.GoodsGallery;

/**
 * 
 * 草稿箱商品相册接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午9:51:33
 */
public interface IDraftGoodsGalleryManager {
	/**
	 * 添加商品相册
	 * 
	 * @param galleryList
	 *            相册集合
	 * @param goods_id
	 *            商品id
	 */
	public void add(List<GoodsGallery> galleryList, Integer goods_id);

	/**
	 * 修改商品相册
	 * 
	 * @param galleryList
	 *            相册集合
	 * @param goods_id
	 *            商品id
	 */
	public void edit(List<GoodsGallery> galleryList, Integer goods_id);

	/**
	 * 根据原图获取相关缩略图
	 * 
	 * @param original
	 *            原图地址
	 * @return
	 */
	public GoodsGallery getGoodsGallery(String original);

	/**
	 * 获取商品相册信息
	 * 
	 * @param goods_id
	 *            商品信息
	 * @return
	 */
	public List<DraftGoodsGallery> list(Integer goods_id);
}
