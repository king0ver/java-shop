package com.enation.app.shop.message.service;

import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.framework.database.Page;

/**
 * 消息管理类
 * @author Kanon
 * @since v6.4.0
 * @version v1.0
 * 2017-8-16
 */
public interface IMessageManager {

	/**
	 * 获取消息模板列表
	 * @param type 类型 1：会员 2：店铺 3：其他
	 * @return
	 */
	public Page listPage(Integer pageNo,Integer pageSize,Integer type);
	
	/**
	 * 修改消息模板
	 * @param messageTemplate 消息模板
	 */
	public void editMessageTemplate(MessageTemplate messageTemplate);
	
	
	/**
	 * 获取消息模板
	 * @param id 模板ID
	 * @return 消息模板
	 */
	public MessageTemplate getMessageTemplate(Integer id);
	
	/**
	 * 根据编号获取消息模板
	 * @param code 编号
	 * @return 消息模板
	 */
	public MessageTemplate getMessageTemplate(String code);
	
	/**
	 * 添加消息模板
	 * @param messageTemplate 消息模板
	 */
	public void addMessageTemplate(MessageTemplate messageTemplate);
	
}
