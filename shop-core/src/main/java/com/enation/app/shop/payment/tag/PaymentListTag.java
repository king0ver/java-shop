package com.enation.app.shop.payment.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.payment.model.vo.PaymentMethodVo;
import com.enation.app.shop.payment.service.IPaymentMethodManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 读取支付方式
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月29日 下午4:41:07
 */
@Component
@Scope("prototype")
public class PaymentListTag extends BaseFreeMarkerTag {

	@Autowired
	private IPaymentMethodManager paymentMethodManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		String client_type = (String) params.get("client_type");
		List<PaymentMethodVo> list = this.paymentMethodManager.list(ClientType.valueOf(client_type));
		
		return list;
	}

}
