package com.enation.app.shop.aftersale.model.vo;

import com.enation.app.shop.aftersale.model.po.RefundGoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退货(款)单详细
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月25日下午4:53:07
 */
@ApiModel(description = "退货(款)单详细")
public class RefundDetail {

	@ApiModelProperty(value = "退货（款）单")
	private RefundVo refund;

	@ApiModelProperty(value = "退货商品列表")
	private RefundGoods goods;

	public RefundVo getRefund() {
		return refund;
	}

	public void setRefund(RefundVo refund) {
		this.refund = refund;
	}

	public RefundGoods getGoods() {
		return goods;
	}

	public void setGoods(RefundGoods goods) {
		this.goods = goods;
	}

}
