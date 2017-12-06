package com.enation.framework.jms.support.goods;

import java.io.Serializable;

/**
 * 商品变更消息vo
 * @author fk
 * @version v1.0
 * 2017年4月28日 下午12:03:48
 */
public class GoodsChangeMsg implements Serializable{

	private static final long serialVersionUID = 3352769927238407770L;

	private Integer[] goods_ids;//变更资源，商品id集合
	
	private Integer operation_type;//操作类型
	
	public final static int ADD_OPERATION = 1;//添加
	
	public final static int UPDATE_OPERATION = 2;//修改
	
	public final static int DEL_OPERATION = 3;//删除
	
	public final static int UNDER_OPERATION = 4;//下架
	
	public final static int REVERT_OPERATION = 5;//还原
	
	public final static int INRECYCLE_OPERATION = 6;//放入回收站

	public final static int GOODS_VERIFY = 7;//商品审核失败
	
	private String message;

	public GoodsChangeMsg(Integer[] goods_ids, Integer operation_type) {
		super();
		this.goods_ids = goods_ids;
		this.operation_type = operation_type;
	}
	
	public GoodsChangeMsg(Integer[] goods_ids, Integer operation_type,String message) {
		super();
		this.goods_ids = goods_ids;
		this.operation_type = operation_type;
		this.message = message;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
