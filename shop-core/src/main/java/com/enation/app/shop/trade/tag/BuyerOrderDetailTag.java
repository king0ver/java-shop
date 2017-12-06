package com.enation.app.shop.trade.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.trade.model.vo.OperateAllowable;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.OrderFlowDetail;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.context.UserConext;

import freemarker.template.TemplateModelException;

/**
 * 商家的订单详细标签
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月23日 下午2:25:30
 */
@Component
@Scope("prototype")
public class BuyerOrderDetailTag extends AbstractOrderDetailTag{
 
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Member member = UserConext.getCurrentMember();
		if(member == null ){
			throw new TemplateModelException("未登陆不能使用此标签[BuyerOrderDetailTag]");
		}
		
		OrderFlowDetail orderFlowDetail = this.getOrderAndFlowDetail();
		
		OrderDetail orderDetail = orderFlowDetail.getOrderDetail();
		//货到付款的订单顾客是不能支付的
		if (PaymentType.cod.value().equals( orderDetail.getPayment_type())) {
			OperateAllowable allowable = orderDetail.getOperateAllowable();
			allowable.setAllowPay(false);
		}
		
		//买家的权限判断
		if(!member.getMember_id().equals(orderDetail.getMember_id())){
			throw new UrlNotFoundException();
		}
		
		return orderFlowDetail;
	}

}
