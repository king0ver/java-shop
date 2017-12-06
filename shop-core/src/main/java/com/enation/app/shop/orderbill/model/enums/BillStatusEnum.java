package com.enation.app.shop.orderbill.model.enums;

/**
 * 
 * 结算单枚举 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 上午10:34:30
 */
public enum BillStatusEnum {

	NEW("未确认", 0), OUT("已出账", 1), RECON("已对账", 2), PASS("已审核", 3), PAY("已付款", 4), COMPLETE("已完成", 5);
	private String name;
	private int index;

	// 构造方法
	private BillStatusEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (BillStatusEnum b : BillStatusEnum.values()) {
			if (b.getIndex() == index) {
				return b.name;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
