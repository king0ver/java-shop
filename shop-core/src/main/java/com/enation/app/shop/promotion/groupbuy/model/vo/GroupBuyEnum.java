package com.enation.app.shop.promotion.groupbuy.model.vo;

/**
 * 团购状态枚举 
 * @author Chopper
 * @version v1.0
 * @since v6.x
 * 2017年9月7日 下午8:00:21 
 *
 */
public enum GroupBuyEnum {

	OVERDUE("已结束"),CONDUCT("进行中"),NOTBEGIN("未开始");

	private String status; 
	
	
	private GroupBuyEnum(String status) {
		this.status=status;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	
	public static void main(String[] args) {
		System.out.println(GroupBuyEnum.CONDUCT.getStatus());
	}
}
