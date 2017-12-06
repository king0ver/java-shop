package com.enation.app.shop.member.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;
@Component
/**
 * 判断会员是否登陆标签
 * @author LiFenLong
 *
 */
public class StoreMemberIsLoginTag extends BaseFreeMarkerTag{
	@Autowired
	private ISellerManager sellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Seller member=sellerManager.getSeller();
		
		boolean isLogin = false;
		if(member!=null){
			isLogin = true;
		}else{
			if("no".equals(params.get("redirect"))){
				return isLogin;
			}
			//没有登录则跳转到登陆页面
			HttpServletRequest request   = ThreadContextHolder.getHttpRequest();
			HttpServletResponse response = ThreadContextHolder.getHttpResponse();
			String curr_url = RequestUtil.getRequestUrl(request);
			String loginUrl=request.getContextPath()+"/store/login.html?forward="+curr_url;
			try {
				if(!curr_url.equals(request.getContextPath()+"/")&&!curr_url.equals(request.getContextPath()+"/store/index.html")){
					response.sendRedirect(loginUrl);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isLogin;
	}
	
}
