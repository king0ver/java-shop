package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;


/**
 * 处理价格vo
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月30日下午10:32:21
 */
public class PriceProcessParam implements Serializable {
	
	private Cart cart;
	private Consignee consignee;
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public Consignee getConsignee() {
		return consignee;
	}
	public void setConsignee(Consignee consignee) {
		this.consignee = consignee;
	}
	
	
	
}
