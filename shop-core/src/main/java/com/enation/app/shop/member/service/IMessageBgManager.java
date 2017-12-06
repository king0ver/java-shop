package com.enation.app.shop.member.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.member.model.MessageBg;
import com.enation.app.shop.member.model.po.MessageBack;
import com.enation.framework.database.Page;

/**
 * 
 * 后台站内消息
 * @author fk
 * @version v1.0
 * @since v6.1
 * 2016年12月5日 下午1:29:27
 */
public interface IMessageBgManager {

	
	/**
	 * 管理员添加消息
	 * @param messageBg 站内通知消息后台
	 * @return 0：添加失败，1：添加成功
	 */
	public void add(MessageBg messageBg) throws Exception;

	/**
	 * 在后台显示所有的历史消息
	 * @param page
	 * @param pageSize
	 * @return 历史消息
	 */
	public Page getAllMessage(int page, int pageSize);

	/**
	 * 获取后台发送的会员消息
	 * @param message_id
	 * @return
	 */
	public MessageBack get(int message_id);
}
