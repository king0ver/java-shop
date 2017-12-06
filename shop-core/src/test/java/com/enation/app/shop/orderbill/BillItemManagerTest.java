package com.enation.app.shop.orderbill;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.app.shop.orderbill.service.IBillItemManager;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.DateUtil;

@Rollback(false)
public class BillItemManagerTest extends SpringTestSupport{

	
	@Autowired
	private IBillItemManager billItemManager;
	
	@Test
	public void testAddOrder() {
		BillItem item = new BillItem(0);
		item.setOrder_sn("0006");
		item.setOrder_price(10.0);
		item.setDiscount_price(0.0);
		item.setSeller_id(15);
		item.setMember_id(16);
		item.setMember_name("food");
		item.setOrder_time(DateUtil.getDateline());
		item.setPayment_type("online");
		item.setShip_name("李白");
		billItemManager.add(item);
	}
	
	@Test
	public void testAddrefund() {
		BillItem item = new BillItem(1);
		item.setOrder_sn("0007");
		item.setOrder_price(1.00);
		item.setDiscount_price(0.0);
		item.setSeller_id(15);
		item.setMember_id(16);
		item.setMember_name("food");
		item.setOrder_time(DateUtil.getDateline());
		item.setPayment_type("online");
		item.setShip_name("李白");
		item.setRefund_sn("t0002");
		item.setRefund_id(1);
		item.setRefund_time(DateUtil.getDateline());
		billItemManager.add(item);
	}

}
