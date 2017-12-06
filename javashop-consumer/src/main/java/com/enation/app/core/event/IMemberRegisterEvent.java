package com.enation.app.core.event;

import com.enation.app.shop.member.model.vo.MemberRegistVo;

/**
 * 会员注册事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:06:07
 */
public interface IMemberRegisterEvent {

	/**
	 * 会员注册
	 * @param member_id
	 */
	public void memberRegister(MemberRegistVo member_vo);
}
