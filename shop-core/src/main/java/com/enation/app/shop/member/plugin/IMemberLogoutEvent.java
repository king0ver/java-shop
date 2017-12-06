package com.enation.app.shop.member.plugin;

import com.enation.app.base.core.model.Member;

/**
 * 会员注销事件
 * @author kingapex
 *
 */
public interface IMemberLogoutEvent {
	 
	public void onLogout(Member member);
	
	
}
