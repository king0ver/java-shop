package com.enation.app.shop.message.service;

import com.enation.framework.jms.EmailModel;

/**
 * amqp邮箱发送接口
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年9月27日 下午6:01:26
 */
public interface IEmailSendlManager {

	/**
	 * 发送mq邮件
	 * @param emailModel 发送邮件所需参数
	 * @return
	 */
	public boolean sendMqMsg(EmailModel emailModel);
	
	
}
