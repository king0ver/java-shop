package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.goods.model.po.DraftGoods;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.promotion.exchange.model.po.Exchange;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品vo
 * 
 * @author fk
 * @version v1.0 2017年4月12日 下午7:48:28
 */
public class GoodsVo implements Serializable {

	private static final long serialVersionUID = 3922135264669953741L;
	private Integer goods_id;
	private Integer category_id;
	private String category_name;
	private Integer shop_cat_id;
	private Integer brand_id;
	private String goods_name;// 商品名称
	private String sn;// 商品编号
	private Double price;
	private Double cost;
	private Double mktprice;
	private Double weight;
	private Integer goods_transfee_charge;// 0：买家承担，1：卖家承担
	private String intro;
	private Integer have_spec;
	private Integer quantity;
	private String unit;
	private Integer disabled; // 删除： 回收站：1 正常 ：0
	@ApiModelProperty(hidden = true)
	private Integer market_enable; // 上下架 上架：1 下架：0 预览：2
	private Integer[] selectSpec;
	private List<GoodsSkuVo> skuList;
	private List<GoodsParams> goodsParamsList;
	private List<GoodsGallery> goodsGalleryList;
	private Exchange exchange;
	private Integer seller_id;
	private Integer enable_quantity;
	private Integer has_changed;// sku数据变化或者组合变化判断 0:没变化，1：变化
	
	private String small;  //商品小图  分享用
	@ApiModelProperty(hidden = true)
	private String page_title;
	@ApiModelProperty(hidden = true)
	private String meta_keywords;
	@ApiModelProperty(hidden = true)
	private String meta_description;
	
	public GoodsVo(Goods goods) {
		this.goods_id = goods.getGoods_id();
		this.category_id = goods.getCategory_id();
		this.shop_cat_id = goods.getShop_cat_id();
		this.brand_id = goods.getBrand_id();
		this.goods_name = goods.getGoods_name();
		this.sn = goods.getSn();
		this.price = goods.getPrice();
		this.cost = goods.getCost();
		this.mktprice = goods.getMktprice();
		this.weight = goods.getWeight();
		this.goods_transfee_charge = goods.getGoods_transfee_charge();
		this.intro = goods.getIntro();
		this.have_spec = goods.getHave_spec();
		this.unit = goods.getUnit();
		this.market_enable = goods.getMarket_enable();
		this.quantity = goods.getQuantity();
		this.disabled = goods.getDisabled();
		this.seller_id = goods.getSeller_id();
		this.enable_quantity = goods.getEnable_quantity();
		this.small = goods.getSmall();
		
	}

	public GoodsVo(DraftGoods goods) {
		this.goods_id = goods.getDraft_goods_id();
		this.category_id = goods.getCategory_id();
		this.shop_cat_id = goods.getShop_cat_id();
		this.brand_id = goods.getBrand_id();
		this.goods_name = goods.getGoods_name();
		this.sn = goods.getSn();
		this.price = goods.getPrice();
		this.cost = goods.getCost();
		this.mktprice = goods.getMktprice();
		this.weight = goods.getWeight();
		this.goods_transfee_charge = goods.getGoods_transfee_charge();
		this.intro = goods.getIntro();
		this.have_spec = goods.getHave_spec();
		this.quantity = goods.getQuantity();
		this.disabled = goods.getDisabled();
		this.seller_id = goods.getSeller_id();
		this.enable_quantity = goods.getEnable_quantity();
	}

	public GoodsVo() {
	}

	public Integer[] getSelectSpec() {
		return selectSpec;
	}

	public void setSelectSpec(Integer[] selectSpec) {
		this.selectSpec = selectSpec;
	}

	public Integer getHave_spec() {
		return have_spec;
	}

	public void setHave_spec(Integer have_spec) {
		this.have_spec = have_spec;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public Integer getShop_cat_id() {
		return shop_cat_id;
	}

	public void setShop_cat_id(Integer shop_cat_id) {
		this.shop_cat_id = shop_cat_id;
	}

	public Integer getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public List<GoodsGallery> getGoodsGalleryList() {
		return goodsGalleryList;
	}

	public void setGoodsGalleryList(List<GoodsGallery> goodsGalleryList) {
		this.goodsGalleryList = goodsGalleryList;
	}

	public Integer getGoods_transfee_charge() {
		return goods_transfee_charge;
	}

	public void setGoods_transfee_charge(Integer goods_transfee_charge) {
		this.goods_transfee_charge = goods_transfee_charge;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public List<GoodsSkuVo> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<GoodsSkuVo> skuList) {
		this.skuList = skuList;
	}

	public List<GoodsParams> getGoodsParamsList() {
		return goodsParamsList;
	}

	public void setGoodsParamsList(List<GoodsParams> goodsParamsList) {
		this.goodsParamsList = goodsParamsList;
	}

	public Integer getMarket_enable() {
		return market_enable;
	}

	public void setMarket_enable(Integer market_enable) {
		this.market_enable = market_enable;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	public Integer getEnable_quantity() {
		return enable_quantity;
	}

	public void setEnable_quantity(Integer enable_quantity) {
		this.enable_quantity = enable_quantity;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public Integer getHas_changed() {
		return has_changed;
	}

	public void setHas_changed(Integer has_changed) {
		this.has_changed = has_changed;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getPage_title() {
		return page_title;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}

	public String getMeta_keywords() {
		return meta_keywords;
	}

	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}

	public String getMeta_description() {
		return meta_description;
	}

	public void setMeta_description(String meta_description) {
		this.meta_description = meta_description;
	}
	
	

}
