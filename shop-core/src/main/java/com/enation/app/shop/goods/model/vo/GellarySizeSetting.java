package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 图片大小设置
 * @author fk
 * @version v1.0
 * 2017年6月14日 下午1:55:20
 */
@ApiModel
public class GellarySizeSetting implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7177185021075507835L;
	
	public static final String THUMBNAIL_PIC_HEIGHT = "thumbnail_pic_height";
	public static final String THUMBNAIL_PIC_WIDTH = "thumbnail_pic_width";
	public static final String BIG_PIC_HEIGHT = "big_pic_height";
	public static final String BIG_PIC_WIDTH = "big_pic_width";
	public static final String TINY_PIC_HEIGHT = "tiny_pic_height";
	public static final String TINY_PIC_WIDTH = "tiny_pic_width";
	public static final String SMALL_PIC_HEIGHT = "small_pic_height";
	public static final String SMALL_PIC_WIDTH = "small_pic_width";
	
	@ApiModelProperty(value = "缩略图高")
	private String thumbnail_pic_height;
	@ApiModelProperty(value = "缩略图宽")
	private String thumbnail_pic_width;
	@ApiModelProperty(value = "商品相册图片高")
	private String big_pic_height;
	@ApiModelProperty(value = "商品相册图片宽")
	private String big_pic_width;
	@ApiModelProperty(value = "商品详细页小图宽")
	private String tiny_pic_width;
	@ApiModelProperty(value = "商品详细页小图高")
	private String tiny_pic_height;
	@ApiModelProperty(value = "商品详细页图片高")
	private String small_pic_height;
	@ApiModelProperty(value = "商品详细页图片宽")
	private String small_pic_width;
	
	public String getThumbnail_pic_height() {
		return thumbnail_pic_height;
	}
	public void setThumbnail_pic_height(String thumbnail_pic_height) {
		this.thumbnail_pic_height = thumbnail_pic_height;
	}
	public String getThumbnail_pic_width() {
		return thumbnail_pic_width;
	}
	public void setThumbnail_pic_width(String thumbnail_pic_width) {
		this.thumbnail_pic_width = thumbnail_pic_width;
	}
	public String getBig_pic_height() {
		return big_pic_height;
	}
	public void setBig_pic_height(String big_pic_height) {
		this.big_pic_height = big_pic_height;
	}
	public String getBig_pic_width() {
		return big_pic_width;
	}
	public void setBig_pic_width(String big_pic_width) {
		this.big_pic_width = big_pic_width;
	}
	public String getTiny_pic_width() {
		return tiny_pic_width;
	}
	public void setTiny_pic_width(String tiny_pic_width) {
		this.tiny_pic_width = tiny_pic_width;
	}
	public String getTiny_pic_height() {
		return tiny_pic_height;
	}
	public void setTiny_pic_height(String tiny_pic_height) {
		this.tiny_pic_height = tiny_pic_height;
	}
	public String getSmall_pic_height() {
		return small_pic_height;
	}
	public void setSmall_pic_height(String small_pic_height) {
		this.small_pic_height = small_pic_height;
	}
	public String getSmall_pic_width() {
		return small_pic_width;
	}
	public void setSmall_pic_width(String small_pic_width) {
		this.small_pic_width = small_pic_width;
	}
}
