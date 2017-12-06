package com.enation.app.shop.aftersale.model.vo;

import java.io.Serializable;
import java.util.List;

import com.enation.app.shop.aftersale.model.po.RefundGoods;
import com.enation.app.shop.goods.model.vo.SpecValueVo;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 退货商品表vo
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年11月23日 上午10:16:53
 */
public class RefundGoodsVo extends RefundGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919542469949262867L;

	/**
	 * 商品规格列表
	 */
	@ApiModelProperty(value = "规格列表")
	private List<SpecValueVo> specList;
	/**
	 * 赠品列表
	 */
	@ApiModelProperty(value = "赠品列表")
	private List<FullDiscountGift> giftList;

	public List<SpecValueVo> getSpecList() {
		Gson gson = new Gson();
		List<SpecValueVo> specList = gson.fromJson(this.getSpec_json(), new TypeToken<List<SpecValueVo>>() {
		}.getType());
		return specList;
	}

	public void setSpecList(List<SpecValueVo> specList) {
		this.specList = specList;
	}

	public List<FullDiscountGift> getGiftList() {
		Gson gson = new Gson();
		List<FullDiscountGift> giftList = gson.fromJson(this.getRefund_gift(), new TypeToken<List<FullDiscountGift>>() {
		}.getType());
		return giftList;
	}

	public void setGiftList(List<FullDiscountGift> giftList) {
		this.giftList = giftList;
	}

}
