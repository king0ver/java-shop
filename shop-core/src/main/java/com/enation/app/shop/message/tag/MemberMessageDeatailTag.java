package com.enation.app.shop.message.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.message.service.IMemberMessageManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 
 * 会员站内消息详情标签
 * @author kanon
 * @version v1.0
 * @since v6.4.0
 * 2017年9月8日
 */
@Component
public class MemberMessageDeatailTag extends BaseFreeMarkerTag{

	@Autowired
	private IMemberMessageManager memberMessageManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		return 	memberMessageManager.getMessageDetail(StringUtil.toInt(params.get("messageId").toString(), true));
	}

}
