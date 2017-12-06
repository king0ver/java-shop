package com.enation.app.shop.trade.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.vo.OrderFlowDetail;
import com.enation.eop.processor.core.UrlNotFoundException;

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
public class SellerOrderDetailTag extends AbstractOrderDetailTag{
 
	
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Seller seller = sellerManager.getSeller();
		if(seller == null ){
			throw new TemplateModelException("未登陆不能使用此标签[SellerOrderDetailTag]");
		}
		
		OrderFlowDetail orderFlowDetail = this.getOrderAndFlowDetail();
		
		//卖家的权限判断
		if(!seller.getStore_id().equals(orderFlowDetail.getOrderDetail().getSeller_id())){
			throw new UrlNotFoundException();
		}
		
		return orderFlowDetail;
	}

	
}
