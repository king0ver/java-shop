package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

/**
 * 赚品
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月28日上午9:21:13
 */
public class Gift  implements Serializable {
 
	private static final long serialVersionUID = 5952695019858873279L;

	private Integer gift_id;
	
	private Integer seller_id;
	
	private String gift_name;
	private Integer gift_num;
	
	public Integer getGift_id() {
		return gift_id;
	}

	public void setGift_id(Integer gift_id) {
		this.gift_id = gift_id;
	}

	public String getGift_name() {
		return gift_name;
	}

	public void setGift_name(String gift_name) {
		this.gift_name = gift_name;
	}

	public Integer getGift_num() {
		return gift_num;
	}

	public void setGift_num(Integer gift_num) {
		this.gift_num = gift_num;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public Gift(){

	}

	public void finalize() throws Throwable {

	}

}