package com.enation.app.core.receiver;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.cms.floor.model.vo.CmsManageMsg;
import com.enation.app.core.event.IIndexChangeEvent;
/**
 * 
 * 首页生成
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:41:18
 */
@Component
public class IndexChangeReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IIndexChangeEvent> events;
	
	/**
	 * 生成首页
	 */
	public void createIndexPage(CmsManageMsg operation) {
		try{
			if(events!=null){
				for(IIndexChangeEvent event : events){
					event.createIndexPage(operation);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理首页生成消息出错",e);
			e.printStackTrace();
		}
	}

}
