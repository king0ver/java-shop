package com.enation.app.shop.message;

import java.util.HashMap;
import java.util.Map;

import com.enation.eop.sdk.context.EopSetting;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.model.vo.MobileVo;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.app.shop.message.service.IMobileSmsSendManager;
import com.enation.eop.resource.model.EopSite;

/**
 * 会员短信发送
 * @author Kanon
 * @version v1.0
 * @since v6.4.0
 * 2017年9月8日
 */
@Component
public class MemberSmsSendConsumer {
	
	@Autowired
	private IMessageManager messageManager;
	
	@Autowired
	private IMobileSmsSendManager mobileSmsSendManager;
	
	public void receive(Map<String,Object> infoMap) {
		String key=infoMap.get("key").toString();
		String dynamicCode=infoMap.get("dynamicCode").toString();
		String mobile=infoMap.get("mobile").toString();
		
		MessageTemplate messageTemplate = messageManager.getMessageTemplate(key);
		
		// 动态码短信内容
		String smsContent = replaceSMSContent(messageTemplate.getSms_content(), dynamicCode);
		
		MobileVo mobileVo = new MobileVo();
		mobileVo.setContent(smsContent);
		mobileVo.setMobile(mobile);
		mobileSmsSendManager.sendMqMsg(mobileVo);
	}
	
	/**
	 * 替换站内信中的订单内容
	 * 
	 * @param content
	 *           内容
	 * @param code
	 *            验证码
	 */
	private static String replaceSMSContent(String content, String code) {
		/** 已经安装 */
		EopSetting.INSTALL_LOCK = "YES";
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("code", code);
		valuesMap.put("siteName", EopSite.getInstance().getSitename());

		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}
}
