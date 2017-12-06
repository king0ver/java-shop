package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 组合商品活动模型
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月23日16:06:48
 */
public class GroupPromotionVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3188594102818543304L;

	/**
	 * 促销活动工具类型 存储PromotionTypeEnum.XXX.getType();
	 */
	@ApiModelProperty(value = "促销活动工具类型")
	private String promotion_type;
	
	/**
	 * 根据以上的活动工具类型 存储对应的Vo<br>
	 * 例如：上面的类型为groupbuy，那么此处的则为GroupbuyVo
	 */
	@ApiModelProperty(value = "活动详情")
	private Object activity_detail;
	
	@ApiModelProperty(value = "商品集合")
	private List<Product> productList;
	
	/**
	 * 商家价格小计 = 商品集合中小计的总和。
	 */
	@ApiModelProperty(value = "商品价格小计")
	private Double subtotal;
	
	/**
	 * 1为是组合活动，2为单品活动
	 */
	@ApiModelProperty(value = "是否是组合活动")
	private Integer is_group;
	
	@ApiModelProperty(value = "差额")
	private double spreadPrice;
	
	@ApiModelProperty(value = "优惠金额")
	private double discountPrice;
	
	/**
	 * 购物车页-满优惠活动是否选中状态
	 * 1为选中
	 */
	@ApiModelProperty(value = "活动是否选中")
	private int checked;

	
	public Object getActivity_detail() {
		return activity_detail;
	}

	public void setActivity_detail(Object activity_detail) {
		this.activity_detail = activity_detail;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Double getSubtotal() {
		if(subtotal==null){
			return 0.0;
		}
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public String getPromotion_type() {
		return promotion_type;
	}

	public void setPromotion_type(String promotion_type) {
		this.promotion_type = promotion_type;
	}

	public Integer getIs_group() {
		return is_group;
	}

	public void setIs_group(Integer is_group) {
		this.is_group = is_group;
	}

	public double getSpreadPrice() {
		return spreadPrice;
	}

	public void setSpreadPrice(double spreadPrice) {
		this.spreadPrice = spreadPrice;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}
	
}
