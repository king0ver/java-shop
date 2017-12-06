package com.enation.eop.sdk.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enation.framework.util.JsonUtil;
import net.sf.json.JSONObject;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.springframework.web.servlet.HandlerInterceptor;

import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.util.JsonResultUtil;

/**
 * 
 * 官网演示站点不允许保存
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年10月19日 下午8:43:17
 */
public class DemoSiteIntercepter implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String servletPath = request.getServletPath();
		boolean flag = false;
		boolean hid = false;
		if(EopSetting.IS_DEMO_SITE) {
			AdminUser user  = UserConext.getCurrentAdminUser();
			if(servletPath.equals("/shop/admin/express/list.do") && user!= null && user.getFounder() != 1){
				hid = true;
			}
			if(servletPath.equals("/core/admin/sms/list.do") && user!= null && user.getFounder() != 1){
				hid = true;
			}	
			if(servletPath.equals("/core/admin/uploader/uploader-list.do") && user!= null && user.getFounder() != 1){
				hid = true;
			}
			//系统设置
			if(servletPath.equals("/core/admin/setting/save.do" ) && user!= null && user.getFounder() != 1){
				flag = true;
			}

			//smtp
			if(servletPath.equals("/core/admin/smtp/save-edit.do") && user!= null && user.getFounder() != 1){
				flag = true;
			}
			//电子面单的参数
			if(servletPath.equals("/shop/admin/waybill/save-edit.do") && user!= null && user.getFounder() != 1){
				flag = true;
			}
			//信任登陆
			if(servletPath.equals("/shop/admin/connect-setting/save.do") && user!= null && user.getFounder() != 1){
				flag = true;
			}
			//组件安装
			if(servletPath.equals("/core/admin/component/install.do") && user!= null && user.getFounder() != 1){
				flag = true;
			}
			//组件卸载
			if(servletPath.equals("/core/admin/component/un-install.do") && user!= null && user.getFounder() != 1){
				flag = true;
			}
			//导航栏
			if(servletPath.equals("/core/admin/site-menu/save.do") && user!= null && user.getFounder() != 1){
				flag = true;
			}
			//站内信修改
			if(servletPath.equals("/b2b2c/admin/message-template/save-edit.do") && user!= null && user.getFounder() != 1) {
				flag = true;
			}//站内信添加
			if(servletPath.equals("/b2b2c/admin/message-template/save-add.do") && user!= null && user.getFounder() != 1) {
				flag = true;
			}
			//静态页地址
			if(servletPath.equals("/shop/admin/page-create/save.do") && user!= null && user.getFounder() != 1) {
				flag = true;
			}
			if(hid) {
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print("<h1 style=\"\n" + 
						"    font-size: 16px;\n" + 
						"    font-weight: normal;\n" + 
						"    margin: 2rem auto;\n" + 
						"    width: fit-content;\n" + 
						"    color: #333;\n" + 
						"    border: 1px solid #aaa;\n" + 
						"    padding: 0.5rem 1rem;\n" + 
						"\">抱歉，当前为演示站点，不能查看这些示例数据！您可以咨询售前获得此部分功能的演示！</h1>");
				return false;
			}
			if(flag) {
				/* 设置格式为text/json */
				response.setContentType("application/json");
				/* 设置字符集为'UTF-8' */
				response.setCharacterEncoding("UTF-8");
				PrintWriter write = response.getWriter();
				String str = "{\"result\":0,\"message\":\""+EopSetting.DEMO_SITE_TIP+"\"}";
				JSONObject jsonObject = JSONObject.fromObject(str);
				write.write(jsonObject.toString());
				write.flush();
				write.close();
				return false;
			}
		}
		return true;
	}
}
