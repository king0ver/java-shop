package com.enation.app.shop.orderbill.model.po;

import java.io.Serializable;

import com.enation.app.shop.orderbill.model.enums.BillStatusEnum;
import com.enation.framework.util.DateUtil;

import io.swagger.annotations.ApiParam;

/**
 * 
 * 结算单
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午2:31:06
 */
public class Bill implements Serializable {

	private static final Long serialVersionUID = 380632286977352459L;
	/**主键 */
	private Integer id;	
	/** 账单编号*/
	private String bill_sn;
	/** 开始时间*/
	private Long start_time;		
	/** 结束时间*/
	private Long end_time;		
	/**金额*/
	private Double price;			
	/** 佣金*/
	private Double commi_price;		
	/** 优惠金额*/
	private Double discount_price;	
	/** 状态 @see com.enation.javashop.entity.BillStatusEnum.java*/
	private Integer status;			
	/**账单类型  0月结 1日结 2季度结 3自定义 */
	private Integer bill_type;		
	/** 店铺id*/
	private Integer seller_id;	
	/** 付款时间*/
	private Long pay_time;      
	/** 出账日期*/
	private Long create_time;       
	/**结算金额	 */
	private Double bill_price;	     
	/** 退单金额	*/
	private Double returned_price;    
	/**退还佣金金额 */
	private Double returned_commi_price;  
	/** 账单号，平台使用*/
	private String sn;  
	/** 店铺名称*/
	@ApiParam(value = "店铺名称")
	private String shop_name;
	/**银行开户名 */
	@ApiParam(value = "银行开户名")
	private String bank_account_name;
	/** 公司银行账号*/
	@ApiParam(value = "公司银行账号")
	private String bank_account_number;
	/** 开户银行支行名称*/
	@ApiParam(value = "开户银行支行名称")
	private String bank_name;
	/** 支行联行号*/
	@ApiParam(value = "支行联行号")
	private String bank_code;
	/** 开户银行地址包括省市区等*/
	@ApiParam(value = "开户银行地址包括省市区等")
	private String bank_address;

	
	private Bill(){}
	
	/**
	 * 只需要传递 开始时间 账单类型 店铺
	 * 结束时间内部创建  设定为当前时间, 状态默认为未出帐
	 * @param startTime 开始时间
	 * @param billType 	账单类型
	 * @param sellerId	店铺id
	 */
	public Bill(Long startTime, Integer billType, Integer sellerId,Long endTime) {
		
		this.start_time = startTime;
		this.end_time = endTime;
		this.bill_type = billType;
		this.seller_id = sellerId;
		this.status = BillStatusEnum.NEW.getIndex();
		this.create_time =DateUtil.getDateline();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBill_sn() {
		return bill_sn;
	}
	public void setBill_sn(String bill_sn) {
		this.bill_sn = bill_sn;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getCommi_price() {
		return commi_price;
	}
	public void setCommi_price(Double commi_price) {
		this.commi_price = commi_price;
	}
	public Double getDiscount_price() {
		return discount_price;
	}
	public void setDiscount_price(Double discount_price) {
		this.discount_price = discount_price;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBill_type() {
		return bill_type;
	}
	public void setBill_type(Integer bill_type) {
		this.bill_type = bill_type;
	}
	public Integer getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public Long getStart_time() {
		return start_time;
	}

	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}

	public Long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}

	public Long getPay_time() {
		return pay_time;
	}

	public void setPay_time(Long pay_time) {
		this.pay_time = pay_time;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Double getBill_price() {
		return bill_price;
	}

	public void setBill_price(Double bill_price) {
		this.bill_price = bill_price;
	}

	public Double getReturned_price() {
		return returned_price;
	}

	public void setReturned_price(Double returned_price) {
		this.returned_price = returned_price;
	}

	public Double getReturned_commi_price() {
		return returned_commi_price;
	}

	public void setReturned_commi_price(Double returned_commi_price) {
		this.returned_commi_price = returned_commi_price;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getBank_account_name() {
		return bank_account_name;
	}

	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}

	public String getBank_account_number() {
		return bank_account_number;
	}

	public void setBank_account_number(String bank_account_number) {
		this.bank_account_number = bank_account_number;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_address() {
		return bank_address;
	}

	public void setBank_address(String bank_address) {
		this.bank_address = bank_address;
	}

}
