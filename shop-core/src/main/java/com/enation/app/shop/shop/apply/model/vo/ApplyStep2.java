package com.enation.app.shop.shop.apply.model.vo;
/**
 * 
 * (申请开店第二步) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月28日 下午2:33:54
 */
public class ApplyStep2 {
	/**法人姓名*/
	private String legal_name;
	
	/**法人身份证*/
	private String legal_id;
	
	/**法人身份证照片*/
	private String legal_img;
	
	/**营业执照号*/
	private String license_num;
	
	/**营业执照所在省id*/
	private Integer license_province_id;
	
	/**营业执照所在市id*/
	private Integer license_city_id;
	
	/**营业执照所在县id*/
	private Integer license_region_id;
	
	/**营业执照所在镇id*/
	private Integer license_town_id;
	
	/**营业执照所在省*/
	private String license_province;
	
	/**营业执照所在市*/
	private String license_city;
	
	/**营业执照所在县*/
	private String license_region;
	
	/**营业执照所在镇*/
	private String license_town;
	
	/**营业执照详细地址*/
	private String license_add;
	
	/**成立日期*/
	private Long  establish_date;	
	
	/**营业执照有效期开始*/
	private Long  licence_start;	
	
	/**营业执照有效期结束*/
	private Long  licence_end;	
	
	/**法定经营范围*/
	private String scope;
	
	/**营业执照电子版*/
	private String licence_img;
	
	/**组织机构代码*/
	private String organization_code;
	
	/**组织机构电子版*/
	private String code_img;
	
	/**一般纳税人证明电子版*/
	private String taxes_img;

	public String getLegal_name() {
		return legal_name;
	}

	public void setLegal_name(String legal_name) {
		this.legal_name = legal_name;
	}

	public String getLegal_id() {
		return legal_id;
	}

	public void setLegal_id(String legal_id) {
		this.legal_id = legal_id;
	}

	public String getLegal_img() {
		return legal_img;
	}

	public void setLegal_img(String legal_img) {
		this.legal_img = legal_img;
	}

	public String getLicense_num() {
		return license_num;
	}

	public void setLicense_num(String license_num) {
		this.license_num = license_num;
	}

	public Integer getLicense_province_id() {
		return license_province_id;
	}

	public void setLicense_province_id(Integer license_province_id) {
		this.license_province_id = license_province_id;
	}

	public Integer getLicense_city_id() {
		return license_city_id;
	}

	public void setLicense_city_id(Integer license_city_id) {
		this.license_city_id = license_city_id;
	}

	public Integer getLicense_region_id() {
		return license_region_id;
	}

	public void setLicense_region_id(Integer license_region_id) {
		this.license_region_id = license_region_id;
	}

	public Integer getLicense_town_id() {
		return license_town_id;
	}

	public void setLicense_town_id(Integer license_town_id) {
		this.license_town_id = license_town_id;
	}

	public String getLicense_province() {
		return license_province;
	}

	public void setLicense_province(String license_province) {
		this.license_province = license_province;
	}

	public String getLicense_city() {
		return license_city;
	}

	public void setLicense_city(String license_city) {
		this.license_city = license_city;
	}

	public String getLicense_region() {
		return license_region;
	}

	public void setLicense_region(String license_region) {
		this.license_region = license_region;
	}

	public String getLicense_town() {
		return license_town;
	}

	public void setLicense_town(String license_town) {
		this.license_town = license_town;
	}

	public String getLicense_add() {
		return license_add;
	}

	public void setLicense_add(String license_add) {
		this.license_add = license_add;
	}

	public Long getEstablish_date() {
		return establish_date;
	}

	public void setEstablish_date(Long establish_date) {
		this.establish_date = establish_date;
	}

	public Long getLicence_start() {
		return licence_start;
	}

	public void setLicence_start(Long licence_start) {
		this.licence_start = licence_start;
	}

	public Long getLicence_end() {
		return licence_end;
	}

	public void setLicence_end(Long licence_end) {
		this.licence_end = licence_end;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getLicence_img() {
		return licence_img;
	}

	public void setLicence_img(String licence_img) {
		this.licence_img = licence_img;
	}

	public String getOrganization_code() {
		return organization_code;
	}

	public void setOrganization_code(String organization_code) {
		this.organization_code = organization_code;
	}

	public String getCode_img() {
		return code_img;
	}

	public void setCode_img(String code_img) {
		this.code_img = code_img;
	}

	public String getTaxes_img() {
		return taxes_img;
	}

	public void setTaxes_img(String taxes_img) {
		this.taxes_img = taxes_img;
	}
	
	
}
