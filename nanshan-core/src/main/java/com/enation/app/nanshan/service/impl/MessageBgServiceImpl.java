package com.enation.app.nanshan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.service.IMessageBgService;
import com.enation.app.nanshan.vo.MessageBgVo;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

@Service("messageBgService")
public class MessageBgServiceImpl implements IMessageBgService {

	@Autowired
	private IDaoSupport daoSupport;
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<MessageBgVo> queryMessageInfoByPage(int pageNo,int pageSize) {
		String sql = "SELECT t.msg_id,t.msg_content,t.msg_title,t.member_ids,t.adminuser_id,t.send_time,t.send_type,t.adminuser_name FROM es_message_bg t ";
		Page<MessageBgVo> page=daoSupport.queryForPage(sql, pageNo,pageSize);
		return page;
	}

}
