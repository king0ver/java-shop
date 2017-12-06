package com.enation.app.shop.aftersale.model.po;

import java.io.Serializable;

import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.database.PrimaryKeyField;
import com.google.gson.Gson;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退货商品表
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月14日下午4:15:26
 */
@ApiModel(description = "退货(款)单详细")
public class RefundGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7565611082209856905L;

	@ApiModelProperty(hidden = true)
	private Integer id;

	@ApiModelProperty(value = "退货(款)编号")
	private String refund_sn;

	@ApiModelProperty(value = "商品id")
	private Integer goods_id;

	@ApiModelProperty(value = "产品id")
	private Integer sku_id;

	@ApiModelProperty(value = "发货数量")
	private Integer ship_num;

	@ApiModelProperty(value = "商品价格")
	private Double price;

	@ApiModelProperty(value = "退货数量")
	private Integer return_num;

	@ApiModelProperty(value = "入库数量")
	private Integer storage_num;

	@ApiModelProperty(value = "商品编号")
	private String goods_sn;

	@ApiModelProperty(value = "商品名称")
	private String goods_name;

	@ApiModelProperty(value = "商品图片")
	private String goods_image;

	@ApiModelProperty(value = "赠品信息")
	private String refund_gift;
	@ApiModelProperty(hidden = true)
	private String spec_json;

	public RefundGoods(Product product) {
		this.goods_id = product.getGoods_id();
		this.sku_id = product.getProduct_id();
		this.return_num = product.getNum();
		this.goods_sn = product.getProduct_sn();
		this.goods_name = product.getName();
		this.goods_image = product.getGoods_image();
		this.ship_num = product.getNum();
		Gson gson = new Gson();
		this.spec_json = gson.toJson(product.getSpecList());
	}

	public RefundGoods() {

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

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public Integer getShip_num() {
		return ship_num;
	}

	public void setShip_num(Integer ship_num) {
		this.ship_num = ship_num;
	}

	public Integer getReturn_num() {
		return return_num;
	}

	public void setReturn_num(Integer return_num) {
		this.return_num = return_num;
	}

	public Integer getStorage_num() {
		return storage_num;
	}

	public void setStorage_num(Integer storage_num) {
		this.storage_num = storage_num;
	}

	public String getGoods_sn() {
		return goods_sn;
	}

	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_image() {
		return goods_image;
	}

	public void setGoods_image(String goods_image) {
		this.goods_image = goods_image;
	}

	public String getSpec_json() {
		return spec_json;
	}

	public void setSpec_json(String spec_json) {
		this.spec_json = spec_json;
	}

	public String getRefund_sn() {
		return refund_sn;
	}

	public void setRefund_sn(String refund_sn) {
		this.refund_sn = refund_sn;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getRefund_gift() {
		return refund_gift;
	}

	public void setRefund_gift(String refund_gift) {
		this.refund_gift = refund_gift;
	}

}
