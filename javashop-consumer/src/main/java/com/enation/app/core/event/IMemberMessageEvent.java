package com.enation.app.core.event;

/**
 * 站内消息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午6:00:51
 */
public interface IMemberMessageEvent {

	/**
	 * 会员站内消息消费
	 * @param message_id
	 */
	public void memberMessage(int message_id);
}
