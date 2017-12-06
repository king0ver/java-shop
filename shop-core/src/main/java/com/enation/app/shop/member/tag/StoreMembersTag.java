package com.enation.app.shop.member.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取本店铺人员标签
 * @author fk
 * @version v1.0
 * @since v6.2
 * 2016年12月13日 下午3:09:18
 */
@Component
public class StoreMembersTag extends BaseFreeMarkerTag{

	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Seller storeMember = sellerManager.getSeller();
		List list =  sellerManager.getMyStoreMembers(storeMember.getStore_id());
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("memberlist", list);
		return result;
	}

}
