package com.enation.app.shop.order.consumer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.po.TransactionRecord;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.ITransactionRecordManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 订单确认收货增加交易记录消费者
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月28日 上午11:36:27
 */
@Service
public class OrderRogTransactionConsumer implements IOrderStatusChangeEvent{

	@Autowired
	private ITransactionRecordManager transactionRecordManager;
	
	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		
		if(orderMessage.getNewStatus().equals(OrderStatus.ROG)){
			
			OrderPo order = orderMessage.getOrder();
			
			TransactionRecord record = new TransactionRecord();
			record.setOrder_sn(order.getSn());
			if(order.getMember_id()==null){
				record.setUname("游客");
				record.setMember_id(0);
			}else{
				record.setMember_id(order.getMember_id());
				record.setUname(order.getMember_name());
			}
			record.setRog_time(DateUtil.getDateline());
			String item_json = order.getItems_json();
			Gson gson = new Gson();
		    Type type = new TypeToken<ArrayList<Product>>() {}.getType();  
			List<Product> productList = gson.fromJson(item_json, type);
			
			for (Product orderItem : productList) {
				record.setPrice(orderItem.getPurchase_price());
				record.setGoods_num(orderItem.getNum());
				record.setGoods_id(orderItem.getGoods_id());
				
				transactionRecordManager.add(record);
			}
		}
	}
}
