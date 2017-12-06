package com.enation.app.core.event;

import com.enation.framework.jms.EmailModel;

/**
 * 发送邮件事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:45:08
 */
public interface ISendEmailEvent {

	/**
	 * 发送邮件
	 * @param emailModel 
	 */
	public void sendEmail(EmailModel emailModel);
}
