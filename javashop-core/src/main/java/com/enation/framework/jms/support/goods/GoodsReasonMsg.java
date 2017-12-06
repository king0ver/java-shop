package com.enation.framework.jms.support.goods;

import java.io.Serializable;

/**
 * 
 * 商品变更消息带原因的vo
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月20日 下午1:42:11
 */
public class GoodsReasonMsg implements Serializable {
	private static final long serialVersionUID = -7737770607802551908L;
	private Integer[] goods_ids;// 变更资源，商品id集合

	private Integer operation_type;// 操作类型
	private String reason;// 原因
	public final static int UNDER_OPERATION = 1;// 下架
	public final static int GOODS_VERIFY = 2;// 商品审核失败

	public GoodsReasonMsg(Integer[] goods_ids, Integer operation_type, String reason) {
		super();
		this.goods_ids = goods_ids;
		this.operation_type = operation_type;
		this.reason = reason;
	}

	public Integer[] getGoods_ids() {
		return goods_ids;
	}

	public void setGoods_ids(Integer[] goods_ids) {
		this.goods_ids = goods_ids;
	}

	public Integer getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(Integer operation_type) {
		this.operation_type = operation_type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
