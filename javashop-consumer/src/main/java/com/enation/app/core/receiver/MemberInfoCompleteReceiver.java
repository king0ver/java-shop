package com.enation.app.core.receiver;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IMemberInfoCompleteEvent;
import com.enation.app.core.event.IMemberLoginEvent;
import com.enation.app.shop.member.model.vo.MemberLoginMsg;

/**
 * 会员完善个人信息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:05:14
 */
@Component
public class MemberInfoCompleteReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IMemberInfoCompleteEvent> events;
	
	/**
	 * 会员完善个人信息
	 * @param member_id
	 */
	public void memberInfoComplete(Integer member_id){
		
		try{
			if(events!=null){
				for(IMemberInfoCompleteEvent event : events){
					event.memberInfoComplete(member_id);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理完善个人信息消息出错",e);
			e.printStackTrace();
		}
	}
}
