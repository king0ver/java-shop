package com.enation.app.shop.trade.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 购物车展示Vo
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月23日14:22:48
 */
@ApiModel(description = "购物车展示Vo")
public class CartVo extends Cart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5700770655258488075L;
	
	/**
	 * 把Cart.productList 数据 根据促销活动压入到此集合中。
	 */
	@ApiModelProperty(value = "促销活动集合（包含商品")
	private List<GroupPromotionVo>  promotionList;
	
	/**
	 * 1为店铺商品全选状态
	 * 0位非全选
	 */
	@ApiModelProperty(value = "购物车页展示时，店铺内的商品是否全选状态")
	private int checked;


	public List<GroupPromotionVo> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(List<GroupPromotionVo> promotionList) {
		this.promotionList = promotionList;
	}
	


	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public CartVo(){
		this.setChecked(1);
	}

	/**
	 * 父类的构造器
	 * @param sellerid
	 * @param sellerName
	 */
	public CartVo(int sellerid, String sellerName) {
		super.setSeller_id(sellerid);
		super.setSeller_name(sellerName);
		super.setPrice(new PriceDetail());
		super.setProductList(new ArrayList<Product>());
		super.setCouponList(new ArrayList<Coupon>());
		super.setGiftList(new ArrayList<FullDiscountGift>());
		super.setGiftCouponList(new ArrayList<Coupon>());
		this.setChecked(1);
	}
	
}
