package com.enation.app.shop.aftersale.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.aftersale.model.enums.RefuseType;
import com.enation.app.shop.aftersale.model.vo.BuyerRefundApply;
import com.enation.app.shop.aftersale.model.vo.FinanceRefundApproval;
import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.aftersale.model.vo.RefundVo;
import com.enation.app.shop.aftersale.model.vo.SellerRefundApproval;
import com.enation.app.shop.aftersale.model.vo.StockIn;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 退款申请控制器
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月25日下午5:39:34
 */
@Api(description = "退货（款）操作API")
@RestController
@RequestMapping("/after-sale")
@Validated
public class RefundOperationController {
	
	
	@Autowired
	private IAfterSaleManager afterSaleManager;
	
	
	@ApiOperation(value = "买家申请退款", response = BuyerRefundApply.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "refund_price", value = "退款金额", required = true, dataType = "double" ,paramType="query"),
        @ApiImplicitParam(name = "refund_way", value = "退款方式", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "account_type", value = "账号类型", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "return_account", value = "退款账号", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "customer_remark", value = "客户备注", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "refund_reason", value = "退款原因", required = true, dataType = "String" ,paramType="query"),
	})
	@PostMapping(value = "/mine/refund/apply")
	public BuyerRefundApply buyerApply(@Valid @ApiIgnore BuyerRefundApply refundApply) {
		refundApply.setRefuseType(RefuseType.return_money.value());
		afterSaleManager.applyRefund(refundApply);

		return refundApply;
	}
	
	
	@ApiOperation(value = "买家申请退货", response = BuyerRefundApply.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "order_sn", value = "订单编号", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "refund_price", value = "退款金额", required = true, dataType = "double" ,paramType="query"),
        @ApiImplicitParam(name = "refund_way", value = "退款方式", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "account_type", value = "账号类型", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "return_account", value = "退款账号", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "customer_remark", value = "客户备注", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "refund_reason", value = "退款原因", required = true, dataType = "String" ,paramType="query"),
	})
	@PostMapping(value = "/mine/return-goods/apply")
	public BuyerRefundApply buyerApplyReturnGoods(@Valid @ApiIgnore BuyerRefundApply refundApply) {
		refundApply.setRefuseType(RefuseType.return_goods.value());
		afterSaleManager.applyGoodsReturn(refundApply);
		
		return refundApply;
	}
	
	@ApiOperation(value = "买家取消退款退货", response = BuyerRefundApply.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sn", value = "退款(货)编号", required =true, dataType = "String" ,paramType="path")
	})
	@PostMapping(value = "/mine/refund/cancel/{sn}")
	public RefundVo buyerCancelRefund(@PathVariable String sn){
		
		RefundVo refund = afterSaleManager.cancelRefund(sn);
		return refund;
	}
	
	@ApiOperation(value = "卖家审核退款/退货", response = SellerRefundApproval.class)
	@PostMapping(value = "/seller/refund/approval/{sn}")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sn", value = "退款(货)编号", required =true, dataType = "String" ,paramType="path"),
        @ApiImplicitParam(name = "agree", value = "是否同意退款", required =true, dataType = "boolean" ,paramType="query"),
        @ApiImplicitParam(name = "refund_price", value = "退款金额", required =true, dataType = "Double" ,paramType="query"),
        @ApiImplicitParam(name = "refund_point", value = "退还积分", required =false, dataType = "int" ,paramType="query")
        
	 })
	public SellerRefundApproval auth(SellerRefundApproval refundApproval,@PathVariable String sn) {

		refundApproval.setSn(sn);
		
		afterSaleManager.approval(refundApproval);

		return refundApproval;
	}
	
	@ApiOperation(value = "卖家入库操作", response = StockIn.class)
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sn", value = "退款(货)编号", required =true, dataType = "String" ,paramType="path")
	 })
	@PostMapping(value = "/seller/refund-goods/stock-in/{sn}")
	public StockIn stockIn(@PathVariable String sn){
		
		StockIn stockIn = new StockIn();
		stockIn.setSn(sn);
		
		afterSaleManager.SellerStockIn(stockIn);
		
		return stockIn;
	}
	
	
	@ApiOperation(value = "平台退款", response = SellerRefundApproval.class)
	@PostMapping(value = "/admin/refund/approval/{sn}")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "sn", value = "退款(货)编号", required =true, dataType = "String" ,paramType="path")
	 })
	public FinanceRefundApproval sellerApply(FinanceRefundApproval refundApply,@PathVariable String sn){
		
		refundApply.setSn(sn);
		
		this.afterSaleManager.approval(refundApply);
		
		return refundApply;
	}
	
	/**
	 
	 * @param refundApply
	 * @param sn
	 * @return
	 */
	@ApiIgnore
	@PostMapping(value = "/client/refund/status")
	public void sellerApply(@RequestBody List<RefundPartVo> list){
		
		this.afterSaleManager.update(list);
	}
	

}
