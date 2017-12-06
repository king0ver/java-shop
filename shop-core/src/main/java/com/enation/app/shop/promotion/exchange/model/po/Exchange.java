package com.enation.app.shop.promotion.exchange.model.po;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;

/**
 * 
 * 积分兑换设置实体
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月23日 下午2:28:28
 */
public class Exchange implements Serializable {

	private static final long serialVersionUID = -4252191776544933920L;

	private Integer setting_id;

	/** 商品id */
	private Integer goods_id;

	/** 是否允许兑换 */
	private Integer enable_exchange;

	/** 兑换所需金额 */
	private double exchange_money;

	/** 商品所属积分分类 */
	private Integer category_id;

	/** 兑换所需积分 */
	private Integer exchange_point;
	/** 商品对照表信息 */
	private List<PromotionGoodsVo> goodsList;

	public Integer getSetting_id() {
		return setting_id;
	}

	public void setSetting_id(Integer setting_id) {
		this.setting_id = setting_id;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getEnable_exchange() {
		return enable_exchange;
	}

	public void setEnable_exchange(Integer enable_exchange) {
		this.enable_exchange = enable_exchange;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public Integer getExchange_point() {
		return exchange_point;
	}

	public void setExchange_point(Integer exchange_point) {
		this.exchange_point = exchange_point;
	}

	public double getExchange_money() {
		return exchange_money;
	}

	public void setExchange_money(double exchange_money) {
		this.exchange_money = exchange_money;
	}

	public List<PromotionGoodsVo> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<PromotionGoodsVo> goodsList) {
		this.goodsList = goodsList;
	}

}
