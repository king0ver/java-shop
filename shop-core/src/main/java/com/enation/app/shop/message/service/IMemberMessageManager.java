package com.enation.app.shop.message.service;

import java.util.Map;

import com.enation.app.shop.message.model.MemberNoticeLog;
import com.enation.framework.database.Page;

/**
 * 会员消息管理接口
 * @author Kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-16
 */
public interface IMemberMessageManager {

	/**
	 * 获取会员消息列表
	 * @param pageNo 分页页数
	 * @param pageSize 每页显示数量
	 * @param type 类型
	 * @param member_id 会员编号
	 * @return 店铺消息分页列表
	 */
	public Page getMemberMessageList(Integer pageNo,Integer pageSize,Integer member_id);
	
	/**
	 * 删除站内信
	 * @param id 编号
	 */
	public void delete(String messageIds);

	/**
	 * 批量标记已读
	 * @param ids 已读编号数组
	 */
	public void read(String messageIds);
	
	/**
	 * 添加会员站内信内容
	 * @param content 信息内容
	 * @param memberId 会员ID
	 */
	public void addMemberContent(String content,Integer memberId);
	
	/**
	 * 记录会员短信内容
	 * @param smsContent 短信内容
	 * @param memberId 会员ID
	 */
	public void addMemberSmsContent(String smsContent,Integer memberId);
	
	/**
	 * 获取消息详情
	 * @param messageId
	 * @return
	 */
	public MemberNoticeLog getMessageDetail(Integer messageId);
	
	/**
	 * 获取未读的站内消息数量
	 * @return
	 */
	public int getMessageNoReadCount();
	
	/**
	 * 查询此范围内的消息个数
	 * @param messageIds 消息编号
	 * @return 消息个数
	 */
	public Integer getCountByIds(String messageIds);
}
