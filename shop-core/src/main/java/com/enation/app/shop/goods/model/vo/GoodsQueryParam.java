package com.enation.app.shop.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 商品查询条件
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 2017年8月14日 下午2:12:59
 */
@ApiModel
public class GoodsQueryParam {
	/** 页码 */
	@ApiModelProperty(value = "页码")
	private Integer page_no;
	/** 分页数 */
	@ApiModelProperty(value = "分页数")
	private Integer page_size;
	/** 是否上架 0代表已下架，1代表已上架，2 代表草稿 */
	@ApiModelProperty(value = "是否上架 0代表已下架，1代表已上架，2 代表草稿")
	private Integer market_enable;
	/** 店铺分类 */
	@ApiModelProperty(value = "店铺分类")
	private Integer shop_cat_id;
	/** 关键字 */
	@ApiModelProperty(value = "关键字")
	private String keyword;
	/** 0 普通搜索 1 高级搜索,默认普通搜索 */
	@ApiModelProperty(value = "0 普通搜索  1 高级搜索,默认普通搜索")
	private Integer stype;
	/** 商品名称 */
	@ApiModelProperty(value = "商品名称")
	private String goods_name;
	/** 商品编号 */
	@ApiModelProperty(value = "商品编号")
	private String goods_sn;
	@ApiModelProperty(value = "店铺名称")
	private String seller_name;
	/** 卖家id */
	@ApiModelProperty(value = "卖家id")
	private Integer seller_id;
	@ApiModelProperty(value = "商品分类id")
	private Integer category_id;

	public Integer getPage_no() {
		return page_no;
	}

	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}

	public Integer getMarket_enable() {
		return market_enable;
	}

	public void setMarket_enable(Integer market_enable) {
		this.market_enable = market_enable;
	}

	public Integer getShop_cat_id() {
		return shop_cat_id;
	}

	public void setShop_cat_id(Integer shop_cat_id) {
		this.shop_cat_id = shop_cat_id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getStype() {
		return stype;
	}

	public void setStype(Integer stype) {
		this.stype = stype;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_sn() {
		return goods_sn;
	}

	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

}
