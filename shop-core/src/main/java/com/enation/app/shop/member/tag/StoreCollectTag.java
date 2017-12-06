package com.enation.app.shop.member.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.member.service.IMemberCollectManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 收藏店铺标签
 * @author xulipeng
 *	2014年12月9日17:38:55
 */

@Component
@SuppressWarnings("unchecked")
public class StoreCollectTag extends BaseFreeMarkerTag {
	@Autowired
	private IMemberCollectManager memberCollectManager;
	@Autowired
	private ISellerManager sellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller Seller =sellerManager.getSeller() ;
		Integer memberid = Seller.getMember_id();
		Page webpage = this.memberCollectManager.getList(memberid,this.getPage(),this.getPageSize());
		
		Map result = new HashMap();
		//获取总记录数
		Long totalCount = webpage.getTotalCount();
		result.put("page", this.getPage());
		result.put("pageSize", this.getPageSize());
		result.put("totalCount", totalCount);
		result.put("storelist", webpage);
		return result;
	}
	
}
