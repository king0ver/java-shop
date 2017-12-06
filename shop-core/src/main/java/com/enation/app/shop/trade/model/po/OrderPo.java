package com.enation.app.shop.trade.model.po;

import java.io.Serializable;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.trade.model.enums.CommentStatus;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.model.vo.Consignee;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.model.vo.Receipt;
import com.enation.framework.database.PrimaryKeyField;
import com.enation.framework.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单PO模型
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月18日上午9:43:21
 */
public class OrderPo implements Serializable {

	private static final long serialVersionUID = -7411588688953580264L;

	public OrderPo() {

	}

	public OrderPo(Order orderVo) {
		this.receive_time = orderVo.getReceive_time();
		this.sn = orderVo.getSn();

		this.ship_time = orderVo.getShip_time();

		this.payment_type = orderVo.getPayment_type();

		// 收货人
		Consignee consignee = orderVo.getConsignee();
		ship_name = consignee.getName();
		ship_addr = consignee.getAddress();

		this.address_id = consignee.getConsignee_id();
		this.ship_mobile = consignee.getMobile();
		this.ship_tel = consignee.getTelephone();

		this.ship_province = consignee.getProvince();
		this.ship_city = consignee.getCity();
		this.ship_region = consignee.getDistrict();
		this.ship_town = consignee.getTown();

		this.ship_provinceid = consignee.getProvince_id();
		this.ship_cityid = consignee.getCity_id();
		this.ship_regionid = consignee.getDistrict_id();
		this.ship_townid = consignee.getTown_id();

		// 价格
		PriceDetail priceDetail = orderVo.getPrice();
		this.order_price = priceDetail.getTotal_price();
		this.goods_price = priceDetail.getGoods_price();
		this.shipping_price = priceDetail.getFreight_price();
		this.discount_price = priceDetail.getDiscount_price();

		// TODO 应付金额要仔细设计
		this.need_pay_money = priceDetail.getTotal_price();

		this.weight = orderVo.getWeight();
		this.shipping_id = orderVo.getShippingid();

		// 卖家
		this.seller_id = orderVo.getSeller_id();
		this.seller_name = orderVo.getSeller_name();

		// 买家
		this.member_id = orderVo.getMember_id();
		this.member_name = orderVo.getMember_name();

		// 创建时间
		this.create_time = orderVo.getCreate_time();

		// 初始化状态
		this.ship_status = ShipStatus.SHIP_NO.value();
		this.order_status = OrderStatus.NEW.value();
		this.pay_status = PayStatus.PAY_NO.value();
		this.comment_status = CommentStatus.UNFINISHED.value();
		this.service_status = ServiceStatus.NOT_APPLY.value();

		this.disabled = 0;

		// 产品列表
		Gson gson = new Gson();
		this.items_json = gson.toJson(orderVo.getProductList());

		 //发票信息
		Receipt receipt = orderVo.getReceipt();
		
		if(receipt!=null){
			this.need_receipt = receipt.getNeed_receipt();
			this.receipt_title = receipt.getTitle();
			this.receipt_content = receipt.getContent();
			this.duty_invoice = receipt.getDuty_invoice();
			this.receipt_type = receipt.getType();
		}else{
			//System.out.println("发票信息是空");
			this.need_receipt = "";
			this.receipt_title = "";
			this.receipt_content = "";
			this.duty_invoice = "";
			this.receipt_type = "";
		}
		

		// 备注
		this.remark = orderVo.getRemark();
		
		//订单来源
		this.client_type = orderVo.getClient_type();
	}

	/**
	 * 订单Id
	 */
	@ApiModelProperty(value = "订单Id")
	private Integer order_id;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String sn;

	/**
	 * 属主id
	 */
	@ApiModelProperty(value = "卖家id")
	private Integer seller_id;

	/**
	 * 属主名称
	 */
	@ApiModelProperty(value = "卖家姓名")
	private String seller_name;

	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员id")
	private Integer member_id;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员姓名")
	private String member_name;

	@ApiModelProperty(value = "交易编号")
	private String trade_sn;

	/**
	 * 订单状
	 */
	@ApiModelProperty(value = "订单状态")
	private String order_status;

	/**
	 * 付款状
	 */
	@ApiModelProperty(value = "付款状态")
	private String pay_status;

	/**
	 * 配送状态
	 */
	@ApiModelProperty(value = "配送状态")
	private String ship_status;

	/**
	 * 评论状态
	 */
	@ApiModelProperty(value = "评论状态")
	private String comment_status;

	/**
	 * 配送方式id
	 */
	@ApiModelProperty(value = "配送方式id")
	private Integer shipping_id;

	/**
	 * 配送方式名
	 */
	@ApiModelProperty(value = "配送方式名称")
	private String shipping_type;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Long create_time;

	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String ship_name;

	/**
	 * 收货地址
	 */
	@ApiModelProperty(value = "会员id")
	private String ship_addr;

	/**
	 * 邮政编码
	 */
	@ApiModelProperty(value = "邮政编码")
	private String ship_zip;

	/**
	 * 收货人手机号
	 */
	@ApiModelProperty(value = "收货人手机号")
	private String ship_mobile;

	/**
	 * 收货人电
	 */
	@ApiModelProperty(value = "会员id")
	private String ship_tel;

	/**
	 * 发货时间
	 */
	@ApiModelProperty(value = "发货时间")
	private Long ship_time;
	/**
	 * 送货时间
	 */
	@ApiModelProperty(value = "送货时间")
	private String receive_time;

	/**
	 * 商品价格
	 */
	@ApiModelProperty(value = "商品价格")
	private Double goods_price;

	/**
	 * 配送费
	 */
	@ApiModelProperty(value = "配送费")
	private Double shipping_price;

	/**
	 * 订单价格
	 */
	@ApiModelProperty(value = "订单价格")
	private Double order_price;

	@ApiModelProperty(value = "优惠的金额")
	private Double discount_price;
	

	/**
	 * 订单重量
	 */
	@ApiModelProperty(value = "订单重量")
	private Double weight;

	/**
	 * 付款金额
	 */
	@ApiModelProperty(value = "付款金额")
	private Double paymoney;

	/**
	 * 订单备注
	 */
	@ApiModelProperty(value = " 订单备注")
	private String remark;

	/**
	 * 是否已经删除
	 */
	@ApiModelProperty(value = "是否已经删除")
	private Integer disabled;

	@ApiModelProperty(value = "支付方式id")
	private Integer payment_method_id;

	@ApiModelProperty(value = "支付插件id")
	private String payment_plugin_id;

	@ApiModelProperty(value = "支付方式名称")
	private String payment_method_name;
	
	@ApiModelProperty(value = "支付时间")
	private Long payment_time;

	
	
	/**
	 * 支付类型
	 */
	@ApiModelProperty(value = "支付类型")
	private String payment_type;

	/**
	 * 付款账号
	 */
	@ApiModelProperty(value = "付款账号")
	private String payment_account;

	/**
	 * 商品数量
	 */
	@ApiModelProperty(value = "商品数量")
	private Integer goods_num;

	/**
	 * 发货仓库id
	 */
	@ApiModelProperty(value = "发货仓库id")
	private Integer warehouse_id;

	/**
	 * 取消订单的原
	 */
	@ApiModelProperty(value = "取消原因")
	private String cancel_reason;

	/**
	 * 收货地址省Id
	 */
	@ApiModelProperty(value = "收货地址省Id")
	private Integer ship_provinceid;

	/**
	 * 收货地址市Id
	 */
	@ApiModelProperty(value = "收货地址市Id")
	private Integer ship_cityid;

	/**
	 * 收货地址区Id
	 */
	@ApiModelProperty(value = "收货地址区Id")
	private Integer ship_regionid;

	/**
	 * 收货地址街道Id
	 */
	@ApiModelProperty(value = "收货地址街道Id")
	private Integer ship_townid;

	/**
	 * 收货省
	 */
	@ApiModelProperty(value = "收货省")
	private String ship_province;

	/**
	 * 收货地址市
	 */
	@ApiModelProperty(value = "收货地址市")
	private String ship_city;

	/**
	 * 收货地址区
	 */
	@ApiModelProperty(value = "收货地址区")
	private String ship_region;

	/**
	 * 收货地址街道
	 */
	@ApiModelProperty(value = "收货地址街道")
	private String ship_town;

	/**
	 * 签收时间
	 */
	@ApiModelProperty(value = "签收时间")
	private Long signing_time;

	/**
	 * 签收人姓名
	 */
	@ApiModelProperty(value = "签收人姓名")
	private String the_sign;

	@ApiModelProperty(value = "管理员备注")
	private String admin_remark;

	/**
	 * 地址id
	 */
	@ApiModelProperty(value = "收货地址id")
	private Integer address_id;

	/**
	 * 2013808新增订单还需要支付多少钱的字kingapex
	 */
	@ApiModelProperty(value = "应付金额")
	private Double need_pay_money;

	@ApiModelProperty(value = "发货单号")
	private String ship_no;

	/**
	 * 发货 2015-05-21新增货物列表json字段-by kingapex
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private String items_json;

	/**
	 * 物流公司Id
	 */
	@ApiModelProperty(value = "物流公司Id")
	private Integer logi_id;

	/**
	 * 物流公司名称
	 */
	@ApiModelProperty(value = "物流公司名称")
	private String logi_name;

	@ApiModelProperty(value = "是否需要发票")
	private String need_receipt;

	@ApiModelProperty(value = "抬头")
	private String receipt_title;

	@ApiModelProperty(value = "内容")
	private String receipt_content;
	
	@ApiModelProperty(value = "税号")
	private String duty_invoice;
	
	@ApiModelProperty(value = "发票类型")
	private String receipt_type;
	
	@ApiModelProperty(value = "售后状态")
	private String service_status;
	
	@ApiModelProperty(value = "支付订单后，支付方式返回的交易号")
	private String pay_order_no;
	
	@ApiModelProperty(value = "订单来源")
	private String client_type;

	@PrimaryKeyField
	public Integer getOrder_id() {
		return order_id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
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

	public String getTrade_sn() {
		return trade_sn;
	}

	public void setTrade_sn(String trade_sn) {
		this.trade_sn = trade_sn;
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

	public Integer getShipping_id() {
		return shipping_id;
	}

	public void setShipping_id(Integer shipping_id) {
		this.shipping_id = shipping_id;
	}

	public String getShipping_type() {
		return shipping_type;
	}

	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public String getShip_name() {
		return ship_name;
	}

	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}

	public String getShip_addr() {
		return ship_addr;
	}

	public void setShip_addr(String ship_addr) {
		this.ship_addr = ship_addr;
	}

	public String getShip_zip() {
		return ship_zip;
	}

	public void setShip_zip(String ship_zip) {
		this.ship_zip = ship_zip;
	}

	public String getShip_mobile() {
		return ship_mobile;
	}

	public void setShip_mobile(String ship_mobile) {
		this.ship_mobile = ship_mobile;
	}

	public String getShip_tel() {
		return ship_tel;
	}

	public void setShip_tel(String ship_tel) {
		this.ship_tel = ship_tel;
	}

	public Long getShip_time() {
		return ship_time;
	}

	public void setShip_time(Long ship_time) {
		this.ship_time = ship_time;
	}

	public Double getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(Double goods_price) {
		this.goods_price = goods_price;
	}

	public Double getShipping_price() {
		return shipping_price;
	}

	public void setShipping_price(Double shipping_price) {
		this.shipping_price = shipping_price;
	}

	public Double getOrder_price() {
		return order_price;
	}

	public void setOrder_price(Double order_price) {
		this.order_price = order_price;
	}

	public Double getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(Double discount_price) {
		this.discount_price = discount_price;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getPaymoney() {
		return paymoney;
	}

	public void setPaymoney(Double paymoney) {
		this.paymoney = paymoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Integer getPayment_method_id() {
		return payment_method_id;
	}

	public void setPayment_method_id(Integer payment_method_id) {
		this.payment_method_id = payment_method_id;
	}

	public String getPayment_plugin_id() {
		return payment_plugin_id;
	}

	public void setPayment_plugin_id(String payment_plugin_id) {
		this.payment_plugin_id = payment_plugin_id;
	}

	public String getPayment_method_name() {
		return payment_method_name;
	}

	public void setPayment_method_name(String payment_method_name) {
		this.payment_method_name = payment_method_name;
	}

	public String getPayment_account() {
		return payment_account;
	}

	public void setPayment_account(String payment_account) {
		this.payment_account = payment_account;
	}

	public Integer getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}

	public String getCancel_reason() {
		return cancel_reason;
	}

	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}

	public Integer getShip_provinceid() {
		return ship_provinceid;
	}

	public void setShip_provinceid(Integer ship_provinceid) {
		this.ship_provinceid = ship_provinceid;
	}

	public Integer getShip_cityid() {
		return ship_cityid;
	}

	public void setShip_cityid(Integer ship_cityid) {
		this.ship_cityid = ship_cityid;
	}

	public Integer getShip_regionid() {
		return ship_regionid;
	}

	public void setShip_regionid(Integer ship_regionid) {
		this.ship_regionid = ship_regionid;
	}

	public Integer getShip_townid() {
		return ship_townid;
	}

	public void setShip_townid(Integer ship_townid) {
		this.ship_townid = ship_townid;
	}

	public Long getSigning_time() {
		return signing_time;
	}

	public void setSigning_time(Long signing_time) {
		this.signing_time = signing_time;
	}

	public String getThe_sign() {
		return the_sign;
	}

	public void setThe_sign(String the_sign) {
		this.the_sign = the_sign;
	}

	public String getAdmin_remark() {
		return admin_remark;
	}

	public void setAdmin_remark(String admin_remark) {
		this.admin_remark = admin_remark;
	}

	public Integer getAddress_id() {
		return address_id;
	}

	public void setAddress_id(Integer address_id) {
		this.address_id = address_id;
	}

	public Double getNeed_pay_money() {
		return need_pay_money;
	}

	public void setNeed_pay_money(Double need_pay_money) {
		this.need_pay_money = need_pay_money;
	}

	public String getShip_no() {
		return ship_no;
	}

	public void setShip_no(String ship_no) {
		this.ship_no = ship_no;
	}

	@JsonIgnore
	public String getItems_json() {
		return items_json;
	}

	public void setItems_json(String items_json) {
		this.items_json = items_json;
	}

	public Integer getLogi_id() {
		return logi_id;
	}

	public void setLogi_id(Integer logi_id) {
		this.logi_id = logi_id;
	}

	public String getLogi_name() {
		return logi_name;
	}

	public void setLogi_name(String logi_name) {
		this.logi_name = logi_name;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getNeed_receipt() {
		return need_receipt;
	}

	public void setNeed_receipt(String need_receipt) {
		this.need_receipt = need_receipt;
	}

	public String getReceipt_title() {
		return receipt_title;
	}

	public void setReceipt_title(String receipt_title) {
		this.receipt_title = receipt_title;
	}

	public String getReceipt_content() {
		return receipt_content;
	}

	public void setReceipt_content(String receipt_content) {
		this.receipt_content = receipt_content;
	}

	public Integer getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(Integer warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public String getShip_province() {
		return ship_province;
	}

	public void setShip_province(String ship_province) {
		this.ship_province = ship_province;
	}

	public String getShip_city() {
		return ship_city;
	}

	public void setShip_city(String ship_city) {
		this.ship_city = ship_city;
	}

	public String getShip_region() {
		return ship_region;
	}

	public void setShip_region(String ship_region) {
		this.ship_region = ship_region;
	}

	public String getShip_town() {
		return ship_town;
	}

	public void setShip_town(String ship_town) {
		this.ship_town = ship_town;
	}

	public String getComment_status() {
		return comment_status;
	}

	public void setComment_status(String comment_status) {
		this.comment_status = comment_status;
	}

	public String getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}

	public String getPay_order_no() {
		return pay_order_no;
	}

	public void setPay_order_no(String pay_order_no) {
		this.pay_order_no = pay_order_no;
	}

	public String getService_status() {
		return service_status;
	}

	public void setService_status(String service_status) {
		this.service_status = service_status;
	}

	public String getDuty_invoice() {
		return duty_invoice;
	}

	public void setDuty_invoice(String duty_invoice) {
		this.duty_invoice = duty_invoice;
	}

	public Long getPayment_time() {
		return payment_time;
	}

	public void setPayment_time(Long payment_time) {
		this.payment_time = payment_time;
	}

	public String getReceipt_type() {
		return receipt_type;
	}

	public void setReceipt_type(String receipt_type) {
		this.receipt_type = receipt_type;
	}

	public String getClient_type() {
		if(StringUtil.isEmpty(client_type)){
			client_type = ClientType.PC.name();
		}
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	
	
}