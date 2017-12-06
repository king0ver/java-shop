package com.enation.app.base.core.model;

/**
 * 商家
 * @author Chopper
 * @version v1.0
 * @since v6.x
 * 2017年8月14日 下午3:45:39 
 *
 */
public class Seller extends Member{

	/** 
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	private static final long serialVersionUID = -3497337796900330426L;
	
	/**
	 * 店铺id
	 */
	private Integer store_id;
	
	/**
	 * 是否属于商家
	 */
	private Integer is_store;
 

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
 
	public Integer getIs_store() {
		return is_store;
	}

	public void setIs_store(Integer is_store) {
		this.is_store = is_store;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
 
	
	
	
	
}
