package com.enation.app.shop.shop.apply.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiParam;

/**
 * 
 * 店铺银行信息
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月15日 下午2:43:55
 */
public class ShopBankVo implements Serializable {

	private static final long serialVersionUID = 8215647483596272201L;
	/** 卖家id */
	private Integer seller_id;
	/** 店铺名字 */
	private String shop_name;
	/** 店铺佣金比例 */
	@ApiParam(value = "店铺佣金比例")
	private Double shop_commission;
	/** 银行开户名 */
	@ApiParam(value = "银行开户名")
	private String bank_account_name;
	/** 公司银行账号 */
	@ApiParam(value = "公司银行账号")
	private String bank_number;
	/** 开户银行支行名称 */
	@ApiParam(value = "开户银行支行名称")
	private String bank_name;
	/** 支行联行号 */
	@ApiParam(value = "支行联行号")
	private String bank_code;
	/** 开户银行所在省 */
	@ApiParam(value = "开户银行所在省")
	private String bank_province;
	/** 开户银行所在市 */
	@ApiParam(value = "开户银行所在市")
	private String bank_city;
	/** 开户银行所在区 */
	@ApiParam(value = "开户银行所在区")
	private String bank_region;
	/** 开户银行所在城镇 */
	@ApiParam(value = "开户银行所在城镇")
	private String bank_town;

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
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

	public Double getShop_commission() {
		return shop_commission;
	}

	public void setShop_commission(Double shop_commission) {
		this.shop_commission = shop_commission;
	}

	public String getBank_number() {
		return bank_number;
	}

	public void setBank_number(String bank_number) {
		this.bank_number = bank_number;
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

	public String getBank_province() {
		return bank_province;
	}

	public void setBank_province(String bank_province) {
		this.bank_province = bank_province;
	}

	public String getBank_city() {
		return bank_city;
	}

	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}

	public String getBank_region() {
		return bank_region;
	}

	public void setBank_region(String bank_region) {
		this.bank_region = bank_region;
	}

	public String getBank_town() {
		return bank_town;
	}

	public void setBank_town(String bank_town) {
		this.bank_town = bank_town;
	}

}
