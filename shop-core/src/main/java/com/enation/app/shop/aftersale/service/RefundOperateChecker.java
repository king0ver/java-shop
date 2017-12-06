package com.enation.app.shop.aftersale.service;

import java.util.HashMap;
import java.util.Map;

import com.enation.app.shop.aftersale.model.enums.RefundOperate;
import com.enation.app.shop.aftersale.model.enums.RefundStatus;
import com.enation.app.shop.aftersale.model.enums.RefuseType;
import com.enation.app.shop.aftersale.model.vo.RefundStep;

/**
 * 退货操作检测，看某状态下是否允许某操作
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年5月23日上午11:29:35
 */
public class RefundOperateChecker {

	/**
	 * 退款
	 */
	private static final Map<RefundStatus, RefundStep> refundFlow = new HashMap<RefundStatus, RefundStep>();
	/**
	 * 退货
	 */
	private static final Map<RefundStatus, RefundStep> returnFlow = new HashMap<RefundStatus, RefundStep>();

	/**
	 * 申请中状态下，可做的操作
	 */
	private static final RefundStep applyStep = new RefundStep(RefundStatus.apply, RefundOperate.cancel, RefundOperate.seller_approval);
	
	/**
	 * 审核拒绝的状态下，可做的操作
	 */
	private static final RefundStep refuseStep = new RefundStep(RefundStatus.refuse,RefundOperate.apply);
	
	/**
	 * 退款中状态下可做的操作
	 */
	private static final RefundStep refundingStep = new RefundStep(RefundStatus.refunding);
	
	/**
	 * 退款失败状态下可做的操作
	 */
	private static final RefundStep refundfailStep = new RefundStep(RefundStatus.refundfail,RefundOperate.admin_approval);
	
	/**
	 * 已完成状态下，可做的操作
	 */
	private static final RefundStep completedStep = new RefundStep(RefundStatus.completed);

	static{
		initRefundflow();
		initReturnflow();
	}
	
	/**
	 * 初始化退款流程
	 */
	private static void initRefundflow() {

		RefundStep passStep = new RefundStep(RefundStatus.pass, RefundOperate.admin_approval);
		refundFlow.put(RefundStatus.apply, applyStep);
		refundFlow.put(RefundStatus.pass, passStep);
		refundFlow.put(RefundStatus.refuse, refuseStep);
		refundFlow.put(RefundStatus.refunding, refundingStep);
		refundFlow.put(RefundStatus.refundfail, refundfailStep);
		refundFlow.put(RefundStatus.completed, completedStep);

	}

	public static void main(String[] args) {
		initReturnflow();
	}
	/**
	 * 初始化退货流程
	 */
	private static void initReturnflow() {
		/**
		 * 审核通过状态下，可做的操作
		 */
		RefundStep passStep = new RefundStep(RefundStatus.pass, RefundOperate.stock_in);
		RefundStep partStockInStep = new RefundStep(RefundStatus.part_stock_in, RefundOperate.stock_in);
		RefundStep allStockInStep = new RefundStep(RefundStatus.all_stock_in, RefundOperate.admin_approval);

		returnFlow.put(RefundStatus.apply, applyStep);
		returnFlow.put(RefundStatus.pass, passStep);
		returnFlow.put(RefundStatus.refuse, refuseStep);
		returnFlow.put(RefundStatus.completed, completedStep);
		returnFlow.put(RefundStatus.refunding, refundingStep);
		returnFlow.put(RefundStatus.refundfail, refundfailStep);
		returnFlow.put(RefundStatus.part_stock_in, partStockInStep);
		returnFlow.put(RefundStatus.all_stock_in, allStockInStep);
		
	}
	
	/**
	 * 校验操作是否允许
	 * @param type
	 * @param status
	 * @param operate
	 * @return
	 */
	public static boolean checkAllowable(RefuseType type,RefundStatus status,RefundOperate operate){
		
		Map<RefundStatus,RefundStep> flow = null;
		if(RefuseType.return_money.compareTo(type)==0 ){
			flow = refundFlow;//退款
		}else{
			flow = returnFlow;//退货
		}
		
		if(flow == null){
			return false;
		}
		
		RefundStep step =  flow.get(status);
		
		return step.checkAllowable(operate);
		
	}

}
