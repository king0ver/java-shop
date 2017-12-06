package com.enation.app.shop.message.service;

import com.enation.app.shop.message.model.enums.ShopNoticeType;
import com.enation.framework.database.Page;

/**
 * 店铺消息管理接口
 * @author Kanon
 * @since 6.4.0
 * @version 1.0 
 * 2017-8-16
 */
public interface IStoreMessageManager {
	/**
	 * 获取店铺消息列表
	 * @param pageNo 分页页数
	 * @param pageSize 每页显示数量
	 * @param type 类型
	 * @param store_id 店铺编号
	 * @return 店铺消息分页列表
	 */
	public Page getStoreMessageList(Integer pageNo,Integer pageSize,Integer store_id,String type);
	
	/**
	 * 删除站内信
	 * @param id 编号
	 */
	public void delete(String ids);

	/**
	 * 批量标记已读
	 * @param ids 已读编号数组
	 */
	public void read(String ids);
	
	/**
	 * 记录店铺短信内容
	 * @param smsContent 短信内容
	 * @param storeId 店铺ID
	 */
	public void addStoreSmsContent(String smsContent,Integer storeId);
	
	/**
	 * 添加店铺站内信内容
	 * @param content 信息内容
	 * @param storeId 店铺ID
	 * @param ShopNoticeType 店铺消息类型
	 */
	public void addStoreContent(String content,Integer storeId,ShopNoticeType type);
}
