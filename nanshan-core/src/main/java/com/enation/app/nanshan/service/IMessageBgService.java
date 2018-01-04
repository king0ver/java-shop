package com.enation.app.nanshan.service;

import com.enation.app.nanshan.vo.MessageBgVo;
import com.enation.framework.database.Page;
/**
 * 消息服务
 * @author jianjianming
 * @version $Id: IMessageBgService.java,v 0.1 2018年1月4日 上午11:27:38$
 */
public interface IMessageBgService {
	
	/**
	 * 分页查询消息信息
	 * @param catId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<MessageBgVo> queryMessageInfoByPage(int pageNo, int pageSize);
	
}
