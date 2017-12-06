package com.enation.app.shop.connect.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.connect.ConnectComponent;
import com.enation.app.shop.connect.model.ConnectType;
import com.enation.app.shop.connect.model.ConnectUser;
import com.enation.app.shop.connect.service.ConnectLogin;
import com.enation.app.shop.connect.service.IConnectMemberManager;
import com.enation.app.shop.connect.service.impl.QQConnectLogin;
import com.enation.app.shop.connect.service.impl.WechatConnectLogin;
import com.enation.app.shop.connect.service.impl.WechatMpConnectLogin;
import com.enation.app.shop.connect.service.impl.WeiboConnectLogin;
import com.enation.app.shop.connect.utils.DesUtils;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.eop.sdk.utils.ValidCodeServlet;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.EncryptUtil;
import com.enation.framework.util.HttpUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;



/**
 * Author: Dawei 
 * Datetime: 2016-02-27 18:17
 */
@Controller
@RequestMapping("/connect")
public class ConnectController {
	protected Logger logger = Logger.getLogger(getClass());
	@Autowired
	private IConnectMemberManager connectMemberManager;

	@Autowired
	private IMemberManager memberManager;
	
	@Autowired
	private ICartWriteManager cartWriteManager;

	@RequestMapping
	public ModelAndView execute(Integer type) throws IOException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		ConnectLogin connectLogin = getConnectionLogin(type);
		if (connectLogin == null) {
			return errorModelView("暂不支持此种登录方式!");
		}
		request.getSession().setAttribute("forward",
				request.getHeader("Referer"));
		return new ModelAndView("redirect:" + getConnectionLogin(type).getLoginUrl());
	}

	/**
	 * PC/Web QQ联合登录的回调方法
	 *
	 * @return
	 */
	@RequestMapping(value = "/qq")
	public ModelAndView qq() {
		return callback(ConnectType.QQ);
	}

	/**
	 * 微信联合登录的回调方法
	 *
	 * @return
	 */
	@RequestMapping(value = "/wechat")
	public ModelAndView wechat() {
		return callback(ConnectType.WECHAT);
	}

	/**
	 * 微信公众号登录的回调方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/wechatmp")
	public ModelAndView wechatmp() {
		return callback(ConnectType.WECHAT_MP);
	}

	/**
	 * 微博联合登录的回调方法
	 *
	 * @return
	 */
	@RequestMapping(value = "/weibo")
	public ModelAndView weibo() {
		return callback(ConnectType.WEIBO);
	}


	/**
	 * 联合登录成功后的回调处理逻辑
	 *
	 * @return
	 */
	private ModelAndView callback(int type) {
		ConnectUser connectUser = getConnectionLogin(type).loginCallback();
		if (connectUser == null) {
			return errorModelView("联合登录失败,请您重试!");
		}

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		// 已经绑定过,直接登录成功
		if (connectMemberManager.loginWithOpenId(connectUser) > 0) {
			String url = "";
			if (isMobile()) {
				url = "member.html";
			}else{
				url="member/member.html";
			}
			return new ModelAndView("redirect:../" + url);
		}
		
		Member member = UserConext.getCurrentMember();
		
		// 没有登录且没有绑定过,跳转到绑定页进行绑定
		if(member == null){
			if(isMobile()){
				ThreadContextHolder.getSession()
				.setAttribute(ConnectComponent.CONNECT_USER_SESSION_KEY, connectUser);			
				return new ModelAndView("redirect:../connectbind_wap.html");
			}else{
				ThreadContextHolder.getSession()
				.setAttribute(ConnectComponent.CONNECT_USER_SESSION_KEY, connectUser);			
				return new ModelAndView("redirect:../connectbind.html");
			}
			
		}
		
		//登录且没有绑定过，直接进行绑定
		if (!connectMemberManager.bind(member, connectUser)) {
			return errorModelView("绑定账号失败，请您重试！");
		}

		request.getSession().removeAttribute(ConnectComponent.CONNECT_USER_SESSION_KEY);
		if(isMobile()){
			return new ModelAndView("redirect:..//member.html");
		}else{
			return new ModelAndView("redirect:..//connectbind_success.html");
		}
		
		
	}

	/**
	 * 登录并绑定
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult login() {
		Object sessionObj = ThreadContextHolder.getSession().getAttribute(
				ConnectComponent.CONNECT_USER_SESSION_KEY);
		if (sessionObj == null) {
			return JsonResultUtil.getErrorJson("联合登录失败,请您重试!");
		}

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		ConnectUser connectUser = (ConnectUser) sessionObj;

		String validcode = request.getParameter("validcode");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
        
		if (this.validcode(validcode, "memberlogin") != 1) {
			return JsonResultUtil.getErrorJson("验证码错误!");
		}

		boolean result = this.memberManager.staticPwdLogin(username, password);
		if (!result) {
			return JsonResultUtil.getErrorJson("账号或密码错误!");
		}

		// 开始绑定
		Member member = memberManager.getMemberByUname(username);
		if (member == null) {
			return JsonResultUtil.getErrorJson("登录失败,请您重试!");
		}

		if (!connectMemberManager.bind(member, connectUser)) {
			return JsonResultUtil.getErrorJson("绑定失败,请您重试!");
		}

		request.getSession().removeAttribute(ConnectComponent.CONNECT_USER_SESSION_KEY);
		return JsonResultUtil.getSuccessJson("登录并绑定账号成功！");
	}

	/**
	 * 校验验证码
	 *
	 * @param validcode
	 * @param name
	 *            (1、memberlogin:会员登录 2、memberreg:会员注册)
	 * @return 1成功 0失败
	 */
	private int validcode(String validcode, String name) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String valid = request.getParameter("valid");
		if (valid != null && valid.equals("1")) {
			return 1;
		}
		if (validcode == null) {
			return 0;
		}

		String code = (String) ThreadContextHolder.getSession().getAttribute(
				ValidCodeServlet.SESSION_VALID_CODE + name);
		if (code == null) {
			return 0;
		} else {
			if (!code.equalsIgnoreCase(validcode)) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * 原生移动端 第三方账号绑定原有平台账号
	 * @param connectType 登录类型 1：QQ 2：Weibo 3：WeChat
	 * @param openid      第三方登录唯一标示ID
	 * @param username    用户名
	 * @param password    密码
	 * @author LDD
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/mloginbind", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult mobileConnectLoginAndBind(String connectType,String openid,String username,String password){
		if(StringUtil.isEmpty(connectType)||StringUtil.isEmpty(openid)||StringUtil.isEmpty(username)||StringUtil.isEmpty(password)){
			return JsonResultUtil.getErrorJson("绑定第三方账号失败！");
		}
		/*获取登录结果*/
		boolean result = this.memberManager.staticPwdLogin(username, password);
		
		/*判断登录结果*/
		if (!result) {
			return JsonResultUtil.getErrorJson("账号或密码错误!");
		}
		
		/*根据用户名从数据库拿出对应会员信息*/
		Member member = memberManager.getMemberByUname(username);
		
		/*判断库中是否存在该会员*/
		if (member == null) {
			return JsonResultUtil.getErrorJson("登录失败,请您重试!");
		}
		/*此处处理Des加密异常*/
		try {
			/*开始绑定第三方Uid于会员*/
			if (!connectMemberManager.bind(member, new ConnectUser(DesUtils.decode(openid),Integer.valueOf(connectType)))) {
				return JsonResultUtil.getErrorJson("绑定失败,请您重试!");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("绑定失败,请您重试!【openid解析异常】");
		}
		/*绑定完成，拼接会员信息*/
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", member.getUname());
		map.put("face", StaticResourcesUtil.convertToUrl(member.getFace()));
		map.put("level", member.getLvname());
        map.put("imuser", "");
        map.put("impass", "");
		return JsonResultUtil.getObjectMessageJson(map,"绑定第三方账号成功！");
	}
	
	/**
	 * 原生移动端 第三方登录接口
	 * @param connectType 登录类型 1：QQ 2：Weibo 3：WeChat
	 * @param openid      第三方登录唯一标示ID
	 * @author LDD
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/mlogin", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult mobileConnectLogin(String connectType,String openid){
		ConnectUser user;
		try {
			/*拼接第三方User对象，openid字段在移动端已经加密，所以需要解密使用，使用Des解密，并处理异常*/
			user = new ConnectUser(DesUtils.decode(openid),Integer.valueOf(connectType));
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("登录失败,请您重试!【openid解析异常】");
		}
		/*使用第三方uid进行登录*/
		if (connectMemberManager.loginWithOpenId(user) == 0) {
			return JsonResultUtil.getErrorJson("联合登录失败！");
		}else{
			/*登录成功 获取Session中的会员信息*/
			Member member = UserConext.getCurrentMember();
			
			//登录成功后合并购物车
			this.cartWriteManager.mergeCart();

			
			/*设置免登陆标识  此处两周以内免登陆*/
			String cookieValue = EncryptUtil.encryptCode("{username:\"" + member.getUname()
					+ "\",password:\"" + member.getPassword() + "\"}");
			HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(),
					"JavaShopUser", cookieValue, 60 * 60 * 24 * 14);
			/*拼接用户信息*/
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("username", member.getUname());
			map.put("face", StaticResourcesUtil.convertToUrl(member.getFace()));
			map.put("level", member.getLvname());
	        map.put("imuser", "");
	        map.put("impass", "");
			return JsonResultUtil.getObjectMessageJson(map,"登陆成功！");
		}
	}
	
	
	/**
	 * 原生移动端第三方登录 注册绑定
	 * @param connectType 登录类型 1：QQ 2：Weibo 3：WeChat
	 * @param openid      第三方登录唯一标示ID
	 * @param username    用户名
	 * @param password    密码
	 * @param nikename    昵称
	 * @param face        头像      
	 * @author LDD
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/mregister", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult mobileConnectRegister(String connectType,String openid,String username,String password,String nikename,String face){
		
		/*判断传参是否完整*/
		if (StringUtil.isEmpty(connectType)||StringUtil.isEmpty(openid)) {
			return JsonResultUtil.getErrorJson("联合登录失败,请您重试!");
		}
		
		/*获取Request 拿到ip*/
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();
		/*准备拼接第三方User*/
		ConnectUser connectUser;
		try {
			/*拼接第三方User对象，openid字段在移动端已经加密，所以需要解密使用，使用Des解密，并处理异常*/
			connectUser = new ConnectUser(DesUtils.decode(openid),Integer.valueOf(connectType));
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("绑定失败,请您重试!【openid解析异常】");
		}
		
		if (StringUtil.isEmpty(username)) {
			return JsonResultUtil.getErrorJson("用户名不能为空!");
		}
		
		if (username.length() < 4 || username.length() > 20) {
			return JsonResultUtil.getErrorJson("用户名的长度为4-20个字符！");
		}
		
		if (username.contains("@")) {
			return JsonResultUtil.getErrorJson("用户名中不能包含@等特殊字符！");
		}
		
		if (StringUtil.isEmpty(nikename)) {
			return JsonResultUtil.getErrorJson("昵称不能为空！");
		}
		
		if (StringUtil.isEmpty(password)) {
			return JsonResultUtil.getErrorJson("密码不能为空！");
		}
		
		if (!memberManager.checkUsername(username)) {
			return JsonResultUtil.getErrorJson("此用户名已经存在，请您选择另外的用户名!");
		}
        /*拼接注册Member对象*/
		Member member = new Member();
		member.setMobile("");
		member.setUname(username);
		member.setName(nikename);
		member.setNickname(nikename);
		member.setPassword(password);
		member.setEmail(username);
		member.setFace(face);
		member.setRegisterip(registerip);
		
		/*注册并获取结果！*/
		boolean result = memberManager.register(member);
		if (!result) {
			return JsonResultUtil.getErrorJson("注册失败,请您重试!");
		}
		
		/*根据用户名从库中拿到会员信息*/
		member = memberManager.getMemberByUname(username);
		if (member == null) {
			return JsonResultUtil.getErrorJson("注册失败,请您重试!");
		}
		
		/*对会员绑定第三方Uid*/
		if (!connectMemberManager.bind(member, connectUser)) {
			return JsonResultUtil.getErrorJson("绑定失败,请您重试!");
		}
		/*绑定完成后登录*/
		this.memberManager.staticPwdLogin(username, password);
		
		/*设置免登陆标识 二周内免登陆*/
		String cookieValue = EncryptUtil.encryptCode("{username:\"" + username
				+ "\",password:\"" + StringUtil.md5(password) + "\"}");
		HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(),
				"JavaShopUser", cookieValue, 60 * 60 * 24 * 14);
		
		/*拼接member信息*/
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", member.getUname());
		map.put("face", member.getFace());
		map.put("level", member.getLvname());
        map.put("imuser", "");
        map.put("impass", "");
        
        return JsonResultUtil.getObjectMessageJson(map,"登陆成功！");
	}
	
	/**
	 * 注册并绑定
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public String register() {
		Object sessionObj = ThreadContextHolder.getSession().getAttribute(
				ConnectComponent.CONNECT_USER_SESSION_KEY);
		if (sessionObj == null) {
			return "{\"result\":0,\"message\":\"联合登录失败,请您重试!\"}";
		}

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		ConnectUser connectUser = (ConnectUser) sessionObj;

		// 首先注册用户
		Member member = new Member();
		String registerip = request.getRemoteAddr();
        
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String mobile = request.getParameter("mobile");
		String password = request.getParameter("password");

		if (StringUtil.isEmpty(username)) {
			return "{\"result\":0,\"message\":\"用户名不能为空！\"}";
		}
		if (username.length() < 4 || username.length() > 20) {
			return "{\"result\":0,\"message\":\"用户名的长度为4-20个字符！\"}";
		}
		if (username.contains("@")) {
			return "{\"result\":0,\"message\":\"用户名中不能包含@等特殊字符！\"}";
		}
		if (StringUtil.isEmpty(email)) {
			return "{\"result\":0,\"message\":\"注册邮箱不能为空！\"}";
		}
		if (!StringUtil.validEmail(email)) {
			return "{\"result\":0,\"message\":\"注册邮箱格式不正确！\"}";
		}
		if (StringUtil.isEmpty(password)) {
			return "{\"result\":0,\"message\":\"密码不能为空！\"}";
		}
		if (!memberManager.checkUsername(username)) {
			return "{\"result\":0,\"message\":\"此用户名已经存在，请您选择另外的用户名!\"}";
		}
		if (!memberManager.checkEmail(email)) {
			return "{\"result\":0,\"message\":\"此邮箱已经注册过，请您选择另外的邮箱!\"}";
		}

		member.setMobile(mobile);
		member.setUname(username);
		member.setName(username);
		member.setPassword(password);
		member.setEmail(email);
		member.setRegisterip(registerip);

		boolean result = memberManager.register(member);
		if (!result) {
			return "{\"result\":0,\"message\":\"注册失败,请您重试!\"}";
		}

		// 绑定OpenId
		member = memberManager.getMemberByUname(username);
		if (member == null) {
			return "{\"result\":0,\"message\":\"注册失败,请您重试!\"}";
		}

		if (!connectMemberManager.bind(member, connectUser)) {
			return "{\"result\":0,\"message\":\"绑定失败,请您重试!\"}";
		}

		request.getSession().removeAttribute(ConnectComponent.CONNECT_USER_SESSION_KEY);
        
		this.memberManager.staticPwdLogin(username, password);
		String mailurl = "http://mail."
				+ StringUtils.split(member.getEmail(), "@")[1];
		return JsonMessageUtil.getStringJson("mailurl", mailurl);

	}

	/**
	 * 输出html到页面
	 * 
	 * @param html
	 */
	private void renderHtml(String html) {
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ModelAndView errorModelView(String msg){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/shop/admin/connect/error");
		modelAndView.addObject("msg", msg);
		return modelAndView;
	}

	/**
	 * 获取联合登录的处理对象
	 *
	 * @return
	 */
	private ConnectLogin getConnectionLogin(int type) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		switch (type) {
		case ConnectType.QQ:
			return new QQConnectLogin(request);
		case ConnectType.WECHAT:
			return new WechatConnectLogin(request);
		case ConnectType.WEIBO:
			return new WeiboConnectLogin(request);
		case ConnectType.WECHAT_MP:
			return new WechatMpConnectLogin(request);
		}
		return null;
	}

	/**
	 * 检测是不是手机访问
	 * 
	 * @return
	 */
	private static boolean isMobile() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		if (request == null)
			return false;
		String user_agent = request.getHeader("user-agent");
		if (StringUtil.isEmpty(user_agent))
			return false;

		String userAgent = user_agent.toLowerCase();

		if (userAgent.contains("android") || userAgent.contains("iphone")) {
			return true;
		}
		return false;
	}
	
}
