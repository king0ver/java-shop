package com.enation.app.shop.shop.apply.model.vo;
/**
 * 
 * (申请开店第三步) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月28日 下午2:33:54
 */
public class ApplyStep3 {
	/**银行开户名*/
	private String bank_account_name;
	
	/**银行开户账号*/
	private String bank_number;
	
	/**开户银行支行名称*/
	private String bank_name;
	
	/**开户银行所在省id*/
	private Integer bank_province_id;
	
	/**开户银行所在市id*/
	private Integer bank_city_id;
	
	/**开户银行所在县id*/
	private Integer bank_region_id;
	
	/**开户银行所在镇id*/
	private Integer bank_town_id;
	
	/**开户银行所在省*/
	private String bank_province;
	
	/**开户银行所在市*/
	private String bank_city;
	
	/**开户银行所在县*/
	private String bank_region;
	
	/**开户银行所在镇*/
	private String bank_town;
	
	/**开户银行许可证电子版*/
	private String bank_img;
	
	/**税务登记证号*/
	private String taxes_certificate_num;
	
	/**纳税人识别号*/
	private String taxes_distinguish_num;
	
	/**税务登记证书*/
	private String taxes_certificate_img;

	public String getBank_account_name() {
		return bank_account_name;
	}

	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
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

	public Integer getBank_province_id() {
		return bank_province_id;
	}

	public void setBank_province_id(Integer bank_province_id) {
		this.bank_province_id = bank_province_id;
	}

	public Integer getBank_city_id() {
		return bank_city_id;
	}

	public void setBank_city_id(Integer bank_city_id) {
		this.bank_city_id = bank_city_id;
	}

	public Integer getBank_region_id() {
		return bank_region_id;
	}

	public void setBank_region_id(Integer bank_region_id) {
		this.bank_region_id = bank_region_id;
	}

	public Integer getBank_town_id() {
		return bank_town_id;
	}

	public void setBank_town_id(Integer bank_town_id) {
		this.bank_town_id = bank_town_id;
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

	public String getBank_img() {
		return bank_img;
	}

	public void setBank_img(String bank_img) {
		this.bank_img = bank_img;
	}

	public String getTaxes_certificate_num() {
		return taxes_certificate_num;
	}

	public void setTaxes_certificate_num(String taxes_certificate_num) {
		this.taxes_certificate_num = taxes_certificate_num;
	}

	public String getTaxes_distinguish_num() {
		return taxes_distinguish_num;
	}

	public void setTaxes_distinguish_num(String taxes_distinguish_num) {
		this.taxes_distinguish_num = taxes_distinguish_num;
	}

	public String getTaxes_certificate_img() {
		return taxes_certificate_img;
	}

	public void setTaxes_certificate_img(String taxes_certificate_img) {
		this.taxes_certificate_img = taxes_certificate_img;
	}
	
}
