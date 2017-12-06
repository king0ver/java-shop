package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.framework.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 订单vo
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月22日下午9:28:30
 */
@ApiIgnore
public class Order extends Cart implements Serializable {

	private static final long serialVersionUID = 8206833000476657708L;
	private String trade_sn;
	private String sn;

	private Consignee consignee;
	private int shippingid;
	private String payment_type;
	private Long ship_time;// 收货时间
	private Receipt receipt;
	private String receive_time;// 发货的时间类型
	@ApiModelProperty(value = "会员id")
	private int member_id;

	@ApiModelProperty(value = "会员姓名")
	private String member_name;

	@ApiModelProperty(value = "订单备注")
	private String remark;

	/**
	 * 创建时间
	 */
	private Long create_time;
	// 以下后添的
	/**
	 * 配送方式名
	 */
	@ApiModelProperty(value = "配送方式名称")
	private String shipping_type;
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
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	private String ship_name;
	/**
	 * 订单价格
	 */
	@ApiModelProperty(value = "订单价格")
	private Double order_price;
	/**
	 * 配送费
	 */
	@ApiModelProperty(value = "配送费")
	private Double shipping_price;
	/**
	 * 评论状态
	 */
	@ApiModelProperty(value = "评论状态")
	private String comment_status;
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
	
	@ApiModelProperty(value = "售后状态")
	private String service_status;
	
	@ApiModelProperty(value = "订单来源")
	private String client_type;
	
	/**
	 * 用一个购物车购造订单
	 * 
	 * @param cart
	 */
	public Order(Cart cart) {

		super(cart.getSeller_id(), cart.getSeller_name());

		// 初始化产品及优惠数据
		this.setWeight(cart.getWeight());
		this.setPrice(cart.getPrice());
		this.setProductList(cart.getProductList());
		this.setCouponList(cart.getCouponList());
		this.setGiftCouponList(cart.getGiftCouponList());
		this.setGiftList(cart.getGiftList());
		this.setGiftPoint(cart.getGiftPoint());
		
	}

	public Order() {
	}

	public Order(int ownerid, String ownername) {
		super(ownerid, ownername);
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@ApiIgnore
	public Consignee getConsignee() {
		return consignee;
	}

	public void setConsignee(Consignee consignee) {
		this.consignee = consignee;
	}

	public int getShippingid() {
		return shippingid;
	}

	public void setShippingid(int shippingid) {
		this.shippingid = shippingid;
	}

	public String getTrade_sn() {
		return trade_sn;
	}

	public void setTrade_sn(String trade_sn) {
		this.trade_sn = trade_sn;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public Long getShip_time() {
		return ship_time;
	}

	public void setShip_time(Long ship_time) {
		this.ship_time = ship_time;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}

	public String getShipping_type() {
		return shipping_type;
	}

	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}

	public String getPayment_method_name() {
		return payment_method_name;
	}

	public void setPayment_method_name(String payment_method_name) {
		this.payment_method_name = payment_method_name;
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

	public String getShip_name() {
		return ship_name;
	}

	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}

	public Double getOrder_price() {
		return order_price;
	}

	public void setOrder_price(Double order_price) {
		this.order_price = order_price;
	}

	public Double getShipping_price() {
		return shipping_price;
	}

	public void setShipping_price(Double shipping_price) {
		this.shipping_price = shipping_price;
	}

	public String getComment_status() {
		return comment_status;
	}

	public void setComment_status(String comment_status) {
		this.comment_status = comment_status;
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

	public Integer getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(Integer warehouse_id) {
		this.warehouse_id = warehouse_id;
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

	public String getService_status() {
		return service_status;
	}

	public void setService_status(String service_status) {
		this.service_status = service_status;
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