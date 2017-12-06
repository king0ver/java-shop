package com.enation.app.shop.snapshot.model.po;

import java.io.Serializable;

/**
 * 
 * 商品快照实体类
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年10月27日 下午2:40:10
 */
public class Snapshot implements Serializable{
	/**快照id*/
	private Integer snapshot_id;
	
	/**商品ID*/
	private Integer goods_id;
	
	/**商品名称*/
	private String name;
	
	/**商品编号*/
	private String sn;
	
	/**商品品牌名称*/
	private String brand_name;
	
	/**分类名称*/
	private String category_name;
	
	/**商品类型*/
	private String goods_type;
	
	/**商品计价单位*/
	private String unit;
	
	/**商品重量*/
	private Double weight;
	
	/**商品详情*/
	private String intro;
	
	/**商品价格*/
	private Double price ;
	
	/**商品成本价*/
	private Double cost;
	
	/**商品市场价*/
	private Double mktprice;
	
	/**是否开启规格*/
	private Integer have_spec;
	
	/**参数字符串*/
	private String params_json;
	
	/**图片字符串*/
	private String img_json;
	
	/**快照时间*/
	private Long create_time;
	
	/**商品积分*/
	private Integer point;
	
	/**店铺ID*/
	private Integer shop_id;

	public Integer getSnapshot_id() {
		return snapshot_id;
	}

	public void setSnapshot_id(Integer snapshot_id) {
		this.snapshot_id = snapshot_id;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getMktprice() {
		return mktprice;
	}

	public void setMktprice(Double mktprice) {
		this.mktprice = mktprice;
	}

	public Integer getHave_spec() {
		return have_spec;
	}

	public void setHave_spec(Integer have_spec) {
		this.have_spec = have_spec;
	}

	public String getParams_json() {
		return params_json;
	}

	public void setParams_json(String params_json) {
		this.params_json = params_json;
	}

	public String getImg_json() {
		return img_json;
	}

	public void setImg_json(String img_json) {
		this.img_json = img_json;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	
	
}
