package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;



/**
 * 商品关联的参数值实体类
 * @author fk
 * @version v1.0
 * 2017年4月7日 下午4:02:12
 */
public class GoodsParams implements Serializable{

	private static final long serialVersionUID = 4302544666062976122L;
	private Integer id        ; 
	private Integer goods_id  ; 
	private Integer param_id  ; 
	private String param_name;
	private String param_value; 
	
	public GoodsParams() {
		
	}
	
	public GoodsParams(Integer id, Integer goods_id, Integer param_id, String param_name, String param_value) {
		super();
		this.id = id;
		this.goods_id = goods_id;
		this.param_id = param_id;
		this.param_name = param_name;
		this.param_value = param_value;
	}

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	public Integer getParam_id() {
		return param_id;
	}
	public void setParam_id(Integer param_id) {
		this.param_id = param_id;
	}
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public String getParam_value() {
		return param_value;
	}
	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

}
