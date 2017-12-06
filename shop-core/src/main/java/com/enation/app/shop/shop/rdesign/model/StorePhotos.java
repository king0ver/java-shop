package com.enation.app.shop.shop.rdesign.model;

import com.enation.framework.database.NotDbField;

/**
 * 
 * (店铺相册) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年4月10日 上午10:05:46
 */
public class StorePhotos {
	private Integer photo_id;	//图片Id
	private Integer store_id;	//店铺Id
	private String img;			//图片
	
	

	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Integer getPhoto_id() {
		return photo_id;
	}
	public void setPhoto_id(Integer photo_id) {
		this.photo_id = photo_id;
	}
	
}
