package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IMemberMessageEvent;

/**
 * 站内消息
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午6:00:01
 */
@Component
public class MemberMessageReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IMemberMessageEvent> events;
	
	/**
	 * 站内消息
	 * @param goodsChangeMsg
	 */
	public void memberMessage(int messageid){
		
		try{
			if(events!=null){
				for(IMemberMessageEvent event : events){
					event.memberMessage(messageid);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理站内消息出错",e);
			e.printStackTrace();
		}
	}
}
