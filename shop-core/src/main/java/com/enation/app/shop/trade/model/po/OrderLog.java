package com.enation.app.shop.trade.model.po;

import com.enation.framework.database.NotDbField;
import com.enation.framework.database.PrimaryKeyField;
import com.enation.framework.util.DateUtil;

/**
 * 订单日志
 * @author kingapex
 *2010-4-6下午05:24:16
 */
public class OrderLog {
	 	
	private Integer  log_id;	//日志Id
	private String order_sn;	//订单Id
	private String op_name;		//管理员／会员名
	private String message;		//日志信息
	private Long op_time;		//创建时间

	@PrimaryKeyField
	public Integer getLog_id() {
		return log_id;
	}

	public String getOp_name() {
		return op_name;
	}

	public void setOp_name(String op_name) {
		this.op_name = op_name;
	}

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Long getOp_time() {
		return op_time;
	}

	public void setOp_time(Long op_time) {
		this.op_time = op_time;
	}


	public void setLog_id(Integer log_id) {
		this.log_id = log_id;
	}


	public String getOrder_sn() {
		return order_sn;
	}


	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	} 
	
	@NotDbField
	public String getOp_time_str(){
		
		return DateUtil.toString(op_time, "yyyy-MM-dd HH:mm:ss");
	}
	
}
