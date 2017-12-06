package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 优惠合规校验参数
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月31日下午12:55:23
 */
public class PromotionCheckParam implements Serializable  {
 
	private static final long serialVersionUID = -6807112067207117569L;
	
	private  List<Cart> cartList;
	
	private Consignee consignee;

	public List<Cart> getCartList() {
		return cartList;
	}

	public void setCartList(List<Cart> cartList) {
		this.cartList = cartList;
	}

	public Consignee getConsignee() {
		return consignee;
	}

	public void setConsignee(Consignee consignee) {
		this.consignee = consignee;
	}
	
	
	
	
}
