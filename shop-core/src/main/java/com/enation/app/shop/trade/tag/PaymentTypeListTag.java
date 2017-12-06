package com.enation.app.shop.trade.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 支付类型列表标签
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月8日 下午7:25:20
 */
@Component
@Scope("prototype")
public class PaymentTypeListTag extends BaseFreeMarkerTag{

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		PaymentType[] values = PaymentType.values();
		List<Map> list = new ArrayList<>();
		
		for(PaymentType paymentType : values){
			Map map = new HashMap<>();
			map.put("name", paymentType.description());
			map.put("value", paymentType.value());
			list.add(map);
		}
		
		return list;
	}

}
