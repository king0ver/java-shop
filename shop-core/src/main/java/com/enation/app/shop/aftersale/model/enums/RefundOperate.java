package com.enation.app.shop.aftersale.model.enums;

/**
 * 退款操作
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月23日上午11:03:20
 */
public enum RefundOperate {
	
	apply("申请退货"), 
	seller_approval("卖家审核"),
	stock_in("退货入库"),
	cancel("取消"),
	admin_approval("管理员审核");
	
	private String description;
	
	RefundOperate(String _description) {
		this.description = _description;
	}

	public String description() {
		return this.description;
	}

	public String value() {
		return this.name();
	}
	
}
