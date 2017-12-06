package com.enation.app.core.event;

import java.util.Map;

/**
 * 发送短信
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午6:06:04
 */
public interface ISmsSendMessageEvent {

	/**
	 * 发送短信
	 * @param data
	 */
	public void sendMessage(Map<String,Object> data);
}
