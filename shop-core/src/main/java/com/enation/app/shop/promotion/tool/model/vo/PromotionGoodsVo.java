package com.enation.app.shop.promotion.tool.model.vo;

import java.io.Serializable;

import com.enation.app.shop.promotion.bonus.model.StoreBonusType;
import com.enation.app.shop.promotion.exchange.model.po.Exchange;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuy;
import com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo;
import com.enation.app.shop.promotion.minus.model.vo.MinusVo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 活动商品对照表Vo
 * 
 * @author xulipeng
 * @since v6.4
 * @version v1.0 2017年08月22日
 */
public class PromotionGoodsVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4796645552318671313L;

	@ApiModelProperty(value = "商品id")
	private Integer goods_id;

	@ApiModelProperty(value = "商品图片")
	private String thumbnail;

	@ApiModelProperty(value = "商品名称")
	private String name;

	@ApiModelProperty(value = "货品id")
	private Integer product_id;

	@ApiModelProperty(value = "活动开始时间")
	private Long start_time;

	@ApiModelProperty(value = "活动结束时间")
	private Long end_time;

	@ApiModelProperty(value = "活动id")
	private Integer activity_id;

	/**
	 * 查看PromotionTypeEnum
	 */
	@ApiModelProperty(value = "活动工具类型")
	private String promotion_type;

	@ApiModelProperty(value = "活动名称")
	private String title;
	
	@ApiModelProperty(value = "积分兑换对象")
	private Exchange exchange;
	
	@ApiModelProperty(value = "团购活动对象")
	private GroupBuy groupBuy;
	
	@ApiModelProperty(value = "满优惠活动")
	private FullDiscountVo fullDiscountVo;
	
	@ApiModelProperty(value = "满赠的优惠券实体")
	private StoreBonusType storeBonusType;
	
	@ApiModelProperty(value = "满赠的赠品实体")
	private FullDiscountGift fullDiscountGift;
	
	@ApiModelProperty(value = "单品立减活动")
	private MinusVo minusVo;
	
	@ApiModelProperty(value = "第二件半价活动")
	private HalfPriceVo halfPriceVo;

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}

	public Long getStart_time() {
		return start_time;
	}

	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}

	public Long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}

	public Integer getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(Integer activity_id) {
		this.activity_id = activity_id;
	}

	public String getPromotion_type() {
		return promotion_type;
	}

	public void setPromotion_type(String promotion_type) {
		this.promotion_type = promotion_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}

	public GroupBuy getGroupBuy() {
		return groupBuy;
	}

	public void setGroupBuy(GroupBuy groupBuy) {
		this.groupBuy = groupBuy;
	}

	public FullDiscountVo getFullDiscountVo() {
		return fullDiscountVo;
	}

	public void setFullDiscountVo(FullDiscountVo fullDiscountVo) {
		this.fullDiscountVo = fullDiscountVo;
	}

	public StoreBonusType getStoreBonusType() {
		return storeBonusType;
	}

	public void setStoreBonusType(StoreBonusType storeBonusType) {
		this.storeBonusType = storeBonusType;
	}

	public FullDiscountGift getFullDiscountGift() {
		return fullDiscountGift;
	}

	public void setFullDiscountGift(FullDiscountGift fullDiscountGift) {
		this.fullDiscountGift = fullDiscountGift;
	}

	public MinusVo getMinusVo() {
		return minusVo;
	}

	public void setMinusVo(MinusVo minusVo) {
		this.minusVo = minusVo;
	}

	public HalfPriceVo getHalfPriceVo() {
		return halfPriceVo;
	}

	public void setHalfPriceVo(HalfPriceVo halfPriceVo) {
		this.halfPriceVo = halfPriceVo;
	}
	

}
