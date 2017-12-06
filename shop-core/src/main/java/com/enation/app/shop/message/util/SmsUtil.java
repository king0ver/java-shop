package com.enation.app.shop.message.util;

import com.enation.app.shop.member.model.enums.MemberSendCodeEnum;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.message.model.enums.MessageCodeEnum;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.Validator;

/**
 * 短信相关通用方法
 * 
 * @author Sylow
 * @version v1.0,2016年7月6日
 * @since v6.1
 * 
 */
public class SmsUtil {

	// 短信验证码session前缀
	private static final String SMS_CODE_PREFIX = "es_sms_";

	// 短信超时时间前缀
	private static final String SENDTIME_PREFIX = "es_sendtime";
	// 短信过期时间
	private static final Long SMS_CODE_TIMEOUT = 120l;

	/**
	 * 验证手机验证码是否正确
	 * 
	 * @param validCode
	 *            验证码
	 * @param mobile
	 *            手机号
	 * @param key
	 *
	 * @exception RuntimeException
	 *                手机号格式错误出错
	 * @return
	 */
	public static boolean validSmsCode(String validCode, String mobile, String key) {
		// 如果手机号格式不对
		if (!Validator.isMobile(mobile)) {
			throw new RuntimeException("手机号码格式错误");
		}

		// 防止 空值
		if (key == null || "".equals(key)) {

			// 默认为登录
			key = MemberSendCodeEnum.LOGINMOBILE.name();
		}

		// 如果验证码为空
		if (validCode == null || "".equals(validCode)) {
			return false;
		}
		String code = (String) ThreadContextHolder.getSession().getAttribute(SMS_CODE_PREFIX + key + mobile);

		// 验证码为空
		if (code == null) {
			return false;
		} else {

			// 忽略大小写 判断 不正确
			if (!code.equalsIgnoreCase(validCode)) {
				return false;
			}
		}

		// 新增优化 auth zjp 2016-12-13
		// 验证短信是否超时
		Long sendtime = (Long) ThreadContextHolder.getSession().getAttribute(SENDTIME_PREFIX + key + mobile);
		Long checktime = DateUtil.getDateline();
		// 验证session但中是否存在当前注册用户的验证码
		if (sendtime == null) {
			return false;
		}
		;
		if ((checktime - sendtime >= SMS_CODE_TIMEOUT)) {
			throw new RuntimeException("验证码超时");
		}
		// 验证通过后 去除session信息
		ThreadContextHolder.getSession().removeAttribute(SMS_CODE_PREFIX + key + mobile);
		return true;
	}

	/**
	 * 验证手机号有没有注册
	 * 
	 * @param mobile
	 *            手机号
	 * @exception RuntimeException
	 *                手机号格式错误出错
	 * @return boolean false=没有注册 true=注册了
	 */
	public static boolean validMobileIsRegister(String mobile) {

		// 如果手机号格式不对
		if (!Validator.isMobile(mobile)) {
			throw new RuntimeException("手机号码格式错误");
		}

		IMemberManager memberManager = SpringContextHolder.getBean("memberManager");
		boolean isExists = memberManager.checkMobile(mobile);
		return isExists;
	}


}
