package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.ICategoryChangeEvent;
import com.enation.app.core.event.IOnlinePayEvent;
import com.enation.framework.jms.support.goods.CategoryChangeMsg;

/**
 * 在线支付
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:47:22
 */
@Component
public class OnlinePayReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IOnlinePayEvent> events;
	
	
	/**
	 * 在线支付
	 * @param member_id
	 */
	public void onlinePay(Integer member_id){
		try {
			if(events!=null){
				for(IOnlinePayEvent event : events){
					event.onlinePay(member_id);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理在线支付消息出错", e);
			e.printStackTrace();
		}
	}
}
