package com.enation.app.shop.goods.model.po;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品实体
 * 
 * @author fk
 * @version v1.0 2017年4月1日 下午3:14:46
 */
@ApiModel
public class Goods implements java.io.Serializable {

	private static final long serialVersionUID = 2248564538065672409L;
	@ApiModelProperty(hidden = true)
	private Integer goods_id;
	@ApiModelProperty(value = "商品名称", example = "春装特价", required = true)
	@NotBlank(message = "商品名称不能为空")
	private String goods_name;
	@ApiModelProperty(value = "商品编号", example = "26735627", required = true)
	@NotBlank(message = "商品编号不能为空")
	private String sn;
	@ApiModelProperty(value = "品牌id")
	private Integer brand_id;
	@ApiModelProperty(value = "分类id", example = "1", required = true)
	@NotNull(message = "分类不能为空")
	private Integer category_id;
	@ApiModelProperty(hidden = true)
	private String goods_type; // enum('normal', 'bind') default 'normal', options 'service'
	@ApiModelProperty(value = "单位", required = false)
	private String unit;
	@ApiModelProperty(value = "商品重量", required = true)
	@NotNull(message = "商品重量不能为空")
	private Double weight;
	@ApiModelProperty(hidden = true)
	private Integer market_enable; // 上下架 上架：1 下架：0 预览：2
	@ApiModelProperty(value = "商品说明（简介）", required = false)
	private String brief;
	@ApiModelProperty(value = "商品详情", required = false)
	private String intro;
	@NotNull(message = "商品价格不能为空")
	@ApiModelProperty(value = "商品价格", required = true)
	private Double price;
	@NotNull(message = "成本价格不能为空")
	@ApiModelProperty(value = "成本价格", required = true)
	private Double cost;
	@NotNull(message = "市场价格不能为空")
	@ApiModelProperty(value = "市场价格", required = true)
	private Double mktprice;
	@ApiModelProperty(value = "是否有规格，1有 0没有", required = true)
	private Integer have_spec;
	@ApiModelProperty(hidden = true)
	private Long create_time;
	@ApiModelProperty(hidden = true)
	private Long last_modify;
	@ApiModelProperty(hidden = true)
	private Integer view_count;
	@ApiModelProperty(hidden = true)
	private Integer buy_count;
	@ApiModelProperty(hidden = true)
	private Integer disabled; // 删除： 回收站：1 正常 ：0
	@ApiModelProperty(hidden = true)
	private Integer quantity;
	@ApiModelProperty(hidden = true)
	private Integer enable_quantity;
	@ApiModelProperty(hidden = true)
	private Integer point; // 积分
	@ApiModelProperty(hidden = true)
	private String page_title;
	@ApiModelProperty(hidden = true)
	private String meta_keywords;
	@ApiModelProperty(hidden = true)
	private String meta_description;
	@ApiModelProperty(hidden = true)
	private Integer sord;
	@ApiModelProperty(hidden = true)
	private Integer grade;
	@ApiModelProperty(hidden = true)
	private String goods_comment;
	@ApiModelProperty(hidden = true)
	private Integer is_pack;
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
	@ApiModelProperty(value = "商家分类分组id", required = true)
	private Integer shop_cat_id;
	@ApiModelProperty(hidden = true)
	private Integer comment_num;
	@ApiModelProperty(value = "运费模板，买家付运费时，必传", required = false)
	private Integer template_id;
	@ApiModelProperty(value = "运费承担，0买家承担  1卖家承担", required = true)
	private Integer goods_transfee_charge;// 运费承担，0买家承担 1卖家承担
	@ApiModelProperty(hidden = true)
	private String seller_name;
	@ApiModelProperty(hidden = true)
	private Double commission;// 商品佣金比例
	@ApiModelProperty(hidden = true)
	private Integer is_auth;
	@ApiModelProperty(hidden = true)
	private String auth_message;

	public Goods() {

	}

	public Goods(GoodsVo goodsVo) {
		this.goods_id = goodsVo.getGoods_id();
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
		this.unit = goodsVo.getUnit();
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

	@PrimaryKeyField
	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(Integer buy_count) {
		this.buy_count = buy_count;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getMarket_enable() {
		return market_enable;
	}

	public void setMarket_enable(Integer market_enable) {
		this.market_enable = market_enable;
	}

	public String getMeta_description() {
		return meta_description;
	}

	public void setMeta_description(String meta_description) {
		this.meta_description = meta_description;
	}

	public String getMeta_keywords() {
		return meta_keywords;
	}

	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}

	public Double getMktprice() {
		return mktprice;
	}

	public void setMktprice(Double mktprice) {
		this.mktprice = mktprice;
	}

	public String getPage_title() {
		return page_title;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goodsType) {
		goods_type = goodsType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getView_count() {
		return view_count;
	}

	public void setView_count(Integer view_count) {
		this.view_count = view_count;
	}

	public Double getWeight() {
		weight = weight == null ? weight = 0D : weight;
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Long getLast_modify() {
		return last_modify;
	}

	public void setLast_modify(Long last_modify) {
		this.last_modify = last_modify;
	}

	public Integer getPoint() {
		point = point == null ? 0 : point;
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getSord() {
		return sord;
	}

	public void setSord(Integer sord) {
		this.sord = sord;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getHave_spec() {
		return have_spec;
	}

	public void setHave_spec(Integer have_spec) {
		this.have_spec = have_spec;
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

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getGoods_comment() {
		return goods_comment;
	}

	public void setGoods_comment(String goods_comment) {
		this.goods_comment = goods_comment;
	}

	public Integer getIs_pack() {
		return is_pack;
	}

	public void setIs_pack(Integer is_pack) {
		this.is_pack = is_pack;
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

	public Integer getComment_num() {
		return comment_num;
	}

	public void setComment_num(Integer comment_num) {
		this.comment_num = comment_num;
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

	public Integer getIs_auth() {
		return is_auth;
	}

	public void setIs_auth(Integer is_auth) {
		this.is_auth = is_auth;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public String getAuth_message() {
		return auth_message;
	}

	public void setAuth_message(String auth_message) {
		this.auth_message = auth_message;
	}

}