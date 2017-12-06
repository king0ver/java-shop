package com.enation.app.shop.aftersale.model.enums;

/**
 * 退货状态
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月24日下午7:53:52
 */
public enum RefundStatus {
	
	apply("申请中"), 
	pass("审核通过"),
	refuse("审核拒绝"),
	all_stock_in("全部入库"), 
	part_stock_in("部分入库"),
	cancel("取消退货"), 
	refunding("退款中"), 
	refundfail("退款失败"), 
	completed("已完成");

	private String description;

	RefundStatus(String _description) {
		this.description = _description;
	}

	public String description() {
		return this.description;
	}

	public String value() {
		return this.name();
	}
}
