package com.enation.app.shop.comments.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.comments.service.IMemberCommentManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取待评论的数量
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年11月29日 下午7:16:40
 */
@Component
@Scope("prototype")
public class MemberWaitCommentCountTag extends BaseFreeMarkerTag{

	@Autowired
	private IMemberCommentManager memberCommentManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		return memberCommentManager.getWaitCommentCount();
	}

}
