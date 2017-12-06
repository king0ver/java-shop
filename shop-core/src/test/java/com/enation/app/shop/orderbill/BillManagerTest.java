package com.enation.app.shop.orderbill;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.enation.app.shop.orderbill.service.IBillManager;
import com.enation.framework.test.SpringTestSupport;

@Rollback(false)
public class BillManagerTest extends SpringTestSupport{

	
	@Autowired
	private IBillManager billManager;
	
	@Test
	public void test() {
		this.billManager.createBills();
	}

}
