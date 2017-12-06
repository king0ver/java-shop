package com.enation.framework.jms.support.goods;

import java.io.Serializable;

/**
 * 分类变更消息vo
 * @author fk
 * @version v1.0
 * 2017年4月28日 下午3:39:53
 */
public class CategoryChangeMsg implements Serializable{

	private static final long serialVersionUID = 6042345706426853823L;
	
	private Integer category_id;//分类id
	
	private Integer operation_type;//操作类型
	
	public final static int ADD_OPERATION = 1;//添加
	
	public final static int UPDATE_OPERATION = 2;//修改
	
	public final static int DEL_OPERATION = 3;//删除

	
	public CategoryChangeMsg(Integer category_id, Integer operation_type) {
		super();
		this.category_id = category_id;
		this.operation_type = operation_type;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public Integer getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(Integer operation_type) {
		this.operation_type = operation_type;
	}
	
	
}
