package com.enation.app.shop.member.model.po;

import java.io.Serializable;

import com.enation.app.shop.member.model.vo.AddressVo;
import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 地址
 * @author fk
 * @version v1.0
 * 2017年2月24日 下午4:23:08
 */
@ApiModel
public class Address implements Serializable{

	private static final long serialVersionUID = -7727289995368987515L;
	@ApiModelProperty(hidden = true)
	private Integer addr_id;	//ID
	@ApiModelProperty(hidden = true)
	private Integer member_id;	//会员id
	@ApiModelProperty(required = true,value="收货人")
	private String name;		//会员名称
	@ApiModelProperty(required = true,value="省ID")
	private Integer province_id; 	//省ID
	@ApiModelProperty(required = true,value="市ID")
	private Integer city_id;		//市ID
	@ApiModelProperty(required = true,value="区ID")
	private Integer region_id;		//区ID
	@ApiModelProperty(required = false,value="街道ID")
	private Integer town_id;		//街道ID
	@ApiModelProperty(required = true,value="省")
	private String province;		//省
	@ApiModelProperty(required = true,value="市")
	private String city;			//市
	@ApiModelProperty(required = true,value="区")
	private String region;			//区
	@ApiModelProperty(required = false,value="街道")
	private String town;		//街道
	@ApiModelProperty(required = true,value="详细地址")
	private String addr;			//详细地址
	@ApiModelProperty(required = false,value="邮编")
	private String zip;				//邮编
	@ApiModelProperty(required = false,value="电话")
	private String tel;				//电话
	@ApiModelProperty(required = true,value="手机号")
	private String mobile;			//手机号
	@ApiModelProperty(required = false,value="是否是默认地址，1表示默认地址")
	private Integer def_addr;		//是否为默认收货地址
	@ApiModelProperty(required = false,value="备注")
	private String remark;			//备注
	@ApiModelProperty(hidden = true)
	private Integer isDel;
	@ApiModelProperty(required = false,value="地址别名")
	private String shipAddressName; //地址别名
	
	public  AddressVo castAddrVo(){
		AddressVo vo = new AddressVo();
		vo.setAddr(addr);
		vo.setAddr_id(addr_id);
		vo.setCity(city);
		vo.setCity_id(city_id);
		vo.setDef_addr(def_addr);
		vo.setIsDel(isDel);
		vo.setMember_id(member_id);
		vo.setMobile(mobile);
		vo.setName(name);
		vo.setName(name);
		vo.setProvince(province);
		vo.setProvince_id(province_id);
		vo.setRegion(region);
		vo.setRegion_id(region_id);
		vo.setRemark(remark);
		vo.setShipAddressName(shipAddressName);
		vo.setTel(tel);
		vo.setTown(town);
		vo.setTown_id(town_id);
		vo.setZip(zip);		
		return vo;
	}
//	
//	private String addressToBeEdit;//编辑时候引用的数组
//	
//	
//	
//	@NotDbField
//	public String getAddressToBeEdit() {
//		return addressToBeEdit;
//	}

//	public void setAddressToBeEdit(String addressToBeEdit) {
//		this.addressToBeEdit = addressToBeEdit;
//	}

	@PrimaryKeyField
	public Integer getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(Integer addr_id) {
		this.addr_id = addr_id;
	}
	
	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public Integer getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Integer province_id) {
		this.province_id = province_id;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public Integer getDef_addr() {
		if(def_addr==null){
			def_addr=0;
		}
		return def_addr;
	}

	public void setDef_addr(Integer def_addr) {
		this.def_addr = def_addr;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShipAddressName() {
		return shipAddressName;
	}

	public void setShipAddressName(String shipAddressName) {
		this.shipAddressName = shipAddressName;
	}

	public Integer getTown_id() {
		return town_id;
	}

	public void setTown_id(Integer town_id) {
		this.town_id = town_id;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}
	
	
}
