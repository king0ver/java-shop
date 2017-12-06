package com.enation.app.shop.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.core.event.IRefundStatusChangeEvent;
import com.enation.app.shop.aftersale.support.RefundChangeMessage;
import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;
import com.enation.app.shop.goods.service.IGoodsQuantityManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 订单发货真实库存扣减
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月27日 上午10:18:20
 */
@Service
public class GoodsQuantityReduceConsumer implements IOrderStatusChangeEvent,IRefundStatusChangeEvent{

	@Autowired
	private IGoodsQuantityManager goodsQuantityManager;
	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {

		if (orderMessage.getNewStatus().name().equals(OrderStatus.SHIPPED.name())) {

			OrderPo order = orderMessage.getOrder();
			String item_json = order.getItems_json();
			Gson gson = new Gson();
			List<Product> list = gson.fromJson(item_json,  new TypeToken< List<Product> >() {  }.getType());
			for(Product sku : list){
				GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
				goodsQuantity.setEnable_quantity(0);
				goodsQuantity.setGoods_id(sku.getGoods_id());
				goodsQuantity.setQuantity(sku.getNum());
				goodsQuantity.setSku_id(sku.getProduct_id());
				goodsQuantityManager.reduceGoodsQuantity(goodsQuantity);
			}


		}else if (orderMessage.getNewStatus().name().equals(OrderStatus.CANCELLED.name())) {
			OrderPo order = orderMessage.getOrder();
			String item_json = order.getItems_json();
			Gson gson = new Gson();
			List<Product> list = gson.fromJson(item_json, new TypeToken<List<Product>>() {
			}.getType());
			for (Product sku : list) {
				GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
				goodsQuantity.setEnable_quantity(sku.getNum());
				goodsQuantity.setGoods_id(sku.getGoods_id());
				goodsQuantity.setQuantity(0);
				goodsQuantity.setSku_id(sku.getProduct_id());
				goodsQuantityManager.addGoodsQuantity(goodsQuantity);
			}
		}

	}
	/**
	 * 如果是已经付款的未发货的订单申请退款，在平台审核的时候需要将可用库存补充回去。
	 */
	@Override
	public void refund(RefundChangeMessage refundPartVo) {
		if (refundPartVo.getOperation_type().equals(RefundChangeMessage.ADMIN_AUTH)) {
			/** 获取订单信息，用来判断是否已经发货 */
			OrderDetail order = this.orderQueryManager.getOneBySn(refundPartVo.getRefund().getOrder_sn());
			/** 判断此订单是否已经发货，未发货的情况需要补充可用库存*/
			if(order.getShip_status().equals(ShipStatus.SHIP_NO.name())) {
				List<Product> product = order.getProductList();
				for (int i = 0; i < product.size(); i++) {
					GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
					goodsQuantity.setGoods_id(product.get(i).getGoods_id());
					goodsQuantity.setEnable_quantity(product.get(i).getNum());
					goodsQuantity.setQuantity(0);
					goodsQuantity.setSku_id(product.get(i).getProduct_id());
					this.goodsQuantityManager.addGoodsQuantity(goodsQuantity);
				}
			}

		}
	}

}
