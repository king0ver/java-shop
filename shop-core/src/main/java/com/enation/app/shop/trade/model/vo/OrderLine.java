package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.trade.model.enums.CommentStatus;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.model.po.OrderItem;
import com.enation.framework.util.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单列表项
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月30日下午8:55:20
 */
@ApiModel(value="OrderListItem", description = "购物车")
public class OrderLine implements Serializable {
 
	private static final long serialVersionUID = 7294308941409413741L;

	public OrderLine(){}
	
	public OrderLine(Order vo){
		
		this.order_id = vo.getMember_id();
		this.sn = vo.getSn();
		this.seller_name= vo.getSeller_name();
		this.shipping_type = vo.getShipping_type();
		this.payment_name = vo.getPayment_method_name();
		
		this.order_status_text = OrderStatus.valueOf( vo.getOrder_status() ).description();
		this.pay_status_text = PayStatus.valueOf(vo.getPay_status()).description();
		this.ship_status_text = ShipStatus.valueOf(vo.getShip_status()).description();
		
		this.order_status=   vo.getOrder_status()  ;
		this.pay_status = vo.getPay_status() ;
		this.ship_status =  vo.getShip_status() ;
		this.comment_status = vo.getComment_status();
		this.service_tatus = vo.getService_status();
		
		this.ship_name=vo.getShip_name();
		this.payment_type = vo.getPayment_type();
		
		this.create_time = DateUtil.toString( vo.getCreate_time(),"yyyy-MM-dd HH:mm:ss" );
		if(vo.getShip_time()!=null){
			this.ship_time = DateUtil.toString( vo.getShip_time(),"yyyy-MM-dd HH:mm:ss" );
		}
		this.order_amount= vo.getOrder_price();
		this.shipping_amount=vo.getShipping_price();
		this.payment_type = vo.getPayment_type();
		
//		String item_json = po.getItems_json();
//		Gson gson = new Gson();
		this.productList = vo.getProductList();
		
		this.client_type = vo.getClient_type();
		
		//初始化订单允许状态
		this.operateAllowable = new OperateAllowable(PaymentType.valueOf(this.payment_type), OrderStatus.valueOf(this.order_status),
				CommentStatus.valueOf(this.comment_status) ,ShipStatus.valueOf(this.ship_status),ServiceStatus.valueOf(this.service_tatus),
				PayStatus.valueOf(this.pay_status));
	}


	

	@ApiModelProperty(value = "订单id" )
	private Integer order_id;
 
	@ApiModelProperty(value = "订单编号" )
	private String sn;
	
	@ApiModelProperty(value = "卖家名称" )
	private String seller_name;
	
	@ApiModelProperty(value = "配送方式" )
	private String shipping_type;
 
	@ApiModelProperty(value = "支付方式" )
	private String payment_name;	

	@ApiModelProperty(value = "订单状态文字" )
	private String order_status_text;
	
	@ApiModelProperty(value = "付款状态文字" )
	private String pay_status_text;
	
	@ApiModelProperty(value = "货运状态文字" )
	private String ship_status_text;
	
	
	@ApiModelProperty(value = "订单状态值" )
	private String order_status;
	
	@ApiModelProperty(value = "付款状态值" )
	private String pay_status;
	
	@ApiModelProperty(value = "货运状态值" )
	private String ship_status;
	
	@ApiModelProperty(value = "评论状态" )
	private String comment_status;
	
	@ApiModelProperty(value = "订单操作允许情况" )
	private OperateAllowable operateAllowable;
	
	@ApiModelProperty(value = "支付类型" )
	private String payment_type;
	@ApiModelProperty(value = "收货人姓名" )
	private String ship_name;
	
	@ApiModelProperty(value = "创建时间" )
	private String create_time;
	@ApiModelProperty(value = "实际发货时间" )
	private String ship_time;
	@ApiModelProperty(value = "订单总价" )
	private Double order_amount;
	
	@ApiModelProperty(value = "运费" )
	private Double shipping_amount;
	
	@ApiModelProperty(value = "售后状态" )
	private String service_tatus;

	@ApiModelProperty(value = "产品列表" )
	private List<Product> productList;
	
	@ApiModelProperty(value = "优惠卷列表" )
	private List<Coupon> couponList;
 
	@ApiModelProperty(value = "赠品列表" ) 
	private List<Gift> giftList;

	@ApiModelProperty(value = "订单项 非数据库字段" )
	private List<OrderItem> itemList;
	
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

	public String getService_tatus() {
		return service_tatus;
	}

	public void setService_tatus(String service_tatus) {
		this.service_tatus = service_tatus;
	}

	public List<OrderItem> getItemList() {return itemList;}

	public void setItemList(List<OrderItem> itemList) {this.itemList = itemList;}






}
