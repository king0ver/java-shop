package com.enation.app.shop.message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.enation.app.core.event.IMemberMessageEvent;
import com.enation.app.shop.member.model.po.MessageBack;
import com.enation.app.shop.member.service.IMessageBgManager;
import com.enation.app.shop.message.service.IMemberMessageManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 会员站内消息consumer
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年10月14日 上午11:27:15
 */
@Service
public class MemberMessageConsumer implements  IMemberMessageEvent{

	@Autowired
	private IMessageBgManager messageBgManager;
	@Autowired
	private IMemberMessageManager  memberMessageManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public void memberMessage(int message_id) {
		
		MessageBack message = messageBgManager.get(message_id);
		Integer sendType = message.getSend_type();//发送类型    0  全站   1   部分
		List<String> memberIdsRes;
		if(sendType != null && sendType.equals(0)){
			String sql = " select member_id from es_member ";
		    memberIdsRes = this.daoSupport.queryForList(sql, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					return rs.getString("member_id");
				}
			});
		}else{
			String memberIds = message.getMember_ids();
			String[] memberIdsArray = memberIds.split(",");
			memberIdsRes = Arrays.asList(memberIdsArray);
		}
		if(memberIdsRes.size() > 0){
			String msgContent = message.getMsg_content();
			for(String id : memberIdsRes){
				memberMessageManager.addMemberContent(msgContent, Integer.valueOf(id));
			}
		}
		
	}

}
