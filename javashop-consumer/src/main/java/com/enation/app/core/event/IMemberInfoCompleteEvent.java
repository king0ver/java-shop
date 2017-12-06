package com.enation.app.core.event;

/**
 * 会员完善个人信息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:06:07
 */
public interface IMemberInfoCompleteEvent {

	/**
	 * 会员完善个人信息
	 * @param member_id
	 */
	public void memberInfoComplete(Integer member_id);
}
