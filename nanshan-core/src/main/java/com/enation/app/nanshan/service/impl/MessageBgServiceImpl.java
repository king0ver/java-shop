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
	public Page<MessageBgVo> queryMessageInfoByPage(Integer member_id,int pageNo,int pageSize) {


		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (")
			  .append("SELECT t.msg_id,t.msg_content,t.msg_title,t.member_ids,t.adminuser_id,t.send_time,t.send_type,t.adminuser_name")
			  .append(" FROM es_message_bg t where t.member_ids is null or t.member_ids = ''")
			  .append(" union all select t.msg_id,t.msg_content,t.msg_title,t.member_ids,t.adminuser_id,t.send_time,t.send_type,t.adminuser_name")
			  .append(" FROM es_message_bg t where  FIND_IN_SET(?,member_ids)) x")
              .append(" order by x.send_time  desc");

		Page<MessageBgVo> page=daoSupport.queryForPage(buffer.toString(), pageNo,pageSize,member_id);
		return page;
	}

}
