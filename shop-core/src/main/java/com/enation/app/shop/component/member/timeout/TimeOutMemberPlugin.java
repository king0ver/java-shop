package com.enation.app.shop.component.member.timeout;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryMonthExecuteEvent;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;

/**
 * 
 * 会员登录次数每月清0 
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月19日 下午6:01:18
 */
@Component
public class TimeOutMemberPlugin extends AutoRegisterPlugin implements IEveryMonthExecuteEvent{
	private IDaoSupport daoSupport;

	@Override
	public void everyMonth() {
		String sql="UPDATE es_member SET logincount = 0";
		this.daoSupport.execute(sql);	
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
}
