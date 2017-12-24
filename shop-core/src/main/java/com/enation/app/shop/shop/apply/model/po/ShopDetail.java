package com.enation.app.shop.shop.apply.model.po;

/**
 * 
 * (店铺详情) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月14日 下午3:11:26
 */
public class ShopDetail{
	  
	/**店铺详细id*/
	private Integer id;
	
	/**店铺id*/
	private Integer shop_id;	
	
	/**店铺所在省id*/
	private Integer shop_province_id;
	
	/**店铺所在市id*/
	private Integer shop_city_id;
	
	/**店铺所在县id*/
	private Integer shop_region_id;
	
	/**店铺所在镇id*/
	private Integer shop_town_id;
	
	/**店铺所在省*/
	private String shop_province;
	
	/**店铺所在市*/
	private String shop_city;
	
	/**店铺所在县*/
	private String shop_region;
	
	/**店铺所在镇*/
	private String shop_town;
	
	/**店铺详细地址*/
	private String shop_add;
	 
	/**公司名称*/
	private String company_name;	
	
	/**公司地址*/
	private String compant_address;
	
	/**公司电话*/
	private String compant_phone;
	
	/**电子邮箱*/
	private String compant_email;
	
	/**员工总数*/
	private Integer employee_num;
	
	/**注册资金*/
	private Integer reg_monety;
	
	/**联系人姓名*/
	private String link_name;
	
	/**联系人电话*/
	private String link_phone;
	
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
	
	/**店铺经营类目*/
	private String goods_management_category;
	
	/**店铺等级*/
	private Integer shop_level;
	
	/**店铺等级申请*/
	private Integer shop_level_apply;
	
	/**店铺相册已用存储量*/
	private Double store_space_capacity;		
	
	/**店铺logo*/
	private String shop_logo;
	
	/**店铺横幅*/
	private String shop_banner;
	
	/**店铺简介*/
	private String shop_desc;
	
	/**是否推荐*/
	private Integer shop_recommend;
	
	/**店铺主题id*/
	private Integer shop_themeid;
	
	/**店铺主题*/
	private String shop_theme_path;
	
	/**店铺主题id*/
	private Integer wap_themeid;
	
	/**wap店铺主题*/
	private String wap_theme_path;
	
	/**店铺信用*/
	private Integer shop_credit;
	
	/**店铺好评率*/
	private Double shop_praise_rate;
	
	/**店铺描述相符度*/
	private Double shop_desccredit;
	
	/**服务态度分数*/
	private Double shop_servicecredit;
	
	/**发货速度分数*/
	private Double shop_deliverycredit;
	
	/**店铺收藏数*/
	private Integer shop_collect;
	
	/**店铺认证*/
	private Integer shop_auth;
	
	/**店主认证*/
	private Integer name_auth;
	
	/**店铺商品数*/
	private Integer goods_num;
	
	/**店铺客服qq*/
	private String shop_qq;
	
	/**店铺佣金比例*/
	private Double shop_commission;
	
	/**货品预警数*/
	private Integer goods_warning_count;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompant_address() {
		return compant_address;
	}
	public void setCompant_address(String compant_address) {
		this.compant_address = compant_address;
	}
	public String getCompant_phone() {
		return compant_phone;
	}
	public void setCompant_phone(String compant_phone) {
		this.compant_phone = compant_phone;
	}
	public String getCompant_email() {
		return compant_email;
	}
	public void setCompant_email(String compant_email) {
		this.compant_email = compant_email;
	}
	public Integer getEmployee_num() {
		return employee_num;
	}
	public void setEmployee_num(Integer employee_num) {
		this.employee_num = employee_num;
	}
	public Integer getReg_monety() {
		return reg_monety;
	}
	public void setReg_monety(Integer reg_monety) {
		this.reg_monety = reg_monety;
	}
	public String getLink_name() {
		return link_name;
	}
	public void setLink_name(String link_name) {
		this.link_name = link_name;
	}
	public String getLink_phone() {
		return link_phone;
	}
	public void setLink_phone(String link_phone) {
		this.link_phone = link_phone;
	}
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
	public String getGoods_management_category() {
		return goods_management_category;
	}
	public void setGoods_management_category(String goods_management_category) {
		this.goods_management_category = goods_management_category;
	}
	public Integer getShop_level() {
		return shop_level;
	}
	public void setShop_level(Integer shop_level) {
		this.shop_level = shop_level;
	}
	public String getShop_logo() {
		return shop_logo;
	}
	public void setShop_logo(String shop_logo) {
		this.shop_logo = shop_logo;
	}
	public String getShop_banner() {
		return shop_banner;
	}
	public void setShop_banner(String shop_banner) {
		this.shop_banner = shop_banner;
	}
	public String getShop_desc() {
		return shop_desc;
	}
	public void setShop_desc(String shop_desc) {
		this.shop_desc = shop_desc;
	}
	public Integer getShop_recommend() {
		return shop_recommend;
	}
	public void setShop_recommend(Integer shop_recommend) {
		this.shop_recommend = shop_recommend;
	}
	public Integer getShop_themeid() {
		return shop_themeid;
	}
	public void setShop_themeid(Integer shop_themeid) {
		this.shop_themeid = shop_themeid;
	}
	public String getShop_theme_path() {
		return shop_theme_path;
	}
	public void setShop_theme_path(String shop_theme_path) {
		this.shop_theme_path = shop_theme_path;
	}
	public Integer getShop_credit() {
		return shop_credit;
	}
	public void setShop_credit(Integer shop_credit) {
		this.shop_credit = shop_credit;
	}
	public Double getShop_praise_rate() {
		return shop_praise_rate;
	}
	public void setShop_praise_rate(Double shop_praise_rate) {
		this.shop_praise_rate = shop_praise_rate;
	}
	public Double getShop_desccredit() {
		return shop_desccredit;
	}
	public void setShop_desccredit(Double shop_desccredit) {
		this.shop_desccredit = shop_desccredit;
	}
	public Double getShop_servicecredit() {
		return shop_servicecredit;
	}
	public void setShop_servicecredit(Double shop_servicecredit) {
		this.shop_servicecredit = shop_servicecredit;
	}
	public Double getShop_deliverycredit() {
		return shop_deliverycredit;
	}
	public void setShop_deliverycredit(Double shop_deliverycredit) {
		this.shop_deliverycredit = shop_deliverycredit;
	}
	public Integer getShop_collect() {
		return shop_collect;
	}
	public void setShop_collect(Integer shop_collect) {
		this.shop_collect = shop_collect;
	}
	public Integer getShop_auth() {
		return shop_auth;
	}
	public void setShop_auth(Integer shop_auth) {
		this.shop_auth = shop_auth;
	}
	public Integer getName_auth() {
		return name_auth;
	}
	public void setName_auth(Integer name_auth) {
		this.name_auth = name_auth;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public String getShop_qq() {
		return shop_qq;
	}
	public void setShop_qq(String shop_qq) {
		this.shop_qq = shop_qq;
	}
	public Double getShop_commission() {
		return shop_commission;
	}
	public void setShop_commission(Double shop_commission) {
		this.shop_commission = shop_commission;
	}
	public Integer getGoods_warning_count() {
		return goods_warning_count;
	}
	public void setGoods_warning_count(Integer goods_warning_count) {
		this.goods_warning_count = goods_warning_count;
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
	public Integer getShop_level_apply() {
		return shop_level_apply;
	}
	public void setShop_level_apply(Integer shop_level_apply) {
		this.shop_level_apply = shop_level_apply;
	}
	public Double getStore_space_capacity() {
		return store_space_capacity;
	}
	public void setStore_space_capacity(Double store_space_capacity) {
		this.store_space_capacity = store_space_capacity;
	}
	public Integer getShop_province_id() {
		return shop_province_id;
	}
	public void setShop_province_id(Integer shop_province_id) {
		this.shop_province_id = shop_province_id;
	}
	public Integer getShop_city_id() {
		return shop_city_id;
	}
	public void setShop_city_id(Integer shop_city_id) {
		this.shop_city_id = shop_city_id;
	}
	public Integer getShop_region_id() {
		return shop_region_id;
	}
	public void setShop_region_id(Integer shop_region_id) {
		this.shop_region_id = shop_region_id;
	}
	public Integer getShop_town_id() {
		return shop_town_id;
	}
	public void setShop_town_id(Integer shop_town_id) {
		this.shop_town_id = shop_town_id;
	}
	public String getShop_province() {
		return shop_province;
	}
	public void setShop_province(String shop_province) {
		this.shop_province = shop_province;
	}
	public String getShop_city() {
		return shop_city;
	}
	public void setShop_city(String shop_city) {
		this.shop_city = shop_city;
	}
	public String getShop_region() {
		return shop_region;
	}
	public void setShop_region(String shop_region) {
		this.shop_region = shop_region;
	}
	public String getShop_town() {
		return shop_town;
	}
	public void setShop_town(String shop_town) {
		this.shop_town = shop_town;
	}
	public String getShop_add() {
		return shop_add;
	}
	public void setShop_add(String shop_add) {
		this.shop_add = shop_add;
	}
	public String getWap_theme_path() {
		return wap_theme_path;
	}
	public void setWap_theme_path(String wap_theme_path) {
		this.wap_theme_path = wap_theme_path;
	}
	public Integer getWap_themeid() {
		return wap_themeid;
	}
	public void setWap_themeid(Integer wap_themeid) {
		this.wap_themeid = wap_themeid;
	}
	
}
