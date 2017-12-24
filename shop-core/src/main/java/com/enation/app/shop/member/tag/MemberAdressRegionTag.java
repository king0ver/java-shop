package com.enation.app.shop.member.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.member.service.IMemberAddressManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 查询默认会员地址
 * @author xulipeng
 * 2014年12月13日16:33:53
 *
 */
@Component
public class MemberAdressRegionTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IMemberAddressManager memberAddressManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Member member = UserConext.getCurrentMember();
		
		if(member!=null){
			return memberAddressManager.addressCount(member.getMember_id());
		}
		return 0;
	}
	

}
