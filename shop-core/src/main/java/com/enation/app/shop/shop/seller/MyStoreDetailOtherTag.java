package com.enation.app.shop.shop.seller;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.comments.service.IStoreMemberCommentManager;
import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
/**
 * 我的店铺其他信息Tag
 * @author LiFenLong
 *
 */
public class MyStoreDetailOtherTag extends BaseFreeMarkerTag {

	@Autowired
	private IStoreMemberCommentManager storeMemberCommentManager;
	@Autowired
	private ISellerManager sellerManager;
	@Autowired
	private IOrderQueryManager orderQueryManager;
	@Autowired
	private IGoodsQueryManager goodsQueryManager;

	
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller member=sellerManager.getSeller();
		Map result=new HashMap();
		//店铺仓库中待上架商品数量
		int notMarket=0;

		Integer count = goodsQueryManager.getWaitShelvesGoodsCount(member.getStore_id());
		if(count != 0){
			notMarket = count;
		}

		//店铺中出售中的商品数量
		int ingMarket=0;

		count = goodsQueryManager.getSellerGoodsCount(member.getStore_id());
		if(count != 0){
			ingMarket = count;
		}
		//卖家未处理得商品留言
		int notReply=storeMemberCommentManager.getCommentCount(2,member.getStore_id());
		/** 获取各种状态下的订单数量 */
		OrderQueryParam orderQueryParam = new OrderQueryParam();
		orderQueryParam.setSeller_id(member.getStore_id());
		Map<Object,Integer> map = orderQueryManager.getOrderNum(orderQueryParam);

		result.put("total", map.get("TOTAL"));
		result.put("orderConfirm", map.get(OrderStatus.CONFIRM.value()));
		result.put("orderPay", map.get(OrderStatus.PAID_OFF.value()));
		result.put("orderShip", map.get(OrderStatus.SHIPPED.value()));
		result.put("orderComplete",map.get(OrderStatus.COMPLETE.value()));
		result.put("orderSellback", map.get(OrderStatus.AFTE_SERVICE.value()));
		
		result.put("notMarket", notMarket);
		result.put("notReply", notReply);
		result.put("ingMarket", ingMarket);
		return result;
	}
	
	
	
}