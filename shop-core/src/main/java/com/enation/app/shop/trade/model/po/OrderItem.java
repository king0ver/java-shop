package com.enation.app.shop.trade.model.po;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.database.PrimaryKeyField;
import com.enation.framework.util.StringUtil;
import com.google.gson.Gson;

/**
 * 订单货物PO
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月23日下午6:26:55
 */
public class OrderItem implements Serializable {

	private static final long serialVersionUID = -531541394274183503L;
	private Integer item_id;
	private String order_sn;
	private Integer goods_id;
	private Integer product_id;
	private Integer num;
	private Integer ship_num;
	private String name;
	private String image;
	private Integer cat_id; // 此商品的分类
	private Double price;
	private int state;// 订单货物状态 0为出库成功，1出库失败
	private String trade_sn;
	private String spec_json;
	private Integer snapshot_id;

	public OrderItem() {

	}

	public OrderItem(Product product) {
		this.setGoods_id(product.getGoods_id());
		this.setProduct_id(product.getProduct_id());
		this.setCat_id(product.getCat_id());
		this.setImage(product.getGoods_image());
		this.setOrder_sn(product.getProduct_sn());
		this.setName(product.getName());
		this.setNum(product.getNum());
		List<SpecValueVo> specList = product.getSpecList();

		// 规格json
		Gson gson = new Gson();
		String _specJson = gson.toJson(specList);
		this.spec_json = _specJson;

		// TODO 还需要记录原价
		this.setPrice(product.getPurchase_price());

	}

	@PrimaryKeyField
	public Integer getItem_id() {
		return item_id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getShip_num() {
		return ship_num;
	}

	public void setShip_num(Integer ship_num) {
		this.ship_num = ship_num;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public String getImage() {

		if (!StringUtil.isEmpty(image)) {
			image = StaticResourcesUtil.convertToUrl(image);
		}

		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}

	public String getTrade_sn() {
		return trade_sn;
	}

	public void setTrade_sn(String trade_sn) {
		this.trade_sn = trade_sn;
	}

	public String getSpec_json() {
		return spec_json;
	}

	public void setSpec_json(String spec_json) {
		this.spec_json = spec_json;
	}

	public Integer getSnapshot_id() {
		return snapshot_id;
	}

	public void setSnapshot_id(Integer snapshot_id) {
		this.snapshot_id = snapshot_id;
	}

}