package com.enation.app.shop.aftersale.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.payment.model.po.PaymentMethod;
import com.enation.app.shop.payment.service.IPaymentMethodManager;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


/**
 * 获取是否需要愿支付方式退款信息
 * @author zjp
 * @version v6.5.0
 * @since v6.5.1
 * 2017年7月7日 上午11:16:59
 */
@Component
@Scope("prototype")
public class RefundWayTag extends BaseFreeMarkerTag {

	@Autowired
	private IOrderQueryManager orderQueryManager;
	@Autowired
	private IPaymentMethodManager paymentMethodManager;

	/**
	 * 
	 * 获取是否需要原支付方式退款
	 * 必须传递orderid
	 * @param orderid,订单id，int型
	 * @return 订单详细 ，Order型
	 * {@link Order}
	 */
	@Override
	public Object exec(Map args) throws TemplateModelException {

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ordersn  = request.getParameter("ordersn");
		/** 获取订单信息 */
		OrderDetail order = this.orderQueryManager.getOneBySn(ordersn);
		/** 根据订单支付方式获取支付方式详细信息 */
		PaymentMethod payMethod = null;
		if(order != null){
			payMethod = this.paymentMethodManager.getByPluginId(order.getPayment_plugin_id());
		}
		if(payMethod != null){
			return payMethod.getIs_retrace();
		}
		return 0;


	}

}
