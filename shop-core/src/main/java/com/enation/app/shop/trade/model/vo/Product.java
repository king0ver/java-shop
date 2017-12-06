package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.trade.model.enums.ServiceStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 购物车中的产品
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月22日下午12:47:48
 */
@ApiModel(value = "Product", description = "产品")
public class Product implements Serializable {

	private static final long serialVersionUID = -1461899980939662926L;

	/**
	 * 在构造器里初始化促销列表，规格列表
	 */
	public Product() {
		this.is_check = 1;
		specList = new ArrayList<SpecValueVo>();
	}

	@ApiModelProperty(value = "卖家id")
	private int seller_id;

	@ApiModelProperty(value = "卖家姓名")
	private String seller_name;

	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private int goods_id;

	/**
	 * 货品id
	 */
	@ApiModelProperty(value = "产品id")
	private int product_id;

	/**
	 * 货品编号
	 */
	@ApiModelProperty(value = "产品sn")
	private String product_sn;

	/**
	 * 商品所属的分类id
	 */
	@ApiModelProperty(value = "商品所属的分类id")
	private int cat_id;

	/**
	 * 购买数量
	 */
	@ApiModelProperty(value = "购买数量")
	private int num;

	/**
	 * 商品重量
	 */
	@ApiModelProperty(value = "商品重量")
	private double goods_weight;

	@ApiModelProperty(value = "商品原价")
	private double original_price;

	/**
	 * 购买时的单个商品成交价
	 */
	@ApiModelProperty(value = "购买时的成交价")
	private double purchase_price;

	/**
	 * 此处的小计为商品计算完促销活动后的优惠金额*数量
	 */
	@ApiModelProperty(value = "小计")
	private double subtotal;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String name;

	/**
	 * 商品图片
	 */
	@ApiModelProperty(value = "商品图片")
	private String goods_image;

	/**
	 * 是否选中，要去结算 0:未选中 1:已选中，默认
	 */
	@ApiModelProperty(value = "是否选中，要去结算")
	private int is_check;

	/**
	 * 1：商家承担运费（免运费） 0：买家承担运费
	 */
	@ApiModelProperty(value = "是否免运费")
	private int is_free_freight;

	@ApiModelProperty(value = "已参与的单品活动工具列表")
	private List<CartPromotionGoodsVo> single_list;

	@ApiModelProperty(value = "已参与的组合活动工具列表")
	private List<CartPromotionGoodsVo> group_list;

	/**
	 * 商品规格列表
	 */
	@ApiModelProperty(value = "规格列表")
	private List<SpecValueVo> specList;

	/**
	 * 积分换购活动中，购买这个商品需要消费的积分。
	 */
	@ApiModelProperty(value = "使用积分")
	private Integer point;

	@ApiModelProperty(value = "快照ID")
	private int snapshot_id;
	@ApiModelProperty(value = "售后状态")
	private String service_status;

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getProduct_sn() {
		return product_sn;
	}

	public void setProduct_sn(String product_sn) {
		this.product_sn = product_sn;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getGoods_weight() {
		return goods_weight;
	}

	public void setGoods_weight(double goods_weight) {
		this.goods_weight = goods_weight;
	}

	public double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(double original_price) {
		this.original_price = original_price;
	}

	public double getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(double purchase_price) {
		this.purchase_price = purchase_price;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoods_image() {
		return goods_image;
	}

	public void setGoods_image(String goods_image) {
		this.goods_image = goods_image;
	}

	public int getIs_check() {
		return is_check;
	}

	public void setIs_check(int is_check) {
		this.is_check = is_check;
	}

	public int getIs_free_freight() {
		return is_free_freight;
	}

	public void setIs_free_freight(int is_free_freight) {
		this.is_free_freight = is_free_freight;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public List<CartPromotionGoodsVo> getSingle_list() {
		return single_list;
	}

	public void setSingle_list(List<CartPromotionGoodsVo> single_list) {
		this.single_list = single_list;
	}

	public List<CartPromotionGoodsVo> getGroup_list() {
		return group_list;
	}

	public void setGroup_list(List<CartPromotionGoodsVo> group_list) {
		this.group_list = group_list;
	}

	public List<SpecValueVo> getSpecList() {
		return specList;
	}

	public void setSpecList(List<SpecValueVo> specList) {
		this.specList = specList;
	}

	public int getSnapshot_id() {
		return snapshot_id;
	}

	public void setSnapshot_id(int snapshot_id) {
		this.snapshot_id = snapshot_id;
	}

	public String getService_status() {
		if (service_status == null) {
			service_status = ServiceStatus.NOT_APPLY.value();
		}
		return service_status;
	}

	public void setService_status(String service_status) {
		this.service_status = service_status;
	}

}