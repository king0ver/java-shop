package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;

import com.enation.app.shop.goods.model.po.SpecValue;

/**
 * 
 * 规格值实体vo
 * 
 * @author yanlin
 * @version v1.0 微服务迁移
 * @since v6.4.0
 * @date 2017年8月28日 下午2:52:11
 */
public class SpecValueVo extends SpecValue implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1426807099688672502L;
	/**
	 * 商品sku——id
	 */
	private Integer sku_id;
	/**
	 * 商品大图
	 */
	private String big;
	/**
	 * 商品小图
	 */
	private String small;
	/**
	 * 商品缩略图
	 */
	private String thumbnail;
	/**
	 * 商品极小图
	 */
	private String tiny;

	public SpecValueVo() {
	}

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public String getBig() {
		return big;
	}

	public void setBig(String big) {
		this.big = big;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTiny() {
		return tiny;
	}

	public void setTiny(String tiny) {
		this.tiny = tiny;
	}

}
