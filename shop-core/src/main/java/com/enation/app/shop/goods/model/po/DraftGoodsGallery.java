package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 
 * 草稿箱po
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午10:25:35
 */
public class DraftGoodsGallery implements Serializable {
	private static final long serialVersionUID = 9080351034317157767L;

	private int img_id;
	private int draft_goods_id;
	private String thumbnail;// 列表尺寸，用于各商品列表中
	private String small;// 小尺寸，用于商品详细中preview
	private String big;// 大尺寸
	private String original;// 原尺寸
	private String tiny;// 极小的，用于商品详细页中主图下的小列表图
	private int isdefault;

	/** 排序 */
	private int sort; // 图片排序

	public DraftGoodsGallery() {

	}

	public DraftGoodsGallery(GoodsGallery goodsGallery) {
		this.draft_goods_id = goodsGallery.getGoods_id();
		this.thumbnail = goodsGallery.getThumbnail();
		this.small = goodsGallery.getSmall();
		this.big = goodsGallery.getBig();
		this.original = goodsGallery.getOriginal();
		this.tiny = goodsGallery.getTiny();
		this.isdefault = goodsGallery.getIsdefault();
	}

	@PrimaryKeyField
	public int getImg_id() {
		return img_id;
	}

	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}

	public int getDraft_goods_id() {
		return draft_goods_id;
	}

	public void setDraft_goods_id(int draft_goods_id) {
		this.draft_goods_id = draft_goods_id;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getBig() {
		return big;
	}

	public void setBig(String big) {
		this.big = big;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getTiny() {
		return tiny;
	}

	public void setTiny(String tiny) {
		this.tiny = tiny;
	}

	public int getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
