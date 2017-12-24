package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
 
/**
 * 购物车模型
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月21日下午7:38:11
 */
@ApiModel( description = "购物车")
public class Cart implements Serializable{
 
	private static final long serialVersionUID = 7525646695959000671L;
	
	
	@ApiModelProperty(value = "卖家id" )
	private int seller_id;
	
	@ApiModelProperty(value = "选中的配送方式id" )
	private int shipping_type_id;
	
	
	@ApiModelProperty(value = "选中的配送方式名称" )
	private String shipping_type_name;
	
	
	@ApiModelProperty(value = "卖家店名" )
	private String seller_name;
 
	
	@ApiModelProperty(value = "购物车重量" )
	private Double weight;
	
	@ApiModelProperty(value = "购物车价格")
	private PriceDetail price;
	
	@ApiModelProperty(value = "购物车中的产品列表" )
	private List<Product> productList;
	
	@ApiModelProperty(value = "已使用的优惠卷列表")
	private List<Coupon> couponList;
 
	@ApiModelProperty(value = "赠品列表" ) 
	private List<FullDiscountGift> giftList;
	
	@ApiModelProperty(value = "赠送优惠卷列表")
	private List<Coupon> giftCouponList;
	
	@ApiModelProperty(value = "赠送积分")
	private int giftPoint;
	
	
	public Cart(){}

	/**
	 * 在构造器中初始化属主、产品列表、促销列表及优惠卷列表
	 */
	public Cart( int _sellerid,String _sellername){
		this.seller_id = _sellerid;
		this.seller_name = _sellername;
		price = new PriceDetail();
		productList = new ArrayList<Product>();
		couponList = new ArrayList<Coupon>();
		giftCouponList = new ArrayList<Coupon>();
		giftList = new ArrayList<FullDiscountGift>();
	}
	
	
	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
 

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public PriceDetail getPrice() {
		return price;
	}

	public void setPrice(PriceDetail price) {
		this.price = price;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public List<Coupon> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<Coupon> couponList) {
		this.couponList = couponList;
	}


	public List<FullDiscountGift> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<FullDiscountGift> giftList) {
		this.giftList = giftList;
	}

	public int getShipping_type_id() {
		return shipping_type_id;
	}

	public void setShipping_type_id(int shipping_type_id) {
		this.shipping_type_id = shipping_type_id;
	}
 

	public String getShipping_type_name() {
		return shipping_type_name;
	}

	public void setShipping_type_name(String shipping_type_name) {
		this.shipping_type_name = shipping_type_name;
	}

	public List<Coupon> getGiftCouponList() {
		return giftCouponList;
	}

	public void setGiftCouponList(List<Coupon> giftCouponList) {
		this.giftCouponList = giftCouponList;
	}

	public int getGiftPoint() {
		return giftPoint;
	}

	public void setGiftPoint(int giftPoint) {
		this.giftPoint = giftPoint;
	}

	public void finalize() throws Throwable {

	}

}