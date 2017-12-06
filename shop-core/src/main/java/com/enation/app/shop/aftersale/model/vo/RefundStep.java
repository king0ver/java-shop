package com.enation.app.shop.aftersale.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.enation.app.shop.aftersale.model.enums.RefundOperate;
import com.enation.app.shop.aftersale.model.enums.RefundStatus;

/**
 * 退款/货步骤
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月23日上午11:27:05
 */
public class RefundStep {
	
	private  RefundStatus status;
	
	private List<RefundOperate> allowableOperate;

	
	public RefundStep(RefundStatus _status,RefundOperate... operates ){
		this.status = _status;
		this.allowableOperate = new ArrayList<RefundOperate>();
		for (RefundOperate refundOperate : operates) {
			allowableOperate.add(refundOperate);
		}
		
	}
	
	/**
	 * 检测操作是否在步骤中
	 * @param operate
	 * @return
	 */
	public boolean checkAllowable(RefundOperate operate){
		for (RefundOperate orderOperate : allowableOperate) {
			if(operate.compareTo(orderOperate)==0){
				return true;
			}
		}
		return false;
	}
	
}
