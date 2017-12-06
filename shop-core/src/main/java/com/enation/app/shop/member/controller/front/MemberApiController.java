package com.enation.app.shop.member.controller.front;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.upload.UploadFactory;
import com.enation.app.base.upload.plugin.IUploader;
import com.enation.app.shop.member.model.enums.MemberSendCodeEnum;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.member.service.IMemberPointManger;
import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.model.enums.MessageCodeEnum;
import com.enation.app.shop.message.model.enums.MessageStatusEnum;
import com.enation.app.shop.message.service.IEmailSendlManager;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.app.shop.message.util.SmsUtil;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.ValidCodeServlet;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.EncryptUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.HttpUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.RequestUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.util.TestUtil;
import com.enation.framework.util.Validator;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
/**
 * 会员api
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年9月29日 下午7:48:23
 */
@RestController
@RequestMapping("/api/shop/member")
@Scope("prototype")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MemberApiController  {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberApiController.class);
	// 短信验证码session前缀
	private static final String SMS_CODE_PREFIX = "es_sms_";
	@Autowired
	private IMemberManager memberManager;
	@Autowired	
	private EmailProducer mailMessageProducer;
	@Autowired
	private IMemberPointManger memberPointManger;
	@Autowired
	private UploadFactory uploadFactory;
	@Autowired
	private IMessageManager messageManager;
	@Autowired
	private IEmailSendlManager emailSendManager;
	@Autowired
	private ICartWriteManager cartWriteManager;
	@Autowired
	private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	
	/**
	 * 会员静态登陆
	 * @param username 用户名
	 * @param password 密码
	 * @param validcode 验证码
	 * @param remember 两周内免登录
	 * @return 登陆的结果
	 */
	@ApiOperation(value="会员静态登陆" )
	@PostMapping(value="/staticPwd-login", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult staticPwdLogin( String username, String password, String validcode,String remember) {
			/** 静态登陆 */
			boolean isPass = memberManager.checkCode(validcode);
			if(!isPass) {
				return JsonResultUtil.getErrorJson("验证码错误");
			}
			boolean isLogin = memberManager.staticPwdLogin(username, password);
			if(!isLogin) {
				return JsonResultUtil.getErrorJson("账号密码错误");
			}else{
				//登录成功后合并购物车
				this.cartWriteManager.mergeCart();

				/** 两周内免登录 */
				if (remember != null && remember.equals("1")) {
					String cookieValue = EncryptUtil.encryptCode("{username:\"" + username + "\",password:\"" + StringUtil.md5(password) + "\"}");
					HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(), "JavaShopUser", cookieValue, 60 * 24 * 14);	
				}
		  }
			return JsonResultUtil.getSuccessJson("登陆成功");
	}
	
	
	/**
	 * 发送短信获取手机验证码
	 * @param mobile 手机
	 * @param key key值
	 * @return
	 */
	@ApiOperation(value="发送短信获取手机验证码" )
	@GetMapping(value="/send-sms-code", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult sendSmsCode(String mobile ,String key,Integer isCheckRegister){
	try {
		if(StringUtil.isEmpty(mobile)) {
			return JsonResultUtil.getErrorJson("手机号码不能为空");
		}
		if(!Validator.isMobile(mobile)) {
			return JsonResultUtil.getErrorJson("手机号码格式不正确");
		}
		if(key.equals(MemberSendCodeEnum.REGISTERMOBILE.name())){
			if(!memberManager.checkMobile(mobile)) {
				return JsonResultUtil.getErrorJson("该手机号码已注册会员");
			}
		}
		/** 如果没有值 就是0*/
		if (isCheckRegister == null) {
			isCheckRegister = 0;
		}
		/** 发送短信 */
		Map<String, Object> sendResult = memberManager.sendSmsCode(mobile, key,isCheckRegister);
		int stateCode = Integer.parseInt(sendResult.get("state_code").toString());
		/** 根据状态码分别返回信息 */
		switch (stateCode) {
			case 0:
				return JsonResultUtil.getErrorJson(sendResult.get("msg").toString());
			case 1:
				return JsonResultUtil.getSuccessJson(sendResult.get("msg").toString());
			case 2:
				return JsonResultUtil.getErrorJson(sendResult.get("msg").toString());
			default:
				return JsonResultUtil.getSuccessJson(sendResult.get("msg").toString());
		}
	}catch(RuntimeException e) {
		TestUtil.print(e);
		LOGGER.debug("发送短信验证码出错", e);
		return JsonResultUtil.getErrorJson(e.getMessage());
	}
}
	
	
	/**
	 * 验证注册的手机验证码
	 * @param mobile 手机
	 * @param code 验证码
	 * @return 返回验证结果
	 */
	@ApiOperation(value="验证注册的手机验证码" )
	@PostMapping(value="/valiRegist-code", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult valiRegistCode(String mobile, String code){
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			if (this.memberManager.checkSms(mobile, code,MemberSendCodeEnum.REGISTERMOBILE.name())) {
				request.getSession().setAttribute("account_info", mobile);

				return JsonResultUtil.getSuccessJson("验证成功");
			} else {
				return JsonResultUtil.getErrorJson("验证失败:验证码错误");
			}
		} catch(RuntimeException e) {
			TestUtil.print(e);
			LOGGER.debug(e.getMessage());
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}
	
	/**
	 * 手机注册
	 * @param password 密码
	 * @param license 是否同意注册
	 * @param username 用户名
	 * @param mobile 手机
	 * @return
	 */
	@ApiOperation(value="手机注册" )
	@PostMapping(value="/mobile-regist", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult mobileRegist(String password, String license, String username, String mobile) {

		if (!"agree".equals(license)) {
			return JsonResultUtil.getErrorJson("同意注册协议才可以注册!");							
		}
		if (StringUtil.isEmpty(password)) {
			return JsonResultUtil.getErrorJson("密码不能为空！");							
		}
			Member member = new Member();
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String registerip = request.getRemoteAddr();
			member.setMobile(mobile);
			member.setUname(username);
			member.setName(username);       
			member.setPassword(password);
			member.setRegisterip(registerip);

			boolean result = memberManager.register(member);
			if (result) { 
				/** 注册成功以后将session信息清楚 */
				ThreadContextHolder.getSession().removeAttribute("account_info");
				boolean staticPwdLogin = memberManager.staticPwdLogin(username, password);
				if(staticPwdLogin) {
					//登录成功后合并购物车
					this.cartWriteManager.mergeCart();
				}
				return JsonResultUtil.getSuccessJson("注册成功");
			} else {
				return JsonResultUtil.getErrorJson("用户名[" + member.getUname() + "]已存在!");				
			}
	}
	
	/**
	 * 检测用户名是否重复
	 * @param username 用户名
	 * @return
	 */
	@ApiOperation(value="检测用户名是否重复" )
	@PostMapping(value="/check-username", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkUsername(String username){
		if (!memberManager.checkUsername(username)) {
			return JsonResultUtil.getErrorJson("此用户名已经存在，请您选择另外的用户名!");
		}
		return JsonResultUtil.getSuccessJson("该用户名不重复");
	}
	
	/**
	 * 检测邮箱是否重复
	 * @param email 邮箱
	 * @return
	 */
	@ApiOperation(value="检测邮箱是否重复" )
	@PostMapping(value="/check-email", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkEmail(String email) {
		if (!memberManager.checkEmail(email)) {
			return JsonResultUtil.getErrorJson("此邮箱已经注册过，请您选择另外的邮箱!");
		}
		return JsonResultUtil.getSuccessJson("该邮箱不重复");
	}
	
	
	/**
	 * 邮箱注册
	 * @param license 是否同意注册协议
	 * @param email 邮箱
	 * @param username 用户名
	 * @param password 密码
	 * @param validcode 验证码
	 * @return
	 */
	@ApiOperation(value="邮箱注册" )
	@ApiImplicitParams({
        @ApiImplicitParam(name = "validcode", value = "验证码", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "license", value = "是否同意协议", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "username", value = "昵称", required = true, dataType = "String" ,paramType="query")
	 })
	@ResponseBody
	@PostMapping(value="/email-regist", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult emailRegist(String validcode,String license, String email, String username, String password) {
		if (!"agree".equals(license)) {
			return JsonResultUtil.getErrorJson("同意注册协议才可以注册!");				
		}
		if (StringUtil.isEmpty(email)) {
			return JsonResultUtil.getErrorJson("注册邮箱不能为空！");				
		}
		if (!StringUtil.validEmail(email)) {
			return JsonResultUtil.getErrorJson("注册邮箱格式不正确！");
		}
		if (StringUtil.isEmpty(username)) {
			return JsonResultUtil.getErrorJson("用户名不能为空！");				
		}
		if (username.length() < 4 || username.length() > 20) {
			return JsonResultUtil.getErrorJson("用户名的长度为4-20个字符！");				
		}
		if (username.contains("@")) {
			return JsonResultUtil.getErrorJson("用户名中不能包含@等特殊字符！");				
		}
		if (StringUtil.isEmpty(password)) {
			return JsonResultUtil.getErrorJson("密码不能为空！");
		}
		if (this.validcode(validcode,"memberreg") == 0) {
			return JsonResultUtil.getErrorJson("验证码输入错误!");				
		}
		
		//校验邮箱是否重复
		if (!memberManager.checkEmail(email)) {
			return JsonResultUtil.getErrorJson("此邮箱已经注册过，请您选择另外的邮箱!");
		}
		
		//校验用户名是否重复
		if (!memberManager.checkUsername(username)) {
			return JsonResultUtil.getErrorJson("此用户名已经存在，请您选择另外的用户名!");
		}
		
		
		Member member = new Member();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();
		member.setUname(username);
		member.setName(username);    
		member.setPassword(password);
		member.setEmail(email);
		member.setRegisterip(registerip);
		/** 注册 */
		boolean result = memberManager.register(member);
		if (result) {
			/** 调用发送邮件的amqp接口 */
			boolean staticPwdLogin = memberManager.staticPwdLogin(username, password);
			if(staticPwdLogin) {
				//登录成功后合并购物车
				this.cartWriteManager.mergeCart();
			}
			return JsonResultUtil.getObjectJson("注册成功");

		} else {
			//这里需要优化
			return JsonResultUtil.getErrorJson("注册失败，验证不通过");				
		}
	}
	
	
	/**
	 * 验证手机号有没有注册
	 * @param mobile 手机号
	 * @exception RuntimeException 手机号格式错误出错
	 * @return boolean false=没有注册 true=注册了
	 */
	public boolean validMobileIsRegister(String mobile){
		/** 如果手机号格式不对 */
		if ( !Validator.isMobile(mobile) ) {
			throw new RuntimeException("手机号码格式错误");
		}
		boolean isExists = memberManager.checkMobile(mobile);
		return isExists;
	}
	
	
	/**
	 * 动态登陆
	 * 新版登录逻辑，验证手机号成功以后若已注册则登录，若没登录跳转到填充资料页面
	 * @param mobile 手机号
	 * @param validcode 验证码
	 * @return 
	 */
	@ApiOperation(value="验证手机号成功以后若已注册则登录，若没登录跳转到填充资料页面" )
	@PostMapping(value="/dynamic-login", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult dynamicLogin(String validcode,String remember,String mobile) {
		try {
			boolean checkResult = memberManager.checkSms(mobile, validcode, MemberSendCodeEnum.CHECKMOBILE.name());
			/** 验证结果 */
			if (checkResult) {
				boolean isRegister = memberManager.validMobileIsRegister(mobile);

				Map<String, Object> result = new HashMap<String, Object>();
				/** 如果注册了 */
				if (!isRegister) {
					/** 手机登陆不需要密码 */
					boolean isLogin = memberManager.dynamicLogin(mobile);
					if(!isLogin) {
						return JsonResultUtil.getErrorJson("账号密码错误");
					}else {
						Member member = memberManager.getMemberByPhone(mobile);
						/** 两周内免登录 */
						if (remember != null && remember.equals("1")) {
							String cookieValue = EncryptUtil.encryptCode(
									"{username:\"" + member.getUname() + "\",password:\"" + StringUtil.md5(member.getPassword()) + "\"}");
							HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(), "JavaShopUser", cookieValue, 60 * 60 * 24 * 14);
						}
						ThreadContextHolder.getSession().removeAttribute(SMS_CODE_PREFIX + MemberSendCodeEnum.LOGINMOBILE.name() + mobile);
					}
					result.put("check_type", MemberSendCodeEnum.LOGINMOBILE.name());
				/** 如果没注册,加密账户信息，返回给前端跳转到填充信息页面 */
				} else {
					HttpServletRequest request = ThreadContextHolder.getHttpRequest();
					request.getSession().setAttribute("account_info", mobile);
					result.put("check_type", MemberSendCodeEnum.REGISTERMOBILE.name());
				}
				return JsonResultUtil.getObjectJson(result);
			} else {
				return JsonResultUtil.getErrorJson("验证码错误");
			}
		} catch(RuntimeException e) {
			TestUtil.print(e);
			LOGGER.debug(e.getMessage());
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}

	
	


	/**
	 * 判断当前用户是否登录
	 * @return
	 */
	@ApiOperation(value="判断当前用户是否登录" )
	@GetMapping(value="/is-login", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult isLogin() {
		Member member = UserConext.getCurrentMember();
		Map<String, Object> result = new HashMap<String, Object>();
		if (member != null) {

			result.put("state", 1);
			result.put("msg", "已经登录");
		} else {
			result.put("state", 0);
			result.put("msg", "未登录");
		}

		return JsonResultUtil.getObjectJson(result);
	}


	/**
	 * 注销会员登录
	 * @param
	 * @return json字串
	 * result  为1表示注销成功，0表示失败 ，int型
	 * message 为提示信息 ，String型
	 */
	@ApiOperation(value="注销会员登录" )
	@GetMapping(value="/logout", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult logout() {
		try {
			HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(), "JavaShopUser", null, 0);
			this.memberManager.logout();
		} catch (Exception e) {
			LOGGER.error("会员注销时发生异常", e);
		}
		return JsonResultUtil.getSuccessJson("注销成功");
	}

	/**
	 * 修改会员密码
	 * @param oldpassword:原密码,String类型
	 * @param newpassword:新密码,String类型
	 * @return json字串
	 * result  为1表示修改成功，0表示失败 ，int型
	 * message 为提示信息 ，String型
	 */
	@ApiOperation(value="修改会员密码" )
	@PostMapping(value="/change-password", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult changePassword(String oldpassword, String newpassword, String re_passwd) {
		Member member = UserConext.getCurrentMember();
		if(member==null){
			return JsonResultUtil.getErrorJson("尚未登录，无权使用此api");				

		}
		String oldPassword = oldpassword;
		oldPassword = oldPassword == null ? "" : StringUtil.md5(oldPassword);
		if (oldPassword.equals(member.getPassword())) {
			String password = newpassword;
			String passwd_re = re_passwd;
			if(StringUtil.isEmpty(password)){
				return JsonResultUtil.getErrorJson("新密码不能为空");
			}
			if(oldpassword.equals(password)){ 				
				return JsonResultUtil.getErrorJson("您输入新旧密相同，请重新输入"); 			
			}
			if (passwd_re.equals(password)) {
				try {
					memberManager.updatePassword(password);
					return JsonResultUtil.getSuccessJson("修改密码成功");

				} catch (Exception e) {
					LOGGER.error("修改密码失败", e);
					return JsonResultUtil.getErrorJson("修改密码失败");				
				}
			} else {
				return JsonResultUtil.getErrorJson("修改失败！两次输入的密码不一致");				

			}
		} else {
			return JsonResultUtil.getErrorJson("修改失败！原始密码不符");							
		}
	}

	/**
	 * 修改会员登陆密码
	 * @author add by DMRain 2016-7-7
	 * @param newpassword 新密码
	 * @param re_passwd 再次输入新密码
	 * @param authCode 验证码
	 * @return
	 */
	@ApiOperation(value="修改会员登陆密码" )
	@PostMapping(value="/update-password", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult updatePassword(String newpassword, String re_passwd, String authCode) {
		Member member = UserConext.getCurrentMember();
		/** 会员信息不能为空 */
		if (member == null) {
			return JsonResultUtil.getErrorJson("尚未登录，无权使用此api");				
		}

		String password = newpassword;
		String passwd_re = re_passwd;

		/** 新密码不能为空 */
		if(StringUtil.isEmpty(password)){
			return JsonResultUtil.getErrorJson("新密码不能为空");
		}

		/** 输入的新密码与原密码相同 */
		if (member.getPassword().equals(StringUtil.md5(password))) {
			return JsonResultUtil.getErrorJson("输入的新密码与原密码相同");
		}

		/** 再一次输入密码不能为空 */
		if(StringUtil.isEmpty(re_passwd)){
			return JsonResultUtil.getErrorJson("请再一次输入密码");
		}

		/** 两次输入的密码必须一致 */
		if (!passwd_re.equals(password)) {
			return JsonResultUtil.getErrorJson("两次输入的密码不一致");	
		}

		/** 验证码不能为空 */
		if(StringUtil.isEmpty(authCode)){
			return JsonResultUtil.getErrorJson("请输入验证码");
		}

		if (this.validcode(authCode,"membervalid") == 0) {
			return JsonResultUtil.getErrorJson("验证码输入错误!");				
		}
		try {
			memberManager.updatePassword(password);
			return JsonResultUtil.getSuccessJson("修改密码成功");

		} catch (Exception e) {
			LOGGER.error("修改密码失败", e);
			return JsonResultUtil.getErrorJson("修改密码失败");				
		}
	}

	/**
	 * 会员手机验证
	 * @author add_by DMRain 2016-7-7
	 * @param mobileCode 手机校验码
	 * @param validcode 验证码
	 * @param mobile 手机号
	 * @param key 手机校验码key值
	 * @return
	 */
	@ApiOperation(value=" 会员手机验证" )
	@GetMapping(value="/member-mobile-validate", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult memberMobileValidate(String mobileCode, String validcode, String mobile, String key){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		Member member = UserConext.getCurrentMember();
		try {

			/** 会员信息不能为空 */
			if (member == null) {
				return JsonResultUtil.getErrorJson("尚未登录，无权使用此api");				
			}

			/** 手机校验码不能为空 */
			if(StringUtil.isEmpty(mobileCode)){
				return JsonResultUtil.getErrorJson("手机校验码不能为空");
			}

			/** 手机号不能为空 */
			if (StringUtil.isEmpty(mobile)) {
				return JsonResultUtil.getErrorJson("手机号不能为空");	
			}

			/** 验证手机校验码的key值不能为空 */
			if(StringUtil.isEmpty(key)){
				return JsonResultUtil.getErrorJson("出现错误，请重试！");
			}
			if (this.validcode(validcode,"membervalid") == 0) {
				return JsonResultUtil.getErrorJson("验证码输入错误!");				
			}
			
			/** 验证码不能为空 */
			if(StringUtil.isEmpty(validcode)){
				return JsonResultUtil.getErrorJson("验证码不能为空");
			}
			boolean result = memberManager.checkSms(mobile, mobileCode, key);

			/** 如果手机校验码错误 */
			if (!result) {
				return JsonResultUtil.getErrorJson("短信验证码错误");
			} else {
				/** 把注册信息 加密  放到session当中 */
				//TODO 取加密信息的时候报错，取到乱码，需要后期修复
				//String ciphertext = EncryptionUtil1.authcode("{\"account_type\" : \"mobile\",\"account\" : \"" + mobile + "\"}", "ENCODE", "", 0);
				request.getSession().setAttribute("account_detail", mobile);

				/** 如果手机校验码的key值为binding */
				if (key.equals(MemberSendCodeEnum.BINDINGMOBILE.name())) {
					this.memberManager.changeMobile(member.getMember_id(), mobile);
				}

				return JsonResultUtil.getSuccessJson("验证成功");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}

	/**
	 * 验证原密码输入是否正确
	 * @param oldpassword:密码，String型
	 * @return json字串
	 * result  为1表示原密码正确，0表示失败 ，int型
	 * message 为提示信息 ，String型
	 */
	@ApiOperation(value=" 验证原密码输入是否正确" )
	@PostMapping(value="/password", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult password(String oldpassword){
		Member member = UserConext.getCurrentMember();
		String old = oldpassword;
		String oldPassword = StringUtil.md5(old);
		if (oldPassword.equals(member.getPassword())){
			return JsonResultUtil.getSuccessJson("正确");
		}else{
			return JsonResultUtil.getErrorJson("输入原始密码错误");				
		}
	}



	/**
	 * 搜索会员(要求管理员身份)
	 * @param lvid:会员级别id，如果为空则搜索全部会员级别，int型
	 * @param keyword:搜索关键字,可为空，String型
	 * @return json字串
	 * result  为1表示搜索成功，0表示失败 ，int型
	 * data: 会员列表
	 * {@link Member}
	 */
	@ApiOperation(value="搜索会员(要求管理员身份)" )
	@PostMapping(value="/search", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult search(Map memberMap, Integer lvid , String keyword){
		try{
			if(UserConext.getCurrentAdminUser()==null){
				return JsonResultUtil.getErrorJson("无权访问此api");				

			}
			memberMap = new HashMap();
			memberMap.put("lvId", lvid);
			memberMap.put("keyword", keyword);
			memberMap.put("stype", 0);
			List memberList  =this.memberManager.search(memberMap);
			if (memberList.size() == 0) {
				return JsonResultUtil.getErrorJson("未搜索到相关会员");
			} else {
				return JsonResultUtil.getObjectJson(memberList);
			}
		}catch(Throwable e){
			LOGGER.error("搜索会员出错", e);
			return JsonResultUtil.getErrorJson("搜索会员出错");						
		}
	}

	/**
	 * 检测mobile是否存在，并生成json返回给客户端
	 * @author add_by DMRain 2016-7-6
	 * @param mobile 手机号
	 * @return
	 */
	@ApiOperation(value="检测mobile是否存在，并生成json返回给客户端" )
	@PostMapping(value="/checkmobile", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkmobile(String mobile) {
		boolean result = this.memberManager.checkMobile(mobile);
		if(result){
			return JsonResultUtil.getSuccessJson("手机号不存在，可以使用");
		}else{
			return JsonResultUtil.getErrorJson("该手机号已经存在！");	
		}

	}

	/**
	 * 用户登录修改信息检测邮箱是否存在
	 * 检测email是否存在，并生成json返回给客户端
	 * @param email
	 * @return
	 */
	@ApiOperation(value="用户登录修改信息检测邮箱是否存在" )
	@PostMapping(value="/checkemailInEdit", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkemailInEdit(String email) {
		Member member = UserConext.getCurrentMember();
		boolean	flag = this.memberManager.checkemailInEdit(email, member.getMember_id());//true为可用，false不可用
		if(flag){
			return JsonResultUtil.getSuccessJson("邮箱不存在，可以使用");
		}else{
			return JsonResultUtil.getErrorJson("该邮箱已经存在！");	
		}

	}

	/**
	 * 重新发送激活邮件
	 */
	@ApiOperation(value="重新发送激活邮件" )
	@PostMapping(value="/re-send-reg-mail", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult reSendRegMail(){
		try{
			/** 重新发送激活邮件 */
			Member member = UserConext.getCurrentMember();
			if(member == null){
				return JsonResultUtil.getErrorJson("请您先登录再重新发送激活邮件!");				
			}
			member = memberManager.get(member.getMember_id());
			if(member == null){
				return JsonResultUtil.getErrorJson("用户不存在,请您先登录再重新发送激活邮件!");				

			}
			if(member.getLast_send_email() != null && System.currentTimeMillis() / 1000 - member.getLast_send_email().intValue() < 2 * 60 * 60){
				return JsonResultUtil.getErrorJson("对不起，两小时之内只能重新发送一次激活邮件!");				

			}

			EopSite site  = EopSite.getInstance();
			String domain =RequestUtil.getDomain();
			String checkurl  = domain+"/memberemailcheck.html?s="+ EncryptUtil.encryptCode(member.getMember_id()+","+member.getRegtime());
			EmailModel emailModel = new EmailModel();
			emailModel.getData().put("username", member.getUname());
			emailModel.getData().put("checkurl", checkurl);
			emailModel.getData().put("sitename", site.getSitename());
			emailModel.getData().put("logo", site.getLogofile());
			emailModel.getData().put("domain", domain);
			if (memberPointManger.checkIsOpen(IMemberPointManger.TYPE_EMIAL_CHECK) ){
				int point =memberPointManger.getItemPoint(IMemberPointManger.TYPE_EMIAL_CHECK+"_num");
				int mp =memberPointManger.getItemPoint(IMemberPointManger.TYPE_EMIAL_CHECK+"_num_mp");
				emailModel.getData().put("point", point);
				emailModel.getData().put("mp", mp);
			}
			emailModel.setTitle(member.getUname()+"您好，"+site.getSitename()+"会员注册成功!");
			emailModel.setEmail(member.getEmail());
			emailModel.setTemplate("reg_email_template.html");
			emailModel.setEmail_type("邮箱激活");
			mailMessageProducer.send(emailModel);
			member.setLast_send_email(DateUtil.getDateline());
			memberManager.edit(member);
			return JsonResultUtil.getSuccessJson("激活邮件发送成功，请登录您的邮箱 " + member.getEmail() + " 进行查收！");

		}catch(RuntimeException e){
			return JsonResultUtil.getErrorJson(e.getMessage());				

		}
	}

	/**
	 * 完善会员资料
	 * @param tel         // 固定电话
	 * @param file        // 头像
	 * @param truename    // 真实姓名
	 * @param zip         // 邮编
	 * @param mobile      // 电话
	 * @param sex         // 性别
	 * @param city        // 城市
	 * @param region      // 区
	 * @param town        //城镇
	 * @param email       // 邮件
	 * @param address     // 详细地址
	 * @param mybirthday  // 生日
	 * @param province_id // 省ID
	 * @param city_id     // 市ID
	 * @param region_id   // 区ID
	 * @param town_id     //城镇
	 * @param province    // 省份
	 * @return
	 * 修改人：whj 
	 * 修改时间：2016-09-18 
	 * 修改内容：对（mobile）字段为空的校验
	 */
	@ApiOperation(value="完善会员资料" )
	@ApiImplicitParams({
        @ApiImplicitParam(name = "tel", value = "固定电话", required = false, dataType = "String" ,paramType="query"),
        @ApiImplicitParam(name = "file", value = "上传头像", required = false, dataType = "MultipartFile" ,paramType="query"),
        @ApiImplicitParam(name = "zip", value = "邮编", required = false, dataType = "String" ,paramType="query"),
	 })
	@PostMapping(value="/re-send-reg-mailsave-info", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult saveInfo( String tel, MultipartFile file, String truename,  String zip, String mobile, String sex, 
			String city, String region,String town, String email, String address, String mybirthday, Integer province_id, 
			Integer city_id, Integer region_id, Integer town_id,String province){
		Member member = UserConext.getCurrentMember();

		member = memberManager.get(member.getMember_id());


		/** 先上传图片 */
		String faceField = "faceFile";

		if(file!=null){
			/** 判断文件类型 */
			if(!FileUtil.isAllowUpImg(file.getOriginalFilename())){
				return JsonResultUtil.getErrorJson("不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
			}

			/** 判断文件大小 */
			if(file.getSize() > 200 * 1024){
				return JsonResultUtil.getErrorJson("'对不起,图片不能大于200K！");				

			}
			/** 获取图片操作方案 */
			IUploader uploader=uploadFactory.getUploader();
			/** 上传图片 */
			String imgPath=	uploader.upload(file);
			member.setFace(imgPath);
		}

		HttpServletRequest request =  ThreadContextHolder.getHttpRequest();

		if(StringUtil.isEmpty(mybirthday)){
			member.setBirthday(0L);
		}else{
			member.setBirthday(DateUtil.getDateline(mybirthday));
		}


		member.setProvince_id(province_id);
		member.setCity_id(city_id);
		member.setRegion_id(region_id);
		member.setTown_id(town_id);
		member.setProvince(province);
		member.setCity(city);
		member.setRegion(region);
		member.setTown(town);
		member.setEmail(email);
		member.setAddress(address);
		member.setZip(zip);

		/** 判断会员电话字段是否为空 */
		if(StringUtil.isEmpty(member.getMobile())){
			if(!StringUtil.isEmpty(mobile)){
				member.setMobile(mobile);
			}
		}

		member.setTel(tel);

		/** 如果会员真实姓名不为空 */
		if(truename != null){
			member.setName(truename);
		}

		member.setSex(Integer.valueOf(sex));
		/** 身份 */
		String midentity = request.getParameter("member.midentity");
		if (!StringUtil.isEmpty(midentity)) {
			member.setMidentity(StringUtil.toInt(midentity,true));
		} else {
			member.setMidentity(0);
		}

		try {
			/** 判断否需要增加积分 */
			boolean addPoint = false;
			if (member.getInfo_full() == 0 && !StringUtil.isEmpty(member.getName())&&!StringUtil.isEmpty(member.getNickname()) && !StringUtil.isEmpty(member.getProvince())&& !StringUtil.isEmpty(member.getCity()) && !StringUtil.isEmpty(member.getRegion()) && !StringUtil.isEmpty(member.getTel())) {
				this.amqpTemplate.convertAndSend(AmqpExchange.MEMBER_INFO_COMPLETE.name(), "member-info-complete-routingkey",member.getMember_id());
				member.setInfo_full(1);
			}
			memberManager.edit(member);
			return JsonResultUtil.getSuccessJson("编辑个人资料成功！");

		} catch (Exception e) {
			LOGGER.error("编辑个人资料失败！", e);
			return JsonResultUtil.getErrorJson("编辑个人资料失败！");				
		}
	} 




	/**
	 * 保存从Flash编辑后返回的头像，保存二次，一大一小两个头像
	 * 
	 * @return
	 */
	@ApiOperation(value="保存从Flash编辑后返回的头像，保存二次，一大一小两个头像" )
	@PostMapping(value="/save-avatar", produces=MediaType.APPLICATION_JSON_VALUE)
	public String saveAvatar(String photoServer, String photoId, String type) {
		String targetFile = makeFilename("avatar",photoServer,photoId,type);

		int potPos = targetFile.lastIndexOf('/') + 1;
		String folderPath = targetFile.substring(0, potPos);
		FileUtil.createFolder(folderPath);

		try {
			File file = new File(targetFile);

			if (!file.exists()) {
				file.createNewFile();
			}

			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			FileOutputStream dos = new FileOutputStream(file);
			int x = request.getInputStream().read();
			while (x > -1) {
				dos.write(x);
				x = request.getInputStream().read();
			}
			dos.flush();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("big".equals(type)) {
			Member member = UserConext.getCurrentMember();
			member.setFace("fs:/attachment/avatar/" + photoId + "_big."
					+ FileUtil.getFileExt(photoServer));
			memberManager.edit(member);
		}

		String json = "{\"data\":{\"urls\":[\"" + targetFile
				+ "\"]},\"status\":1,\"statusText\":\"保存存成功\"}";

		return json;
	}

	/**
	 * 上传头像文件
	 * 
	 * @return
	 */
	@ApiOperation(value="上传头像文件" )
	@PostMapping(value="/upload-avatar", produces=MediaType.APPLICATION_JSON_VALUE)
	public String uploadAvatar(File faceFile, String faceFileName, @RequestParam(value = "face", required = false) MultipartFile face) {
		JSONObject jsonObject = new JSONObject();
		try {
			if (faceFile != null) {
				/** 获取文件操作方案 */
				IUploader uploader=uploadFactory.getUploader();
				/** 文件上传 */
				String file = uploader.upload(face);
				Member member = UserConext.getCurrentMember();
				jsonObject.put("result", 1);
				jsonObject.put("member_id", member.getMember_id());
				jsonObject.put("url", toUrl(file));
				jsonObject.put("message", "操作成功！");
			}
		} catch (Exception e) {
			jsonObject.put("result", 0);
			jsonObject.put("message", "操作失败！");
		}

		String json =jsonObject.toString();
		return json;
	}

	protected String toUrl(String path) {
		String static_server_domain= SystemSetting.getStatic_server_domain();
		String urlBase = static_server_domain;
		return path.replaceAll("fs:", urlBase);
	}

	protected String makeFilename(String subFolder, String photoServer, String photoId, String type ) {
		String ext = FileUtil.getFileExt(photoServer);
		String fileName = photoId + "_" + type + "." + ext;
		String static_server_path= null;

		String filePath = static_server_path + "/attachment/";
		if (subFolder != null) {
			filePath += subFolder + "/";
		}

		filePath += fileName;
		return filePath;
	}

	
	/**
	 * 检查用户输入的验证码
	 * 需要传入mobileNum一个参数
	 * 
	 * @param mobileNum 验证码,String型
	 *  
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */
	@ApiOperation(value="检查用户输入的验证码" )
	@PostMapping(value="/check-sms-code", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkSmsCode(String mobileNum, String validcode ) {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
//			String code = (String)request.getSession().getAttribute("smscode");
			Member member = memberManager.getMemberByMobile(mobileNum);
			if(member==null){
				return JsonResultUtil.getErrorJson("没有找到用户");
			}
			if(SmsUtil.validSmsCode(validcode, mobileNum, MemberSendCodeEnum.FINDPASSWORDMOBILE.name())) {
				request.getSession().setAttribute("smscode", "999");
				request.getSession().setAttribute("smsnum", member.getMember_id());
				return JsonResultUtil.getSuccessJson("验证成功");
			} else {
				return JsonResultUtil.getErrorJson("验证失败");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	
	}
	
	/**
	 * 验证通过后重置密码
	 * 需要传入mobileNum一个参数
	 *
	 * @param password 新密码,String型
	 * 
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */
	@ApiOperation(value="验证通过后重置密码" )
	@PostMapping(value="/reset-password", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult resetPassword(String password) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		String code = (String)request.getSession().getAttribute("smscode");
		Integer memberid = (Integer)request.getSession().getAttribute("smsnum");
		if(memberid!=null && "999".equals(code)) {
			try {
				memberManager.updatePassword(memberid, password);
				request.getSession().setAttribute("smscode", null);
				return JsonResultUtil.getSuccessJson("新密码设置成功");
			} catch(Exception e) {
				return JsonResultUtil.getErrorJson("设置密码出错");
			}
			
		} else {
			return JsonResultUtil.getErrorJson("认证超时，请重新验证");
		}
	}
		
	protected String createRandom(){
		Random random  = new Random();
		StringBuffer pwd=new StringBuffer();
		for(int i=0;i<6;i++){
			pwd.append(random.nextInt(9));
			 
		}
		return pwd.toString();
	}
	
	
	/**
	 * 找回密码
	 * @param choose 找回方式.0为邮箱1.用户名
	 * @param email 邮箱或者用户名
	 * @param email
	 */
	@ApiOperation(value="找回密码" )
	@PostMapping(value="/find", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult find(Integer choose,String email){
		//获取站点信息
		EopSite  site  =EopSite.getInstance();
		//验证邮箱
		
		//获取用户信息By Email
		Member member =	new Member();
		if(choose==0){
			if(email==null || !StringUtil.validEmail(email)){
				return JsonResultUtil.getErrorJson("请输入正确的邮箱地址");				

			}
			member=this.memberManager.getMemberByEmail(email);
		}else{
			if(email == null || StringUtil.isEmpty(email)){
				return JsonResultUtil.getErrorJson("请输入正确的用户名");				

			}
			member=this.memberManager.getMemberByUname(email);
		}
		if(member==null){
			return JsonResultUtil.getErrorJson("["+ email +"]不存在!");	
		}
		String domain =RequestUtil.getDomain();
		String initCode = member.getMember_id()+","+member.getRegtime();
		String edit_url  =domain+ "/findPassword.html?s="+ EncryptUtil.encryptCode(initCode);
		if(member.getEmail() == null){
			return JsonResultUtil.getErrorJson("用户邮箱地址为空!请完善个人用户信息");
		}
		//编辑邮件信息发送邮件
		if(!StringUtil.isEmpty(member.getEmail())) {
			Map data = new HashMap();
			data.put("byName","通过邮箱找回密码");
			data.put("code",initCode);
			data.put("email",member.getEmail());
			data.put("email_title","找回密码提醒");
			this.amqpTemplate.convertAndSend(AmqpExchange.SMS_SEND_MESSAGE.name(), "memberCodeSendMsg",data);
		}
		this.memberManager.updateFindCode(member.getMember_id(),DateUtil.getDateline()+"");
		return JsonResultUtil.getSuccessJson("请登录"+ member.getEmail()+"查收邮件并完成密码修改。");
	}
	
	
	/**
	 * 修改密码
	 * @param password
	 * @param conpasswd
	 * @param s
	 */
	@ApiOperation(value="修改密码" )
	@PostMapping(value="/modify", produces=MediaType.APPLICATION_JSON_VALUE)
	public JsonResult modify(String email,String password,String conpasswd,String s){
		if(email==null || !StringUtil.validEmail(email)){
			return JsonResultUtil.getErrorJson("邮箱错误,请重试");				
		}
		if(s==null){
			return JsonResultUtil.getErrorJson("非法链接地址,请重新找回");				
		}
		String str = EncryptUtil.decryptCode(s);
		String[] array = StringUtils.split(str,",");
		if(array.length!=2){
			return JsonResultUtil.getErrorJson("验证字串不正确,请重新找回");				
		}
		int memberid  = Integer.valueOf(array[0]);
		long regtime = Long.valueOf(array[1]);
		
		Member member = this.memberManager.get(memberid);
		if(member==null || member.getRegtime() != regtime){
			return JsonResultUtil.getErrorJson("验证字串不正确,请重新找回");				
		}
		if(member.getFind_code()==null||"".equals(member.getFind_code())||member.getFind_code().length()!=10){
			return JsonResultUtil.getErrorJson("地址已经过期,请重新找回");				
		}
		int time = Integer.parseInt(member.getFind_code())+60*60;
		if(DateUtil.getDateline()>time){
			return JsonResultUtil.getErrorJson("地址已经过期,请重新找回");				
		}
		if(!password.equals(conpasswd)){
			return JsonResultUtil.getErrorJson("密码不同");				
		}
		this.memberManager.updatePassword(memberid, password);
		this.memberManager.updateFindCode(memberid, "");
		this.memberManager.staticPwdLogin(member.getUname(), password);
		return JsonResultUtil.getSuccessJson("修改密码成功");

	}
	
	/**
	 * 校验验证码
	 * 
	 * @param validcode
	 * @param name (1、memberlogin:会员登录  2、memberreg:会员注册 3、membervalid:会员手机验证)
	 * @return 1成功 0失败
	 */
	private int validcode(String validcode,String name) {
		if (validcode == null) {
			return 0;
		}

		String code = (String) ThreadContextHolder.getSession().getAttribute(ValidCodeServlet.SESSION_VALID_CODE + name);

		if (code == null) {
			return 0;
		} else {
			if (!code.equalsIgnoreCase(validcode)) {
				return 0;
			}
		}
		return 1;
	}
	
	
}
