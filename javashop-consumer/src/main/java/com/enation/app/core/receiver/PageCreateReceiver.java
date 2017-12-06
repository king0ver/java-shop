package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IPageCreateEvent;

/**
 * 页面生成
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:58:23
 */
@Component
public class PageCreateReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IPageCreateEvent> events;
	
	/**
	 * 页面生成
	 * @param choose_pages
	 */
	public void createPage(String[] choose_pages){
		
		try{
			if(events!=null){
				for(IPageCreateEvent event : events){
					event.createPage(choose_pages);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理页面生成出错",e);
			e.printStackTrace();
		}
	}
}
