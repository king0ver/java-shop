package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;

import com.enation.app.cms.floor.model.enumeration.CmsManageMsgType;

/**
 * 
 * 为发送消息提供的模型 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:28:16
 */
public class CmsManageMsg implements Serializable{
	
	private static final long serialVersionUID = 7363703171010224557L;
	/**资源id*/
	private Integer source_id;//资源id
	/**消息类型*/
	private CmsManageMsgType type;
	/**操作类型 1:为新增或修改  2:删除*/
	private Integer operation_type;
	public final static int ADD_OPERATION=1;
	public final static int DEL_OPERATION=2;
	public CmsManageMsg(Integer source_id, CmsManageMsgType type,Integer operation_type) {
		super();
		this.source_id = source_id;
		this.type = type;
		this.operation_type=operation_type;
	}
	public Integer getSource_id() {
		return source_id;
	}
	public void setSource_id(Integer source_id) {
		this.source_id = source_id;
	}
	public CmsManageMsgType getType() {
		return type;
	}
	public void setType(CmsManageMsgType type) {
		this.type = type;
	}
	public Integer getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(Integer operation_type) {
		this.operation_type = operation_type;
	}
	
	
	
}
