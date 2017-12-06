package com.enation.app.shop.shop.setting.model.po;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.enation.framework.util.JsonUtil;

import io.swagger.annotations.ApiParam;

public class ShipTemplate implements Serializable {

	/** 
	* <p>Title: </p> 
	* <p>Description: </p>  
	*/
	private static final long serialVersionUID = -2310849247997108107L;
	
	/**
	 * 模版id
	 */
	@ApiParam(hidden=true,value="模版id")
	private Integer template_id;

	@ApiParam("商家id")
	private Integer seller_id;
	
	@ApiParam("名字")
	private String name;

	@ApiParam("首重／首件")
	private Integer first_company;

	@ApiParam("运费")
	private Double first_price;

	@ApiParam("续重／需件")
	private Integer continued_company;

	@ApiParam("续费")
	private Double continued_price;

	@ApiParam("地区‘，‘分隔")
	private String area;

	@ApiParam("模版类型，1 重量算运费 2 计件算运费")
	private Integer type;
	
	@ApiParam("地区id‘，‘分隔")
	private String area_id;
	
	@ApiParam("地区Json")
	private String area_json;
	
	public Integer getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public Integer getFirst_company() {
		return first_company;
	}

	public void setFirst_company(Integer first_company) {
		this.first_company = first_company;
	}

	public Double getFirst_price() {
		return first_price;
	}

	public void setFirst_price(Double first_price) {
		this.first_price = first_price;
	}

	public Integer getContinued_company() {
		return continued_company;
	}

	public void setContinued_company(Integer continued_company) {
		this.continued_company = continued_company;
	}

	public Double getContinued_price() {
		return continued_price;
	}

	public void setContinued_price(Double continued_price) {
		this.continued_price = continued_price;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 1 重量算运费 2 计件算运费
	 * @return
	 */
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getArea_json() {
		return area_json;
	}

	public void setArea_json(String area_json) {
		this.area_json = area_json;
	}

	public void putArea(String area) {
		List<Map<String, Object>> list = JsonUtil.toList(area);
		StringBuffer _area=new StringBuffer("");
		StringBuffer _area_id=new StringBuffer("");
		for (Map<String, Object> map : list) {
			_area.append(map.get("local_name")+",");
			_area_id.append(map.get("region_id")+",");
		}
		
		this.area=_area.substring(0, _area.length()-1).toString();
		this.area_id=_area_id.substring(0, _area_id.length()-1).toString();
	}
	
}
