package com.enation.app.shop.aftersale.model.vo;

import java.io.Serializable;

import com.enation.app.shop.aftersale.model.enums.RefundOperate;
import com.enation.app.shop.aftersale.model.enums.RefundStatus;
import com.enation.app.shop.aftersale.model.enums.RefuseType;
import com.enation.app.shop.aftersale.service.RefundOperateChecker;

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
	
	private RefuseType type;
	private RefundStatus status;
	
	public OperateAllowable(RefuseType _type,RefundStatus _status){
		this.type = _type;
		this.status =_status;
	}
	
	@ApiModelProperty(value = "是否允许被取消" )
	private boolean allowCancel;
	
	@ApiModelProperty(value = "是否允许申请" )
	private boolean allowApply;
	
	@ApiModelProperty(value = "是否允许退货入库" )
	private boolean allowStockIn;
	
	@ApiModelProperty(value = "是否管理员审核" )
	private boolean allowAdminApproval;
	
	@ApiModelProperty(value = "是否允许商家审核" )
	private boolean allowSellerApproval;
	
	public boolean isAllowCancel() {
		allowCancel = RefundOperateChecker.checkAllowable(type, status,RefundOperate.cancel);
		return allowCancel;
	}


	public boolean isAllowApply() {
		allowApply = RefundOperateChecker.checkAllowable(type, status,RefundOperate.apply);
		return allowApply;
	}


	public boolean isAllowStockIn() {
		allowStockIn = RefundOperateChecker.checkAllowable(type, status,RefundOperate.stock_in);
		return allowStockIn;
	}


	public boolean isAllowAdminApproval() {
		allowAdminApproval = RefundOperateChecker.checkAllowable(type, status,RefundOperate.admin_approval);
		return allowAdminApproval;
	}


	public boolean isAllowSellerApproval() {
		allowSellerApproval = RefundOperateChecker.checkAllowable(type, status,RefundOperate.seller_approval);
		return allowSellerApproval;
	}

}
