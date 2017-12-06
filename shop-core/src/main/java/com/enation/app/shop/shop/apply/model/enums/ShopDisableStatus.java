package com.enation.app.shop.shop.apply.model.enums;
/**
 * (店铺状态) 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年8月15日 下午3:27:28
 */
public enum ShopDisableStatus {
	
	open("开启中"),
	closed("店铺关闭"),
	apply("申请开店"),
	refused("审核拒绝"),
	applying("申请中");
	
	private String description;

	ShopDisableStatus(String _description) {
		this.description = _description;
	}

	public String description() {
		return this.description;
	}

	public String value() {
		return this.name();
	}
}
