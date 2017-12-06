package com.enation.app.shop.payment.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.payment.model.vo.PaymentMethodVo;
import com.enation.app.shop.payment.service.IPaymentMethodManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 
 * wap使用的支付方式列表标签
 * @author jianghongyan
 * @version v1.0
 * @since v6.2
 * 2017年1月10日 下午10:50:21
 */
@Component
@Scope("prototype")
public class WapPaymentListTag extends BaseFreeMarkerTag{
	
	@Autowired
	private IPaymentMethodManager paymentMethodManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		String client_type = (String) params.get("client_type");
		List<PaymentMethodVo> list = this.paymentMethodManager.list(ClientType.valueOf(client_type));
		
		return list;
	}
	// 检测是不是手机访问
	private static boolean isMobile() {

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		if (request == null)
			return false;
		String user_agent = request.getHeader("user-agent");
		if (StringUtil.isEmpty(user_agent))
			return false;

		String userAgent = user_agent.toLowerCase();

		if (userAgent.contains("android") || userAgent.contains("iphone")) {
			return true;
		}
		return false;

	}
}
