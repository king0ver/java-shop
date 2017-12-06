package com.enation.app.shop.trade.support;

import java.io.Serializable;

/**
 * 订单价格信息
 * @author kingapex
 * @version v1.0
 * @since v6.2
 * 2016年11月14日上午11:53:06
 */
public class OrderPrice  implements Serializable{
	
 
	private static final long serialVersionUID = -9085768224230271752L;

	/**
	 * 商品价格，经过优惠过的
	 */
	private Double goodsPrice;
		
	/**
	 * 订单总价，商品价格+运费
	 */
	private Double orderPrice;

	/**
	 * 配送费用
	 */
	private Double shippingPrice; 
	
	/**
	 * 需要支付的金额(应付款)
	 */
	private Double needPayMoney; 
	
	/**
	 * 优惠的价格总计
	 */
	private Double discountPrice; 
	
	
	/**
	 * 是否免运费
	 */
	private boolean isFreeShip= false;
	
 
	/**
	 * 商品重量
	 */
	private Double weight ; 
 

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Double getShippingPrice() {
		return shippingPrice;
	}

	public void setShippingPrice(Double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}

	public Double getNeedPayMoney() {
		return needPayMoney;
	}

	public void setNeedPayMoney(Double needPayMoney) {
		this.needPayMoney = needPayMoney;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public boolean getIsFreeShip() {
		return isFreeShip;
	}

	public void setFreeShip(boolean isFreeShip) {
		this.isFreeShip = isFreeShip;
	}
	
 
	
	
}
