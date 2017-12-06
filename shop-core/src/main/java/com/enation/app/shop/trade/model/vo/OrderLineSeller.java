package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.trade.model.enums.CommentStatus;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.framework.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单列表项
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月30日下午8:55:20
 */
@ApiModel(value = "OrderListItem", description = "购物车")
public class OrderLineSeller implements Serializable {

	private static final long serialVersionUID = 7294308941409413741L;

	public OrderLineSeller() {
	}

	public OrderLineSeller(OrderPo po) {

		this.order_id = po.getOrder_id();
		this.sn = po.getSn();
		this.seller_name = po.getSeller_name();
		this.shipping_type = po.getShipping_type();
		this.payment_name = po.getPayment_method_name();

		// 订单状态 款到发货 已确认变为未付款
		if (po.getOrder_status().equals(OrderStatus.CONFIRM.value())
				&& po.getPayment_type().equals(PaymentType.online.value())) {
			this.order_status_text = "未付款";
		} else {

			this.order_status_text = OrderStatus.valueOf(po.getOrder_status()).description();
		}
		this.pay_status_text = PayStatus.valueOf(po.getPay_status()).description();
		this.ship_status_text = ShipStatus.valueOf(po.getShip_status()).description();
		this.order_status = po.getOrder_status();
		this.pay_status = po.getPay_status();
		this.ship_status = po.getShip_status();
		this.comment_status = po.getComment_status();
		this.service_status = po.getService_status();
		this.payment_type = po.getPayment_type();
		this.create_time = DateUtil.toString(po.getCreate_time(), "yyyy-MM-dd HH:mm:ss");
		if (po.getShip_time() != null) {
			this.ship_time = DateUtil.toString(po.getShip_time(), "yyyy-MM-dd HH:mm:ss");
		}
		this.order_amount = po.getOrder_price();
		this.shipping_amount = po.getShipping_price();
		this.payment_type = po.getPayment_type();

		this.member_id = po.getMember_id();
		this.member_name = po.getMember_name();
		this.ship_name = po.getShip_name();
		
		this.seller_id = po.getSeller_id();

		String item_json = po.getItems_json();
		Gson gson = new Gson();
		this.productList = gson.fromJson(item_json, new TypeToken<List<Product>>() {
		}.getType());

		this.orderSkuList = new ArrayList<>();
		for (Product p : productList) {
			OrderSkuVo OrderSkuVo = new OrderSkuVo(this.order_status, this.pay_status, this.ship_status,
					this.payment_type);
			BeanUtils.copyProperties(p, OrderSkuVo);
			this.orderSkuList.add(OrderSkuVo);
		}
		this.client_type = ClientType.valueOf(po.getClient_type()).getClient();

		// 初始化订单允许状态
		this.operateAllowable = new OperateAllowable(PaymentType.valueOf(this.payment_type),
				OrderStatus.valueOf(this.order_status), CommentStatus.valueOf(this.comment_status),
				ShipStatus.valueOf(this.ship_status), ServiceStatus.valueOf(this.service_status),
				PayStatus.valueOf(this.pay_status));
	}

	@ApiModelProperty(value = "订单id")
	private Integer order_id;

	@ApiModelProperty(value = "订单编号")
	private String sn;

	@ApiModelProperty(value = "卖家名称")
	private String seller_name;
	
	@ApiModelProperty(value = "卖家id")
	private Integer seller_id;

	@ApiModelProperty(value = "配送方式")
	private String shipping_type;

	@ApiModelProperty(value = "支付方式")
	private String payment_name;

	@ApiModelProperty(value = "订单状态文字")
	private String order_status_text;

	@ApiModelProperty(value = "付款状态文字")
	private String pay_status_text;
	@ApiModelProperty(value = "收货人姓名")
	private String ship_name;
	@ApiModelProperty(value = "货运状态文字")
	private String ship_status_text;

	@ApiModelProperty(value = "订单状态值")
	private String order_status;

	@ApiModelProperty(value = "付款状态值")
	private String pay_status;

	@ApiModelProperty(value = "货运状态值")
	private String ship_status;

	@ApiModelProperty(value = "评论状态")
	private String comment_status;

	@ApiModelProperty(value = "订单操作允许情况")
	private OperateAllowable operateAllowable;

	@ApiModelProperty(value = "支付类型")
	private String payment_type;
	@ApiModelProperty(value = "实际发货时间")
	private String ship_time;

	@ApiModelProperty(value = "创建时间")
	private String create_time;

	@ApiModelProperty(value = "订单总价")
	private Double order_amount;

	@ApiModelProperty(value = "运费")
	private Double shipping_amount;

	@ApiModelProperty(value = "会员id")
	private Integer member_id;

	@ApiModelProperty(value = "售后状态")
	private String service_status;

	@ApiModelProperty(value = "会员姓名")
	private String member_name;

	@ApiModelProperty(value = "产品列表")
	private List<Product> productList;
	@ApiModelProperty(value = "sku列表")
	private List<OrderSkuVo> orderSkuList;
	@ApiModelProperty(value = "优惠卷列表")
	private List<Coupon> couponList;

	@ApiModelProperty(value = "赠品列表")
	private List<Gift> giftList;

	@ApiModelProperty(value = "订单来源")
	private String client_type;

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getShipping_type() {
		return shipping_type;
	}

	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}

	public String getPayment_name() {
		return payment_name;
	}

	public void setPayment_name(String payment_name) {
		this.payment_name = payment_name;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getShip_status() {
		return ship_status;
	}

	public void setShip_status(String ship_status) {
		this.ship_status = ship_status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public Double getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(Double order_amount) {
		this.order_amount = order_amount;
	}

	public Double getShipping_amount() {
		return shipping_amount;
	}

	public void setShipping_amount(Double shipping_amount) {
		this.shipping_amount = shipping_amount;
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

	public List<Gift> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<Gift> giftList) {
		this.giftList = giftList;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getOrder_status_text() {
		return order_status_text;
	}

	public void setOrder_status_text(String order_status_text) {
		this.order_status_text = order_status_text;
	}

	public String getPay_status_text() {
		return pay_status_text;
	}

	public void setPay_status_text(String pay_status_text) {
		this.pay_status_text = pay_status_text;
	}

	public String getShip_status_text() {
		return ship_status_text;
	}

	public void setShip_status_text(String ship_status_text) {
		this.ship_status_text = ship_status_text;
	}

	public String getComment_status() {
		return comment_status;
	}

	public void setComment_status(String comment_status) {
		this.comment_status = comment_status;
	}

	public OperateAllowable getOperateAllowable() {
		return operateAllowable;
	}

	public void setOperateAllowable(OperateAllowable operateAllowable) {
		this.operateAllowable = operateAllowable;
	}

	public String getShip_time() {
		return ship_time;
	}

	public void setShip_time(String ship_time) {
		this.ship_time = ship_time;
	}

	public String getShip_name() {
		return ship_name;
	}

	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getService_status() {
		return service_status;
	}

	public void setService_status(String service_status) {
		this.service_status = service_status;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}
	public List<OrderSkuVo> getOrderSkuList() {
		return orderSkuList;
	}

	public void setOrderSkuList(List<OrderSkuVo> orderSkuList) {
		this.orderSkuList = orderSkuList;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}
	
	
}
