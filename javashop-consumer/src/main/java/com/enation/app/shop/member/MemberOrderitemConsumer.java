package com.enation.app.shop.member;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.database.IDaoSupport;
/**
 * 
 * 生成会员购买商品信息消费者
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月26日 下午9:47:58
 */
@Component
public class MemberOrderitemConsumer implements IOrderStatusChangeEvent{
	@Autowired
	IDaoSupport daoSupport;
	
	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		if(orderMessage.getNewStatus().equals(OrderStatus.ROG)){
			String sql = "select * from es_order_items where order_sn=?";
			List<Map> list = daoSupport.queryForList(sql, orderMessage.getOrder().getSn());
			for (Map map : list) {
				this.daoSupport.execute(
								"INSERT INTO es_member_order_item(member_id,goods_id,order_id,item_id,commented,comment_time,product_id) VALUES(?,?,?,?,0,0,?)",
								orderMessage.getOrder().getMember_id(), 
								map.get("goods_id").toString(), 
								orderMessage.getOrder().getOrder_id(),  
								map.get("item_id").toString(),
								map.get("product_id").toString()); //添加product_id用于区分相同商品不同规格的情况

			} 
		}
	}
}
