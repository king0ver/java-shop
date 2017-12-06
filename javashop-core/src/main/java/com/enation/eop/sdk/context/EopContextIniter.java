package com.enation.eop.sdk.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enation.eop.SystemSetting;
import com.enation.eop.processor.SafeHttpRequestWrapper;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.eop.processor.session.RequestEventSubject;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * Eop上下文初始化
 * @author kingapex
 *
 */
public class EopContextIniter {

	public static void init(HttpServletRequest httpRequest, HttpServletResponse httpResponse, RequestEventSubject requestEventSubject) {
		FreeMarkerPaser.set(new FreeMarkerPaser());
		FreeMarkerPaser fmp = FreeMarkerPaser.getInstance();

		SafeHttpRequestWrapper safeHttpRequest = new SafeHttpRequestWrapper(httpRequest, httpResponse, requestEventSubject);

		ThreadContextHolder.setSession(httpRequest.getSession());
		ThreadContextHolder.setHttpRequest(safeHttpRequest);
		ThreadContextHolder.setHttpResponse(httpResponse);
		httpRequest.setAttribute("staticserver", SystemSetting.getStatic_server_domain());
		EopContext context = new EopContext();

		String servletPath = httpRequest.getServletPath();

		if (servletPath.startsWith("/statics"))
			return;

		if( servletPath.startsWith("/install") ){

		}else{

			fmp.putData("site", EopSite.getInstance() );
		}
		EopContext.setContext(context);

		/**
		 * 设置freemarker的相关常量
		 */
		fmp.putData("ctx", httpRequest.getContextPath());
	}


}
