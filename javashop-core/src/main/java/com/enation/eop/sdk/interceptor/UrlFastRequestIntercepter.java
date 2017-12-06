package com.enation.eop.sdk.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.enation.eop.SystemSetting;
import com.enation.framework.cache.ICache;

/**
 * 防刷新机制，同一客户端同一url在规定时间不能调用多次
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月26日 上午11:01:47
 */
public class UrlFastRequestIntercepter implements HandlerInterceptor{

	@Autowired
	private ICache cache;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(SystemSetting.getTest_mode() == 1){//测试模式
			return true;
		}
		String servletPath = request.getServletPath();
		if(servletPath.equals("/shop/admin/region/children.do")){
			return true;
		}
		if(servletPath.equals("/shop/admin/comments/list-json-by-member.do")){
			return true;
		}
		if(servletPath.equals("/shop/admin/region/list-children.do")){
			return true;
		}
		if(servletPath.equals("/api/base/region/get-children.do")){
			return true;
		}
		if(servletPath.equals("/cms/admin/floor/mobile/design.do")){
			return true;
		}
		if(servletPath.equals("/api/base/upload-image/upload-image.do")){
			return true;
		}
		if(servletPath.equals("/api/base/upload-image/upload-img.do")){
			return true;
		}
		if(servletPath.equals("/core/upload.do")){
			return true;
		}
		if(servletPath.equals("/api/shop/order-create/trade/price.do")){
			return true;
		}
		if(servletPath.equals("/api/shop/order-create/cart/total.do")){
			return true;
		}
		if(servletPath.equals("/core/admin/site-menu/get-list-by-parentid-json.do")){
			return true;
		}
		if(servletPath.startsWith("/shop/goodslist/")){//TODO 放行/shop/goodslist/" + id + ".do，应该要写正则
			return true;
		}
		if(servletPath.startsWith("/api/shop/order-create/cart/product/")){//TODO 放行/api/shop/order-create/cart/product/156.do，应该要写正则
			return true;
		}
		if(servletPath.equals("/api/shop/point/get-point.do")){
			return true;
		}
		if(servletPath.startsWith("/api/mobile/order-create/")){
			return true;
		}

		String ip = request.getRemoteAddr();
		Object time = cache.get(ip+servletPath);
		if(time!=null){
			long intervalTime = System.currentTimeMillis()-(Long)time;
			if(intervalTime<200){//规定时间内点击了多次
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print("{\"error\":\"您的访问过于频繁，请稍后重试...\"}");
				return false;
			}
		}
		cache.put(ip+servletPath, System.currentTimeMillis(),30);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
}
