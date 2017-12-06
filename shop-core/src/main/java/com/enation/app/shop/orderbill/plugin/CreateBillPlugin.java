package com.enation.app.shop.orderbill.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import com.enation.app.base.core.plugin.job.IEveryMonthExecuteEvent;
import com.enation.app.shop.orderbill.service.IBillManager;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 
 * 店铺结算每月执行任务 
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年12月1日 上午12:28:44
 */
@AutoRegister
public class CreateBillPlugin extends AutoRegisterPlugin implements IEveryMonthExecuteEvent{
	
	@Autowired
	private IBillManager billManager;
	
	@Override
	public void everyMonth() {
		billManager.createBills();
	}

}
