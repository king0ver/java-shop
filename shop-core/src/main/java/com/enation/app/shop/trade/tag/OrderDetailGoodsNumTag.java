package com.enation.app.shop.trade.tag;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.trade.model.po.OrderItem;
import com.enation.app.shop.trade.service.IOrderItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 通过订单ID，获得该订单下商品的个数
 * @author wanghongjun
 * 2015-04-15 15:05
 */

@Component
@Scope("prototype")
public class OrderDetailGoodsNumTag extends BaseFreeMarkerTag{
	@Autowired
	private IOrderItemManager orderItemQueryManager;
	
	@Override
	public Object exec(Map params) throws TemplateModelException {
		Object ordersn= params.get("ordersn");
		if(ordersn != null){
			String sn = ordersn.toString();
			List<OrderItem> items = this.orderItemQueryManager.queryByOrderSn(sn);
			return items.size();
		}
		return 0;
	}

	
	
	

}
