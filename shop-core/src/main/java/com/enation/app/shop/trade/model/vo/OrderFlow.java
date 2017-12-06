package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

import com.enation.app.shop.trade.model.enums.OrderStatus;

/**
 * 订单流程
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月24日 上午10:58:58
 */
public class OrderFlow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8737862640684638513L;

	private String text;//文字
	
	private String order_status;//订单状态
	
	private Integer show_status;//0 灰色    1 普通显示     2   结束显示   3 取消显示 

	public OrderFlow() {
		
	}
	
	public OrderFlow(OrderStatus orderStatus) {
		super();
		this.text = orderStatus.description();
		this.order_status = orderStatus.value();
		this.show_status = 0;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public Integer getShow_status() {
		return show_status;
	}

	public void setShow_status(Integer show_status) {
		this.show_status = show_status;
	}

	
}
