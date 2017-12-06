package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.ISendEmailEvent;
import com.enation.framework.jms.EmailModel;

/**
 * 发送邮件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午6:30:35
 */
@Component
public class SendEmailReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<ISendEmailEvent> events;
	
	/**
	 * 发送邮件
	 * @param categoryChangeMsg
	 */
	public void sendEmail(EmailModel emailModel){
		
		try{
			if(events!=null){
				for(ISendEmailEvent event : events){
					event.sendEmail(emailModel);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理发送邮件消息出错",e);
			e.printStackTrace();
		}
	}
}
