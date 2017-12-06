package com.enation.app.core.receiver;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.ISmsSendMessageEvent;

/**
 * 发送短信
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午6:05:28
 */
@Component
public class SmsSendMessageReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<ISmsSendMessageEvent> events;
	
	/**
	 * 商品分类变化
	 * @param goodsChangeMsg
	 */
	public void sendMessage(Map<String,Object> data){
		
		try{
			if(events!=null){
				for(ISmsSendMessageEvent event : events){
					event.sendMessage(data);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理发送短信消息出错",e);
			e.printStackTrace();
		}
	}
}
