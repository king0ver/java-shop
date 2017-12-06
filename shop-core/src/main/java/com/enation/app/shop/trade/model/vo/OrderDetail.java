package com.enation.app.shop.trade.model.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单明细
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年6月5日下午8:44:52
 */
@ApiModel(description = "订单明细")
public class OrderDetail extends OrderPo {

	private static final long serialVersionUID = 5203744517347027553L;

	public OrderDetail() {
	}

	@ApiModelProperty(value = "订单操作允许情况")
	private OperateAllowable operateAllowable;

	@ApiModelProperty(value = "订单状态文字")
	private String order_status_text;

	@ApiModelProperty(value = "付款状态文字")
	private String pay_status_text;

	@ApiModelProperty(value = "发货状态文字")
	private String ship_status_text;

	@ApiModelProperty(value = "支付方式")
	private String payment_name;

	@ApiModelProperty(value = "产品列表")
	private List<Product> productList;
	@ApiModelProperty(value = "sku列表")
	private List<OrderSkuVo> orderSkuList;
	/** 评论商品列表 */
	private List<Product> commentsProductsList;

	public String getOrder_status_text() {

		if (this.getOrder_status().equals(OrderStatus.CONFIRM.value())
				&& this.getPayment_type().equals(PaymentType.online.value())) {
			this.order_status_text = "未付款";
		} else {

			this.order_status_text = OrderStatus.valueOf(this.getOrder_status()).description();
		}

		return order_status_text;
	}

	public void setOrder_status_text(String order_status_text) {
		this.order_status_text = order_status_text;
	}

	public String getPay_status_text() {
		this.pay_status_text = PayStatus.valueOf(this.getPay_status()).description();

		return pay_status_text;
	}

	public void setPay_status_text(String pay_status_text) {
		this.pay_status_text = pay_status_text;
	}

	public String getShip_status_text() {
		this.ship_status_text = ShipStatus.valueOf(this.getShip_status()).description();

		return ship_status_text;
	}

	public void setShip_status_text(String ship_status_text) {
		this.ship_status_text = ship_status_text;
	}

	public List<Product> getProductList() {
		Gson gson = new Gson();
		this.productList = gson.fromJson(this.getItems_json(), new TypeToken<List<Product>>() {
		}.getType());

		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public OperateAllowable getOperateAllowable() {

		return operateAllowable;
	}

	public void setOperateAllowable(OperateAllowable operateAllowable) {
		this.operateAllowable = operateAllowable;
	}

	/**
	 * 支付类型 显示为中文
	 */
	public String getPayment_type_text() {
		String paymenttype = super.getPayment_type();
		PaymentType paymentType = PaymentType.valueOf(paymenttype);
		if (paymentType == null) {
			return "未知";
		}
		return paymentType.description();
	}

	/**
	 * 下单时间字符串
	 * 
	 * @return
	 */
	public String getCreate_time_str() {

		return DateUtil.toString(this.getCreate_time(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 发货时间字符串
	 * 
	 * @return
	 */
	public String getShip_time_str() {
		if (this.getShip_time() != null) {
			return DateUtil.toString(this.getShip_time(), "yyyy-MM-dd HH:mm:ss");
		} else {
			return "";
		}
	}

	public String getPayment_name() {

		return PaymentType.valueOf(this.getPayment_type()).description();
	}

	public void setPayment_name(String payment_name) {
		this.payment_name = payment_name;
	}

	public List<Product> getCommentsProductsList() {
		return commentsProductsList;
	}

	public void setCommentsProductsList(List<Product> commentsProductsList) {
		this.commentsProductsList = commentsProductsList;
	}

	// 省市区
	public String getShipping_area() {
		String area = this.getShip_province() + this.getShip_city() + this.getShip_region();
		if (!StringUtil.isEmpty(this.getShip_town())) {
			area += this.getShip_town();
		}
		return area;
	}

	public List<OrderSkuVo> getOrderSkuList() {
		this.orderSkuList = new ArrayList<>();
		for (Product product : this.getProductList()) {
			OrderSkuVo OrderSkuVo = new OrderSkuVo(this.getOrder_status(), this.getPay_status(), this.getShip_status(),
					this.getPayment_type());
			BeanUtils.copyProperties(product, OrderSkuVo);
			this.orderSkuList.add(OrderSkuVo);
		}
		return orderSkuList;
	}

	public void setOrderSkuList(List<OrderSkuVo> orderSkuList) {
		this.orderSkuList = orderSkuList;
	}

}
