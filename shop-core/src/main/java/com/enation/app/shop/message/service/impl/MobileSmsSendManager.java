package com.enation.app.shop.message.service.impl;


import java.util.Map;

import com.enation.app.base.core.model.SmsPlatform;
import com.enation.app.base.core.plugin.sms.ISmsSendEvent;
import com.enation.app.base.core.service.ISmsManager;
import com.enation.framework.context.spring.SpringContextHolder;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.message.model.vo.MobileVo;
import com.enation.app.shop.message.service.IMobileSmsSendManager;

/**
 * 手机短信发送管理类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年9月27日 下午6:02:49
 */
@Service
public class MobileSmsSendManager implements IMobileSmsSendManager {

	@Autowired
	private ISmsManager smsManager;


	@Override
	public boolean sendMqMsg(MobileVo mobileVo) {
		try {
			SmsPlatform platform = smsManager.getOpen();
			System.out.println("===============开始发送！！！！============");
			/** 判断是否设置了短信网关组件 */
			if (platform != null) {
				String config = platform.getConfig();
				JSONObject jsonObject = JSONObject.fromObject(config);

				Map itemMap = (Map)jsonObject.toBean(jsonObject, Map.class);


				ISmsSendEvent smsSendEvent = SpringContextHolder.getBean(platform.getCode());
				smsSendEvent.onSend(mobileVo.getMobile(), mobileVo.getContent(), itemMap);
				System.out.println("===============发送成功！！！！============");
			}
		} catch (RuntimeException e) {
			return false;
		}
		return true;
	}


}
