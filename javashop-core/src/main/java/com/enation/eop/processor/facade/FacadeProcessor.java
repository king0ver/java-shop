package com.enation.eop.processor.facade;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.enation.eop.IEopProcessor;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * 前台模板处理器<br>
 * docs目录 由文档解析器处理
 * 其它目录由模板处理器处理
 * @author kingapex
 *2015-3-13
 */
public class FacadeProcessor implements IEopProcessor{

	public boolean process()throws IOException, ServletException{
		HttpServletRequest httpRequest =ThreadContextHolder.getHttpRequest();

		String uri =httpRequest.getServletPath();
		ThreadContextHolder.getHttpResponse().setContentType("text/html;charset=UTF-8");
		SsoProcessor processor = new SsoProcessor();
		processor.parse(); 

		if(uri.startsWith("/docs")){
			DocsPageParser docsPageParser = new DocsPageParser();
			docsPageParser.parse(uri);
			return true;
		}
		FacadePageParser parser = new FacadePageParser();
		return parser.parse(uri);

	}

}
