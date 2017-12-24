package com.enation.app.shop.message.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.message.service.IMemberMessageManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 会员消息列表标签
 * @author Kanon
 * @since v6.4
 * @version v1.0
 * 2017-8-16
 */
@Component
public class MemberMessageListTag extends BaseFreeMarkerTag {
	
	
	@Autowired
	private IMemberMessageManager memberMessageManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Member member = UserConext.getCurrentMember();
		if(member==null){
			return new UrlNotFoundException();
		}
		
		return memberMessageManager.getMemberMessageList(this.getPage(), this.getPageSize(), member.getMember_id());
	}

}
