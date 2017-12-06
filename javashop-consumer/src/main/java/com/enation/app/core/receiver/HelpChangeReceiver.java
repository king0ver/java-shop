package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IHelpChangeEvent;
/**
 * 
 * 帮助中心页面生成
 * @author zh
 * @version v1.0
 * @since v1.0
 * 2017年8月29日 下午3:40:48
 */
@Component
public class HelpChangeReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IHelpChangeEvent> events;
	
	/**
	 * 消费执行者
	 * @param articeids
	 */
	public void helpChange(List<Integer> articeids) {
		try{
			if(events!=null){
				for(IHelpChangeEvent event : events){
					event.helpChange(articeids);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理帮助中心页面生成消息出错",e);
			e.printStackTrace();
		}
	}
}
