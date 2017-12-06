package com.enation.app.core.event;

/**
 * 在线支付事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:45:08
 */
public interface IOnlinePayEvent {

	/**
	 * 在线支付
	 * @param member_id
	 */
	public void onlinePay(Integer member_id);
}
