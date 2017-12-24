package com.enation.app.shop.log;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.log.ILogCreator;

/**
 * 多店日志创建实现
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月13日 上午10:04:15
 */
@Component
public class B2b2cLogCreator implements ILogCreator{

	@Autowired
	private ISellerManager storeMemberManager;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.framework.log.ILogCreator#createLog()
	 */
	@Override
	public Map createLog() {
		Seller seller = storeMemberManager.getSeller();
		AdminUser adminUser = UserConext.getCurrentAdminUser();
		Map map = new HashMap();
		if(seller != null){
			map.put("member_name", seller.getUname());
			map.put("member_id", seller.getMember_id());
			map.put("store_id", seller.getStore_id());
		}
		map.put("admin_user", adminUser);
		return map;
	}

}
