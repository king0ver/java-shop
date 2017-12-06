package com.enation.app.shop.trade.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.OrderFlow;
import com.enation.app.shop.trade.model.vo.OrderFlowDetail;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

/**
 * 订单详情抽象接口
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月29日 上午10:04:18
 */
public abstract class AbstractOrderDetailTag extends BaseFreeMarkerTag{

	@Autowired
	private IOrderQueryManager orderQueryManager;
	
	public OrderFlowDetail  getOrderAndFlowDetail(){
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ordersn =  request.getParameter("ordersn");
		
		OrderDetail order  =null;
		if(StringUtil.isEmpty(ordersn) ){
			throw new UrlNotFoundException();
		}
		
		order = this.orderQueryManager.getOneBySn(ordersn);
		 
		if(order==null){
			throw new UrlNotFoundException();
		}
		
		OrderFlowDetail  orderFlowDetail = new OrderFlowDetail();
		orderFlowDetail.setOrderDetail(order);
		//创建流程
		List<OrderFlow> flowList = this.getOrderFlowList(order.getOrder_status(),order.getPayment_type());
		orderFlowDetail.setOrderFlowList(flowList);
		
		return orderFlowDetail;
	}
	
	/**
	 * 获取订单流程状态集合
	 * @param order_status 订单状态
	 * @param payment_type 支付方式   cod  货到付款
	 * @return
	 */
	private List<OrderFlow> getOrderFlowList(String order_status,String payment_type) {
		
		OrderFlow orderCreateFlow = new OrderFlow(OrderStatus.NEW);//新订单
		OrderFlow orderConfirmFlow = new OrderFlow(OrderStatus.CONFIRM);//订单确认
		OrderFlow ordePaidoffFlow = new OrderFlow(OrderStatus.PAID_OFF);//订单已付款
		OrderFlow ordeShippedFlow = new OrderFlow(OrderStatus.SHIPPED);//已发货
		OrderFlow ordeRogFlow = new OrderFlow(OrderStatus.ROG);//已收货
		OrderFlow ordeCompleteFlow = new OrderFlow(OrderStatus.COMPLETE);//已完成
		OrderFlow ordeAfteserviceFlow = new OrderFlow(OrderStatus.AFTE_SERVICE);//售后中
		OrderFlow ordeCancelledFlow = new OrderFlow(OrderStatus.CANCELLED);//已取消
		
		
		List<OrderFlow> resultFlow = new ArrayList<>();
		resultFlow.add(orderCreateFlow);
		resultFlow.add(orderConfirmFlow);
		//订单取消
		if(OrderStatus.CANCELLED.value().equals(order_status)){
			resultFlow.add(ordeCancelledFlow);
		}else if(PaymentType.cod.value().equals(payment_type)){//货到付款
			resultFlow.add(ordeShippedFlow);
			resultFlow.add(ordeRogFlow);
			resultFlow.add(ordePaidoffFlow);
			resultFlow.add(ordeCompleteFlow);
		}else{//款到发货
			resultFlow.add(ordePaidoffFlow);
			resultFlow.add(ordeShippedFlow);
			resultFlow.add(ordeRogFlow);
			resultFlow.add(ordeCompleteFlow);
		}
		boolean flowfinish = true;
		int i = 1;
		for(OrderFlow flow : resultFlow){//0 灰色    1 普通显示     2   结束显示   3 取消显示 
			
			if(flowfinish){
				if(flow.getOrder_status().equals(OrderStatus.CANCELLED.value())){
					flow.setShow_status(3);
				}else{
					if(i == resultFlow.size()){
						flow.setShow_status(2);
					}else{
						flow.setShow_status(1);
					}
				}
			}
			i++;
			if(flow.getOrder_status().equals(order_status)){
				flowfinish = false;
				break;
			}
			
		}
		
		return resultFlow;
	}
}
