package com.enation.app.shop.trade.support;

import com.enation.app.shop.trade.model.vo.PriceDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 购物车总价
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月22日下午8:27:52
 */
@ApiModel(value="CartTotalPrice", description = "购物车总价")
public class CartTotalPrice {
	
	
	public   CartTotalPrice( ) {
	}
	
	
	public   CartTotalPrice(PriceDetail priceDetail) {
		
		this.goods_price = priceDetail.getGoods_price();
		this.discount_price = priceDetail.getDiscount_price();
		
		
	}
	
	@ApiModelProperty(value = "商品价格" )
	private double  goods_price;
	
	@ApiModelProperty(value = "优惠金额" )
	private double  discount_price;

	public double getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}

	public double getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(double discount_price) {
		this.discount_price = discount_price;
	}
	
	
	
	
}
