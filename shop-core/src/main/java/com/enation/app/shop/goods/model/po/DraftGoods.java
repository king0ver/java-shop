package com.enation.app.shop.goods.model.po;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.enation.app.shop.goods.model.vo.GoodsVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 草稿箱商品po
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月27日 上午11:44:08
 */
@ApiModel
public class DraftGoods implements Serializable {
	private static final long serialVersionUID = 2853770909779540694L;

	public DraftGoods() {

	}

	private Integer draft_goods_id;
	@ApiModelProperty(value = "商品编号")
	private String sn;
	@ApiModelProperty(value = "商品名称")
	private String goods_name;
	@ApiModelProperty(value = "品牌id")
	private Integer brand_id;
	@ApiModelProperty(value = "分类id")
	private Integer category_id;
	@ApiModelProperty(value = "商品重量")
	private Double weight;
	@ApiModelProperty(value = "商品详情", required = false)
	private String intro;
	@ApiModelProperty(value = "商品价格")
	private Double price;
	@ApiModelProperty(value = "成本价格")
	private Double cost;
	@NotNull(message = "市场价格不能为空")
	@ApiModelProperty(value = "市场价格")
	private Double mktprice;
	@ApiModelProperty(hidden = true)
	private String goods_type; // enum('normal', 'bind') default 'normal', options 'service'
	@ApiModelProperty(value = "是否有规格，1有 0没有", required = true)
	private Integer have_spec;
	@ApiModelProperty(hidden = true)
	private Long create_time;
	@ApiModelProperty(hidden = true)
	private Integer disabled; // 删除： 回收站：1 正常 ：0
	@ApiModelProperty(hidden = true)
	private Integer quantity;
	@ApiModelProperty(hidden = true)
	private Integer enable_quantity;
	@ApiModelProperty(hidden = true)
	private String thumbnail;
	@ApiModelProperty(hidden = true)
	private String big;
	@ApiModelProperty(hidden = true)
	private String small;
	@ApiModelProperty(hidden = true)
	private String original;
	@ApiModelProperty(hidden = true)
	private Integer seller_id;
	@ApiModelProperty(value = "商家分类分组id")
	private Integer shop_cat_id;
	@ApiModelProperty(value = "运费模板，买家付运费时，必传", required = false)
	private Integer template_id;
	@ApiModelProperty(value = "运费承担，0买家承担  1卖家承担", required = true)
	private Integer goods_transfee_charge;// 运费承担，0买家承担 1卖家承担
	@ApiModelProperty(hidden = true)
	private String seller_name;
	@ApiModelProperty(value = "兑换价格")
	private Double exchange_money;
	@ApiModelProperty(value = "兑换积分")
	private Integer exchange_point;
	@ApiModelProperty(value = "兑换分类")
	private Integer exchange_category;
	@ApiModelProperty(value = "seo标题")
	private String page_title;
	@ApiModelProperty(value = "seo关键字")
	private String meta_keywords;
	@ApiModelProperty(value = "seo描述")
	private String meta_description;
	
	public DraftGoods(GoodsVo goodsVo) {
		this.draft_goods_id = goodsVo.getGoods_id();
		this.category_id = goodsVo.getCategory_id();
		this.shop_cat_id = goodsVo.getShop_cat_id();
		this.brand_id = goodsVo.getBrand_id();
		this.goods_name = goodsVo.getGoods_name();
		this.sn = goodsVo.getSn();
		this.price = goodsVo.getPrice();
		this.cost = goodsVo.getCost();
		this.mktprice = goodsVo.getMktprice();
		this.weight = goodsVo.getWeight();
		this.goods_transfee_charge = goodsVo.getGoods_transfee_charge();
		this.intro = goodsVo.getIntro();
		this.have_spec = goodsVo.getHave_spec();
		if (goodsVo.getExchange().getEnable_exchange() == 1) {
			this.goods_type = "point";
			this.exchange_money = goodsVo.getExchange().getExchange_money();
			this.exchange_point = goodsVo.getExchange().getExchange_point();
			this.exchange_category = goodsVo.getExchange().getCategory_id();
		} else {
			this.goods_type = "normal";
			this.exchange_money = 0.00;
			this.exchange_point = 0;
			this.exchange_category = 1;
		}
		if (goodsVo.getPage_title() == null || "".equals(goodsVo.getPage_title())) {
			this.page_title = goodsVo.getGoods_name();
		} else {
			this.page_title = goodsVo.getPage_title();
		}
		if (goodsVo.getMeta_keywords() == null || "".equals(goodsVo.getMeta_keywords())) {
			this.meta_keywords = goodsVo.getGoods_name();
		} else {
			this.meta_keywords = goodsVo.getMeta_keywords();
		}
		if (goodsVo.getMeta_description() == null || "".equals(goodsVo.getMeta_description())) {
			this.meta_description = goodsVo.getGoods_name();
		} else {
			this.meta_description = goodsVo.getMeta_description();
		}
	}

	public Integer getDraft_goods_id() {
		return draft_goods_id;
	}

	public void setDraft_goods_id(Integer draft_goods_id) {
		this.draft_goods_id = draft_goods_id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public Integer getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
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

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public Integer getHave_spec() {
		return have_spec;
	}

	public void setHave_spec(Integer have_spec) {
		this.have_spec = have_spec;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getEnable_quantity() {
		return enable_quantity;
	}

	public void setEnable_quantity(Integer enable_quantity) {
		this.enable_quantity = enable_quantity;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getBig() {
		return big;
	}

	public void setBig(String big) {
		this.big = big;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public Integer getShop_cat_id() {
		return shop_cat_id;
	}

	public void setShop_cat_id(Integer shop_cat_id) {
		this.shop_cat_id = shop_cat_id;
	}

	public Integer getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}

	public Integer getGoods_transfee_charge() {
		return goods_transfee_charge;
	}

	public void setGoods_transfee_charge(Integer goods_transfee_charge) {
		this.goods_transfee_charge = goods_transfee_charge;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public Double getExchange_money() {
		return exchange_money;
	}

	public void setExchange_money(Double exchange_money) {
		this.exchange_money = exchange_money;
	}

	public Integer getExchange_point() {
		return exchange_point;
	}

	public void setExchange_point(Integer exchange_point) {
		this.exchange_point = exchange_point;
	}

	public Integer getExchange_category() {
		return exchange_category;
	}

	public void setExchange_category(Integer exchange_category) {
		this.exchange_category = exchange_category;
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
