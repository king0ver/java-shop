package com.enation.app.shop.waybill.model.waybilljson;
/**
 * 电子面单收发件人的信息实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月12日 下午6:39:22
 */
public class Information {
	/** 收发件人公司 */
	private String Company; 
	/** 收发件人 */
	private String Name; 
	/** 手机 */
	private String Mobile; 
	/** 电话 */
	private String Tel;
	/** 收发件省（如广东省，不要缺少“省”）*/
	private String ProvinceName; 
	/** 收发件市（如深圳市，不要缺少“市” */
	private String CityName; 
	/** 收发件区（如福田区，不要缺少“区”或“县”）*/
	private String ExpAreaName; 
	/** 收发件详细地址 */
	private String Address; 
	/** 收发件人邮编 */
	private String PostCode; 
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	public String getProvinceName() {
		return ProvinceName;
	}
	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getExpAreaName() {
		return ExpAreaName;
	}
	public void setExpAreaName(String expAreaName) {
		ExpAreaName = expAreaName;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getPostCode() {
		return PostCode;
	}
	public void setPostCode(String postCode) {
		PostCode = postCode;
	}
	
	
	
	

}
