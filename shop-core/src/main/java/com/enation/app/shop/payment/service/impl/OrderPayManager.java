package com.enation.app.shop.payment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.aftersale.model.vo.RefundBill;
import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.aftersale.model.vo.RefundVo;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.payment.model.po.PaymentBill;
import com.enation.app.shop.payment.model.po.PaymentMethod;
import com.enation.app.shop.payment.model.vo.PayBill;
import com.enation.app.shop.payment.service.IOrderPayManager;
import com.enation.app.shop.payment.service.IPaymentBillManager;
import com.enation.app.shop.payment.service.IPaymentPlugin;
import com.enation.app.shop.trade.model.enums.TradeType;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.ResourceNotFoundException;
import com.enation.framework.validator.UnProccessableServiceException;


/**
 * 订单支付业务
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月3日下午11:18:18
 */
@Service
public class OrderPayManager implements IOrderPayManager {
	
	@Autowired
	private List<IPaymentPlugin> paymentPluginList;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IPaymentBillManager paymentBillManager;
	
	@Autowired
	private IAfterSaleManager afterSaleManager;
	
	
	
	/**
	 * 支付一个账单
	 * @param bill
	 * @param payment_method_id 选择的支付方式，如果没有选择使用bill中的支付方式
	 * @return
	 */
	private String pay(PayBill bill,Integer payment_method_id){
		String pluginid = "";
//		
		//指定了支付方式
		if( payment_method_id!=null  ){
			PaymentMethod paymentMethod  = this.daoSupport.queryForObject("select * from es_payment_method where method_id=? ", PaymentMethod.class, payment_method_id);
			if( paymentMethod== null ){
				throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "未找到相应的支付方式["+payment_method_id+"]");
			}
			pluginid=paymentMethod.getPlugin_id();
		}else{
			pluginid = bill.getPayment_plugin_id();
		}
		
		String sn = System.currentTimeMillis()+""  ;
		
		//这里生成支付流水
		PaymentBill paymentStream = new PaymentBill();
		paymentStream.setPay_key(sn);
		paymentStream.setSn(bill.getTrade_sn());
		paymentStream.setIs_pay(0);
		paymentBillManager.add(paymentStream);
		
		bill.setTrade_sn(sn);
		
//		//调起插件
		IPaymentPlugin plugin = this.findPlugin(pluginid);
		String html = plugin.onPay(bill);
	
		return html;
	}
	
	@Override
	public String payOrder(String order_sn,Integer payment_method_id,String pay_mode,String client_type) {
		
		PayBill bill = this.daoSupport.queryForObject("select  order_id , sn as trade_sn,  order_price,payment_method_id,payment_plugin_id , payment_method_name from es_order  where sn=?", PayBill.class, order_sn);
 
		if(bill==null){
			throw new RuntimeException("未找到相应的交易["+order_sn+"]");
		}
		
		bill.setTradeType(TradeType.order);
		bill.setPay_mode(pay_mode);
		bill.setClientType(ClientType.valueOf(client_type));
		
		return this.pay(bill, payment_method_id);
		 	
	}

	@Override
	public String payRecharge(String rechange_sn, Integer payment_method_id,String pay_mode,String client_type){

		PayBill bill = this.daoSupport.queryForObject("select recharge_id as order_id , recharge_sn as trade_sn, price order_price from es_nanshan_recharge_record  where recharge_sn=?",
				PayBill.class, rechange_sn);

		if(bill==null){
			throw new RuntimeException("未找到相应的充值订单["+rechange_sn+"]");
		}

		bill.setTradeType(TradeType.recharge);
		bill.setPay_mode(pay_mode);
		bill.setClientType(ClientType.valueOf(client_type));

		return this.pay(bill, payment_method_id);


	}


	@Override
	public String payTrade(String  trade_sn,Integer payment_method_id,String pay_mode,String client_type) {
		
		PayBill bill = this.daoSupport.queryForObject("select trade_id as order_id ,trade_sn, total_price as order_price,payment_method_id,payment_plugin_id , payment_method_name from es_trade  where trade_sn=?", PayBill.class, trade_sn);
	
		if(bill==null){
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "该交易不存在");
		}
		bill.setPay_mode(pay_mode);
		bill.setTradeType(TradeType.trade);
		bill.setClientType(ClientType.valueOf(client_type));
		
		return this.pay(bill, payment_method_id);
	}


	@Override
	public String payReturn(TradeType tradeType,String pluginId) {
		IPaymentPlugin plugin = this.findPlugin(pluginId);
		return plugin.onReturn(tradeType);
	}


	@Override
	public String payCallback(TradeType tradeType,String pluginId) {
		IPaymentPlugin plugin = this.findPlugin(pluginId);
		String result  = plugin.onCallback(tradeType);
		return result;
	}
	
	
	
	
	private IPaymentPlugin findPlugin(String pluginid){
		for (IPaymentPlugin plugin : paymentPluginList) {
			 if(plugin.getPluginId().equals( pluginid)){
				   return plugin;
			 }
		}
		
		throw new RuntimeException("没有找到要应的支付方式["+pluginid+"]");
	}

	@Override
	public boolean returnOrder(RefundVo refund) {
		//查询该交易的支付方式
		String sql = "select payment_plugin_id,order_price from es_order where sn = ?";
		
		PayBill bill = this.daoSupport.queryForObject(sql,PayBill.class, refund.getOrder_sn());
		
		if(bill.getOrder_price()<refund.getRefund_price()){
			throw new UnProccessableServiceException(ErrorCode.ORDER_PAY_VALID_ERROR, "退款金额不能大于订单金额");
		}
		
		IPaymentPlugin plugin = this.findPlugin(bill.getPayment_plugin_id());
		
		RefundBill refundBill = new RefundBill();
		refundBill.setRefund_price(refund.getRefund_price());
		refundBill.setRefund_sn(refund.getSn());
		refundBill.setTrade_sn(refund.getPay_order_no());
		
		return plugin.returnPay(refundBill);
		
	}

	@Override
	public void queryRefundOrderStatus() {
		
		List<RefundPartVo> list = afterSaleManager.queryNoReturnOrder();
		if(list!=null && list.size()>0){
			for(RefundPartVo refund : list){
				IPaymentPlugin plugin = this.findPlugin(refund.getAccount_type());
				refund = plugin.queryRefundStatus(refund);
			}
			this.afterSaleManager.update(list);
		}
	}

}
