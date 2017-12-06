package com.enation.app.shop.connect.tag;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.connect.ConnectComponent;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;

@Component
public class ConnectUserTag extends BaseFreeMarkerTag{

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		return ThreadContextHolder.getSession().getAttribute(ConnectComponent.CONNECT_USER_SESSION_KEY);
	}
}
