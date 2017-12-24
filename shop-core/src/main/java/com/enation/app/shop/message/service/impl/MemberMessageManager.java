package com.enation.app.shop.message.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.message.model.MemberNoticeLog;
import com.enation.app.shop.message.model.MemberSMSLog;
import com.enation.app.shop.message.service.IMemberMessageManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;

/**
 * 会员消息管理类
 * 
 * @author Kanon
 * @since 6.4.0
 * @version 1.0 2017-8-16
 */
@Service
public class MemberMessageManager implements IMemberMessageManager {

	@Autowired
	private IDaoSupport daoSupport;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.order.service.message.IMemberMessageManager#
	 * getMemberMessageList(java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getMemberMessageList(Integer pageNo, Integer pageSize, Integer member_id) {

		String sql = "SELECT * FROM es_member_notice_log WHERE member_id=? and is_delete = 0 ";

		sql += " ORDER BY id DESC";
		return daoSupport.queryForPage(sql, pageNo, pageSize, member_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.message.service.IMemberMessageManager#delete(java.
	 * lang.String)
	 */
	@Override
	public void delete(String messageIds) {
		Map map = new HashMap<String, Object>();
		map.put("is_delete", 1);
		daoSupport.update("es_member_notice_log", map , " id IN ("+messageIds+")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.message.service.IMemberMessageManager#read(java.lang
	 * .String)
	 */
	@Override
	public void read(String messageIds) {
		Map map = new HashMap<String, Object>();
		map.put("is_read", 1);
		daoSupport.update("es_member_notice_log", map , " id IN ("+messageIds+")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.base.core.service.IMemberMessageManager#addMemberContent(
	 * java.lang.String, java.lang.Integer)
	 */
	@Override
	public void addMemberContent(String content, Integer memberId) {
		MemberNoticeLog memberNoticeLog = new MemberNoticeLog();
		memberNoticeLog.setMember_id(memberId);
		memberNoticeLog.setNotice_content(content);
		memberNoticeLog.setSend_time(DateUtil.getDateline());
		daoSupport.insert("es_member_notice_log", memberNoticeLog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.base.core.service.IMemberMessageManager#
	 * addMemberSmsContent(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void addMemberSmsContent(String smsContent, Integer memberId) {
		MemberSMSLog memberSMSLog = new MemberSMSLog();
		memberSMSLog.setMember_id(memberId);
		memberSMSLog.setSms_content(smsContent);
		memberSMSLog.setSend_time(DateUtil.getDateline());
		daoSupport.insert("es_member_sms_log", memberSMSLog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.message.service.IMemberMessageManager#
	 * getMessageDetail(java.lang.Integer)
	 */
	@Override
	public MemberNoticeLog getMessageDetail(Integer messageId) {
		return this.daoSupport.queryForObject("SELECT * FROM es_member_notice_log WHERE id=? AND member_id=?",
				MemberNoticeLog.class, messageId, UserConext.getCurrentMember().getMember_id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.app.shop.message.service.IMemberMessageManager#
	 * getMessageNoReadCount()
	 */
	@Override
	public int getMessageNoReadCount() {
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			return 0;
		}
		String sql = "SELECT COUNT(0) from es_member_notice_log WHERE member_id =? AND is_read =0 AND is_delete =0";
		return daoSupport.queryForInt(sql.toString(), member.getMember_id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.message.service.IMemberMessageManager#getCountByIds(
	 * java.lang.String)
	 */
	@Override
	public Integer getCountByIds(String messageIds) {
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			return 0;
		}
		String sql = "SELECT COUNT(0) FROM es_member_notice_log WHERE  member_id =? AND id IN (" + messageIds + ")";

		return daoSupport.queryForInt(sql.toString(), member.getMember_id());
	}
}
