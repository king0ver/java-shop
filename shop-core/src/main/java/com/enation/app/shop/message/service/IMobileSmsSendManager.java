package com.enation.app.shop.message.service;

import com.enation.app.shop.message.model.vo.MobileVo;

/**
 * 手机短信发送接口
 * @author Kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-9
 */
public interface IMobileSmsSendManager {

	
	/**
	 * 发送mq短信
	 * @param mobileVo 发送封装实体
	 * @return
	 */
	public boolean sendMqMsg(MobileVo mobileVo);
	
	
}
