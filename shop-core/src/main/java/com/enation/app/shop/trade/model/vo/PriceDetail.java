package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

import com.enation.framework.util.CurrencyUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单价格信息
 * @since v6.2 
 * @author kingapex
 * @version v1.0
 * @created 2017年08月17日
 */
@ApiModel(value="PriceDetail", description = "价格明细")
public class PriceDetail  implements Serializable{

 
	@Override
	public String toString() {
		return "PriceDetail [total_price=" + total_price + ", goods_price=" + goods_price + ", freight_price="
				+ freight_price + ", discount_price=" + discount_price + ", is_free_freight=" + is_free_freight
				+ ", exchange_point=" + exchange_point + "]";
	}

	private static final long serialVersionUID = -960537582096338500L;
	
	@ApiModelProperty(value = "总价")
	private double  total_price;
	
	@ApiModelProperty(value = "商品价格" )
	private double  goods_price;
	
 
	@ApiModelProperty(value = "配送费" )
	private double  freight_price;
	
	/**
	 * 此金额 = 各种优惠工具的优惠金额 + 优惠券优惠的金额 
	 */
	@ApiModelProperty(value = "优惠金额" )
	private double  discount_price;
 
	/**
	 * 1为免运费
	 */
	@ApiModelProperty(value = "是否免运费" )
	private int is_free_freight;
	
	@ApiModelProperty(value = "积分" )
	private int exchange_point;
	
	
	/**
	 * 价格累加运算
	 * @param price
	 * @return
	 */
	public PriceDetail plus(PriceDetail price){
		
		double total =  CurrencyUtil.add(total_price, price.getTotal_price());
		double goods  = CurrencyUtil.add(goods_price, price.getGoods_price());
		double freight = CurrencyUtil.add(this.freight_price, price.getFreight_price());
		double discount = CurrencyUtil.add(this.discount_price, price.getDiscount_price());
		int point = this.exchange_point+price.getExchange_point();
		
		PriceDetail newPrice  = new PriceDetail();
		newPrice.setTotal_price(total);
		newPrice.setGoods_price(goods);
		newPrice.setFreight_price(freight);
		newPrice.setDiscount_price(discount);
		newPrice.setExchange_point(point);
		newPrice.setIs_free_freight(price.getIs_free_freight());
		return newPrice;
	}
	
	/**
	 * 当前店铺总价计算
	 */
	public void countPrice(){
		//购物车内当前商家的商品原价总计
		Double goods_Price = this.getGoods_price();
		
		//购物车内当前商家的促销优惠金额总计（不含优惠券）
		Double discount_price = this.getDiscount_price();
		
		//购物车内当前商家的配送金额总计
		Double freight_price = this.getFreight_price();
		
		//购物车内当前商家的应付金额总计
		//运算过程=商品原价总计-优惠金额总计+配送费用
		Double total_price = CurrencyUtil.add(CurrencyUtil.sub(goods_Price, discount_price), freight_price);
		
		//防止金额为负数
		if(total_price.doubleValue()<=0){
			total_price = 0d;
			discount_price = 0d;
		}
		this.setTotal_price(total_price);
		
	}
	
	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public double getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}

	public double getFreight_price() {
		return freight_price;
	}

	public void setFreight_price(double freight_price) {
		this.freight_price = freight_price;
	}

	public double getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(double discount_price) {
		this.discount_price = discount_price;
	}

 
	public int getIs_free_freight() {
		return is_free_freight;
	}

	public void setIs_free_freight(int is_free_freight) {
		this.is_free_freight = is_free_freight;
	}

	public PriceDetail(){

	}

	public int getExchange_point() {
		return exchange_point;
	}

	public void setExchange_point(int exchange_point) {
		this.exchange_point = exchange_point;
	}

	public void finalize() throws Throwable {

	}

}