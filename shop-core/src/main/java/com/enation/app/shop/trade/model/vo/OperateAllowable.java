package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

import com.enation.app.shop.trade.model.enums.CommentStatus;
import com.enation.app.shop.trade.model.enums.OrderOperate;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.support.OrderOperateChecker;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 操作被允许的情况
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年6月5日下午9:31:58
 */
@ApiModel(  description = "操作被允许的情况")
public class OperateAllowable implements Serializable{
	private static final long serialVersionUID = -6083914452276811925L;
	
	public OperateAllowable(){}
	
	private PaymentType paymentType;
	private OrderStatus status;
	private CommentStatus commentStatus;
	private ShipStatus shipStatus;
	private ServiceStatus serviceStatus;
	private PayStatus payStatus;
	
	public OperateAllowable(PaymentType _paymentType,OrderStatus _status,CommentStatus _commentStatus,ShipStatus _shipStatus,
			ServiceStatus _serviceStatus,PayStatus _payStatus){
		this.paymentType = _paymentType;
		this.status =_status;
		this.commentStatus = _commentStatus;
		this.shipStatus = _shipStatus;
		this.serviceStatus = _serviceStatus;
		this.payStatus = _payStatus;
	}
	
	@ApiModelProperty(value = "是否允许被取消" )
	private boolean allowCancel;
	
	@ApiModelProperty(value = "是否允许被确认" )
	private boolean allowConfirm;
	
	@ApiModelProperty(value = "是否允许被支付" )
	private Boolean allowPay;
	
	@ApiModelProperty(value = "是否允许被发货" )
	private boolean allowShip;
	
	@ApiModelProperty(value = "是否允许被收货" )
	private boolean allowRog;
	
	@ApiModelProperty(value = "是否允许被评论" )
	private boolean allowComment;
	
	@ApiModelProperty(value = "是否允许被完成" )
	private boolean allowComplete;
	
	@ApiModelProperty(value = "是否允许申请售后" )
	private boolean allowApplyService;
	


	public boolean isAllowCancel() {
		//货到付款  没有发货的订单可以取消
		allowCancel =OrderOperateChecker.checkAllowable(paymentType, status, OrderOperate.cancel);
		return allowCancel;
	}


	public boolean isAllowConfirm() {
		allowConfirm =OrderOperateChecker.checkAllowable(paymentType, status, OrderOperate.confirm);
		return allowConfirm;
	}


	public boolean isAllowPay() {
		if(allowPay==null) {
			allowPay = OrderOperateChecker.checkAllowable(paymentType, status, OrderOperate.pay);
		}
		return allowPay;
	}


	/**
	 * 设置是否允许支付，提供外部化的可能的更改
	 * @param _allowPay
	 */
	public void setAllowPay(boolean _allowPay){
		this.allowPay=_allowPay;
	}

	public boolean isAllowShip() {
		allowShip =OrderOperateChecker.checkAllowable(paymentType, status, OrderOperate.ship);
		return allowShip;
	}


	public boolean isAllowRog() {
		allowRog =OrderOperateChecker.checkAllowable(paymentType, status, OrderOperate.rog);
		return allowRog;
	}


	public boolean isAllowComment() {
		
		allowComment = CommentStatus.UNFINISHED.value().equals(this.commentStatus.value())&&ShipStatus.SHIP_ROG.value().equals(this.shipStatus.value());
		//如果是未评论状态且已收货，就可以评论
		return allowComment; 
	}


	public boolean isAllowComplete() {
		allowComplete = OrderOperateChecker.checkAllowable(paymentType, status, OrderOperate.complete);
		return allowComplete;
	}
	
	public boolean isAllowApplyService() {
		boolean service_status = serviceStatus.NOT_APPLY.value().equals(this.serviceStatus.value());
		//货到付款
		if(PaymentType.cod.compareTo( paymentType)==0){
			
			allowApplyService = ShipStatus.SHIP_ROG.value().equals(this.shipStatus.value())&&service_status;
			
		}else{
			
			allowApplyService = PayStatus.PAY_YES.value().equals(this.payStatus.value())&&service_status&&!OrderStatus.CANCELLED.equals(this.status.value());
		}
		
		
		return allowApplyService;
	}


	
}
