package com.enation.app.shop.orderbill.model.po;

import java.io.Serializable;

import com.enation.app.shop.orderbill.model.enums.BillStatusEnum;
import com.enation.framework.util.DateUtil;

/**
 * 
 * 结算单项
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月15日 下午2:35:32
 */
public class BillItem implements Serializable {

	private static final Long serialVersionUID = -4994060078954743095L;
	/** 主键 */
	private Integer id;
	/** 订单编号 */
	private String order_sn;
	/** 订单价格 */
	private Double order_price;
	/** 优惠价格 */
	private Double discount_price;
	/** 单项类型 0:收款，1=退款 */
	private Integer item_type;
	/** 加入时间 */
	private Long add_time;
	/** 所属账单id */
	private Integer bill_id;
	/** 状态 【一般情况下 和所属账单id状态】 */
	private Integer status;
	/** 店铺id */
	private Integer seller_id;
	/** 下单时间 */
	private Long order_time;
	/** 退款单号 */
	private String refund_sn;
	/** 会员id */
	private Integer member_id;
	/** 会员名字 */
	private String member_name;
	/** 收货人 */
	private String ship_name;
	/** 支付方式 */
	private String payment_type;
	/** 退货时间 */
	private Long refund_time;
	/** 退货id */
	private Integer refund_id;

	// 私有化默认构造函数
	private BillItem() {
	}

	public BillItem(Integer item_type) {
		this.add_time = DateUtil.getDateline();
		this.status = BillStatusEnum.NEW.getIndex();
		this.item_type = item_type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
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

	public Integer getItem_type() {
		return item_type;
	}

	public void setItem_type(Integer item_type) {
		this.item_type = item_type;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBill_id() {
		return bill_id;
	}

	public void setBill_id(Integer bill_id) {
		this.bill_id = bill_id;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}


	public String getRefund_sn() {
		return refund_sn;
	}

	public void setRefund_sn(String refund_sn) {
		this.refund_sn = refund_sn;
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

	public String getShip_name() {
		return ship_name;
	}

	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}


	public Integer getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(Integer refund_id) {
		this.refund_id = refund_id;
	}

	public Long getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Long add_time) {
		this.add_time = add_time;
	}

	public Long getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Long order_time) {
		this.order_time = order_time;
	}

	public Long getRefund_time() {
		return refund_time;
	}

	public void setRefund_time(Long refund_time) {
		this.refund_time = refund_time;
	}

}
