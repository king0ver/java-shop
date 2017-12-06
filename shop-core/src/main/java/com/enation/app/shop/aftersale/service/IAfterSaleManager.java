package com.enation.app.shop.aftersale.service;

import java.util.List;

import com.enation.app.shop.aftersale.model.vo.BuyerRefundApply;
import com.enation.app.shop.aftersale.model.vo.FinanceRefundApproval;
import com.enation.app.shop.aftersale.model.vo.RefundDetail;
import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.aftersale.model.vo.RefundQueryParam;
import com.enation.app.shop.aftersale.model.vo.RefundVo;
import com.enation.app.shop.aftersale.model.vo.SellerRefundApproval;
import com.enation.app.shop.aftersale.model.vo.StockIn;
import com.enation.framework.database.Page;

/**
 * 售后管理接口
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月14日下午6:39:02
 */
public interface IAfterSaleManager {
	
	/**
	 * 申请退款
	 * @param refundApply 退款申请
	 */
	public void applyRefund(BuyerRefundApply refundApply);
	
	
	
	/**
	 * 退货申请
	 * @param goodsReturnsApply
	 */
	public void applyGoodsReturn(BuyerRefundApply refundApply);
	
	
	
	
	/**
	 * 根据参数查询退款（货）单
	 * @param param 查询参数
	 * @return
	 */
	public Page<RefundVo>  query(RefundQueryParam param);
	
	
	/**
	 * 根据编号获取详细
	 * @param sn 单据编号
	 * @return
	 */
	public RefundDetail getDetail(String sn);
	
	
	/**
	 * 卖家审批一个退货（款）
	 * @param refundApprove 批准 vo
	 * @return 批准 vo
	 */
	public SellerRefundApproval approval(SellerRefundApproval refundApproval); 
	
	
	/**
	 * 卖家入库
	 * @param stockIn 入库单
	 * @return
	 */
	public StockIn SellerStockIn(StockIn stockIn) ;
	
	
	
	/**
	 * 财务审核/执行一个退款
	 * @param refundApproval
	 * @return
	 */
	public FinanceRefundApproval approval(FinanceRefundApproval refundApproval);

	/**
	 * 买家取消退款或退货
	 * @param sn
	 * @return
	 */
	public RefundVo cancelRefund(String sn);


	/**
	 * @return
	 */
	public List<RefundPartVo> queryNoReturnOrder();


	/**
	 * 更新退款单的状态
	 * @param list
	 */
	public void update(List<RefundPartVo> list); 

	
}
